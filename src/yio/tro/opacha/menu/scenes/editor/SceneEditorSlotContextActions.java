package yio.tro.opacha.menu.scenes.editor;

import yio.tro.opacha.game.editor.EditorSavesManager;
import yio.tro.opacha.game.editor.EsmItem;
import yio.tro.opacha.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.ModalSceneYio;
import yio.tro.opacha.menu.scenes.Scenes;

public class SceneEditorSlotContextActions extends ModalSceneYio {

    String index;
    private EsmItem item;


    @Override
    protected void initialize() {
        createCloseButton();
        createDefaultLabel(0.8, 0.3);
        createInternals();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        forceElementsToTop();
    }


    private void createInternals() {
        uiFactory.getButton()
                .setParent(defaultLabel)
                .setSize(0.7, 0.06)
                .centerHorizontal()
                .alignBottom(0.02)
                .applyText("delete")
                .setReaction(getDeleteReaction());

        uiFactory.getButton()
                .clone(previousElement)
                .alignTop(previousElement, 0.02)
                .applyText("rename")
                .setReaction(getRenameReaction());
    }


    private Reaction getRenameReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.keyboard.create();
                Scenes.keyboard.setValue(item.name);
                Scenes.keyboard.setReaction(getKeyboardReaction());
            }
        };
    }


    private AbstractKbReaction getKeyboardReaction() {
        return new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                getEditorSavesManager().renameItem(item, input);
                onChangeApplied();
            }
        };
    }


    private void onChangeApplied() {
        Scenes.editorLoad.updateList();
        Scenes.editorSave.updateList();
        destroy();
    }


    private Reaction getDeleteReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                getEditorSavesManager().removeItem(item);
                onChangeApplied();
            }
        };
    }


    public void setIndex(String index) {
        this.index = index;
        item = getEditorSavesManager().getItem(index);

        defaultLabel.applyText(item.name);
    }


    private EditorSavesManager getEditorSavesManager() {
        return yioGdxGame.gameController.editorSavesManager;
    }
}
