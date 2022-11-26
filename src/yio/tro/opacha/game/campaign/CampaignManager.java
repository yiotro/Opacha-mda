package yio.tro.opacha.game.campaign;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.opacha.menu.elements.customizable_list.CciType;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.TimeMeasureYio;

import java.util.ArrayList;

public class CampaignManager {


    private static CampaignManager instance;
    public static final int LAST_LEVEL_INDEX = 1517;
    private static final String PREFS = "yio.tro.opacha.campaign";
    ArrayList<Integer> completedLevels;


    public CampaignManager() {
        completedLevels = new ArrayList<>();
    }


    public static void initialize() {
        instance = null;
        getInstance(); // load
    }


    public static CampaignManager getInstance() {
        if (instance == null) {
            instance = new CampaignManager();
            instance.loadValues();
        }

        return instance;
    }


    public void loadValues() {
        Preferences preferences = getPreferences();
        String source = preferences.getString("progress");
        String[] split = source.split(" ");
        completedLevels.clear();
        for (String token : split) {
            if (token.length() < 1) continue;
            completedLevels.add(Integer.valueOf(token));
        }
    }


    public void onLevelCompleted(int index) {
        if (isLevelCompleted(index)) return;
        completedLevels.add(index);
        Scenes.campaign.onLevelMarkedAsCompleted();
        saveValues();
    }


    public int getNextLevelIndex(int index) {
        if (index >= LAST_LEVEL_INDEX) return index;

        int nextIndex = index + 1;
        while (nextIndex != index && isLevelCompleted(nextIndex)) {
            nextIndex++;
            if (nextIndex > LAST_LEVEL_INDEX) {
                nextIndex = 0;
            }
        }

        return nextIndex;
    }


    public boolean isLevelCompleted(int index) {
        if (index == -1) return true;

        for (int completedLevel : completedLevels) {
            if (completedLevel == index) return true;
        }
        return false;
    }


    public boolean areAllLevelsCompleted() {
        return completedLevels.size() > LAST_LEVEL_INDEX;
    }


    public void saveValues() {
        Preferences preferences = getPreferences();
        StringBuilder builder = new StringBuilder();
        for (Integer completedLevel : completedLevels) {
            builder.append(completedLevel).append(" ");
        }
        preferences.putString("progress", builder.toString());
        preferences.flush();
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences(PREFS);
    }


    public int getIndexOfHighestUnlockedLevel() {
        int result = 0;
        for (int index = 0; index < LAST_LEVEL_INDEX; index++) {
            if (getLevelType(index) != CciType.unlocked) continue;
            result = index;
        }
        return result;
    }


    public int getNumberOfCompletedLevels() {
        return completedLevels.size();
    }


    public CciType getLevelType(int levelIndex) {
        if (isLevelCompleted(levelIndex)) {
            return CciType.completed;
        }
        if (isLevelCompleted(levelIndex - 1) || isLevelCompleted(levelIndex - 2) || isLevelCompleted(levelIndex - 3)) {
            return CciType.unlocked;
        }
        return CciType.unknown;
    }

}
