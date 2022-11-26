package yio.tro.opacha.menu.scenes.editor;

import yio.tro.opacha.game.editor.EditorSavesManager;
import yio.tro.opacha.game.editor.EsmItem;
import yio.tro.opacha.game.loading.LoadingParameters;
import yio.tro.opacha.game.loading.LoadingType;
import yio.tro.opacha.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.opacha.menu.elements.customizable_list.ScrollListItem;
import yio.tro.opacha.menu.elements.customizable_list.SliReaction;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.SceneYio;
import yio.tro.opacha.menu.scenes.Scenes;

public class SceneEditorLoad extends SceneYio {

    private SliReaction clickReaction;
    private SliReaction longTapReaction;
    private CustomizableListYio customizableListYio;


    public SceneEditorLoad() {
        customizableListYio = null;
    }


    @Override
    protected void initialize() {
        setBackground(GroundIndex.GREEN);
        initReactions();
        createList();
        createCloseButton();
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05);
    }


    private void initReactions() {
        clickReaction = new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                SceneEditorLoad.this.onItemClicked(item.key);
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


    @Override
    protected void onAppear() {
        super.onAppear();
        updateList();
    }


    private EditorSavesManager getEditorSavesManager() {
        return yioGdxGame.gameController.editorSavesManager;
    }


    private void onItemClicked(String key) {
        String levelCode = getEditorSavesManager().getLevelCode(key);
        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.addParameter("level_code", levelCode);
        loadingParameters.addParameter("slot_key", key);
        yioGdxGame.loadingManager.startInstantly(LoadingType.editor_load, loadingParameters);
    }


    public void updateList() {
        if (customizableListYio == null) return;

        customizableListYio.clearItems();
        EditorSavesManager editorSavesManager = getEditorSavesManager();
        int size = editorSavesManager.items.size();
        for (int i = size - 1; i >= 0; i--) {
            EsmItem item = editorSavesManager.items.get(i);

            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setTitle(item.name);
            scrollListItem.setKey(item.index);
            scrollListItem.setClickReaction(clickReaction);
            scrollListItem.setLongTapReaction(longTapReaction);
            scrollListItem.setDarken((size - i) % 2 == 1);
            customizableListYio.addItem(scrollListItem);
        }
    }


    private void createCloseButton() {
        spawnBackButton(new Reaction() {
            @Override
            protected void reaction() {
                Scenes.editorLobby.create();
            }
        });
    }
}
