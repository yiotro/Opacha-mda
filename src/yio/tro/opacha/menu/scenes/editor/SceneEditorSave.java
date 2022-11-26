package yio.tro.opacha.menu.scenes.editor;

import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.editor.EditorSavesManager;
import yio.tro.opacha.game.editor.EsmItem;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.opacha.menu.elements.customizable_list.ScrollListItem;
import yio.tro.opacha.menu.elements.customizable_list.SliReaction;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.SceneYio;
import yio.tro.opacha.menu.scenes.Scenes;

public class SceneEditorSave extends SceneYio{

    private CustomizableListYio customizableListYio;
    private SliReaction clickReaction;
    private SliReaction longTapReaction;


    public SceneEditorSave() {
        customizableListYio = null;
    }


    @Override
    protected void initialize() {
        setBackground(GroundIndex.GREEN);
        initReactions();
        createList();
        createCloseButton();
    }


    private void initReactions() {
        clickReaction = new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                SceneEditorSave.this.onItemClicked(item.key);
            }
        };

        longTapReaction = new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                Scenes.editorSlotContextActions.create();
                Scenes.editorSlotContextActions.setIndex(item.key);
            }
        };
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        updateList();
    }


    public void updateList() {
        if (customizableListYio == null) return;

        customizableListYio.clearItems();
        EditorSavesManager editorSavesManager = getEditorSavesManager();

        ScrollListItem firstListItem = new ScrollListItem();
        firstListItem.setTitle("[" + LanguagesManager.getInstance().getString("create") + "]");
        firstListItem.setKey("create");
        firstListItem.setClickReaction(clickReaction);
        firstListItem.setDarken(true);
        customizableListYio.addItem(firstListItem);

        int size = editorSavesManager.items.size();
        for (int i = size - 1; i >= 0; i--) {
            EsmItem item = editorSavesManager.items.get(i);
            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setTitle(item.name);
            scrollListItem.setKey(item.index);
            scrollListItem.setClickReaction(clickReaction);
            scrollListItem.setLongTapReaction(longTapReaction);
            scrollListItem.setDarken((size - i) % 2 == 0);
            customizableListYio.addItem(scrollListItem);
        }
    }


    private EditorSavesManager getEditorSavesManager() {
        return yioGdxGame.gameController.editorSavesManager;
    }


    private void onItemClicked(String key) {
        String exportedLevelCode = yioGdxGame.gameController.objectsLayer.exportManager.perform();

        if (key.equals("create")) {
            showKeyboardForNewSave(exportedLevelCode);
            return;
        }

        getEditorSavesManager().rewriteLevelCode(key, exportedLevelCode);
        Scenes.editorPauseMenu.create();
    }


    private void showKeyboardForNewSave(final String exportedLevelCode) {
        Scenes.keyboard.create();
        Scenes.keyboard.setValue("");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                EsmItem esmItem = getEditorSavesManager().addItem(input, exportedLevelCode);
                Scenes.editorPauseMenu.create();
            }
        });
    }


    private void createCloseButton() {
        spawnBackButton(new Reaction() {
            @Override
            protected void reaction() {
                Scenes.editorPauseMenu.create();
            }
        });
    }

}
