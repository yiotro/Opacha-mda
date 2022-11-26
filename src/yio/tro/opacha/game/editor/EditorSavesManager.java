package yio.tro.opacha.game.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.GameController;
import yio.tro.opacha.stuff.DateYio;

import java.util.ArrayList;

public class EditorSavesManager {

    GameController gameController;
    public ArrayList<EsmItem> items;
    private static final String PREFS = "yio.tro.opacha.editor_saves";


    public EditorSavesManager(GameController gameController) {
        this.gameController = gameController;
        items = new ArrayList<>();
        loadValues();
    }


    public EsmItem addItem(String name, String levelCode) {
        EsmItem esmItem = new EsmItem();
        esmItem.index = getNewIndex();
        esmItem.name = name;
        esmItem.levelCode = levelCode;
        items.add(esmItem);
        saveValues();
        return esmItem;
    }


    public void rewriteLevelCode(String index, String levelCode) {
        EsmItem item = getItem(index);
        if (item == null) return;

        item.levelCode = levelCode;
        saveValues();
    }


    public void renameItem(EsmItem item, String name) {
        item.name = name;
        saveValues();
    }


    public void removeItem(EsmItem item) {
        items.remove(item);
        saveValues();
    }


    public void saveValues() {
        Preferences preferences = getPreferences();
        preferences.putString("indexes", createIndexesString());
        for (EsmItem item : items) {
            preferences.putString("name_" + item.index, item.name);
            preferences.putString("level_code_" + item.index, item.levelCode);
        }
        preferences.flush();
    }


    private String createIndexesString() {
        StringBuilder builder = new StringBuilder();
        for (EsmItem item : items) {
            builder.append(item.index).append(" ");
        }
        return builder.toString();
    }


    public void loadValues() {
        Preferences preferences = getPreferences();
        String indexes = preferences.getString("indexes", "");
        items.clear();
        DateYio dateYio = new DateYio();
        dateYio.applyCurrentDay();
        for (String index : indexes.split(" ")) {
            if (index.length() == 0) continue;
            EsmItem esmItem = new EsmItem();
            esmItem.index = index;
            esmItem.name = preferences.getString("name_" + index, dateYio.toString());
            esmItem.levelCode = preferences.getString("level_code_" + index, "");
            items.add(esmItem);
        }
    }


    public EsmItem getItem(String index) {
        if (index == null) return null;
        for (EsmItem item : items) {
            if (!item.index.equals(index)) continue;
            return item;
        }
        return null;
    }


    public EsmItem getLastItem() {
        if (items.size() == 0) return null;

        return items.get(items.size() - 1);
    }


    public String getLevelCode(String index) {
        EsmItem item = getItem(index);
        if (item == null) return null;
        return item.levelCode;
    }


    private boolean isIndexUsed(String index) {
        return getItem(index) != null;
    }


    private String getNewIndex() {
        while (true) {
            int value = YioGdxGame.random.nextInt(100000);
            String index = "" + value;
            if (isIndexUsed(index)) continue;
            return index;
        }
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences(PREFS);
    }

}
