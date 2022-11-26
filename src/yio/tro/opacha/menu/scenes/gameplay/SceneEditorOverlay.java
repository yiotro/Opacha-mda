package yio.tro.opacha.menu.scenes.gameplay;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.game.touch_modes.TouchMode;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.ModalSceneYio;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.GraphicsYio;

public class SceneEditorOverlay extends ModalSceneYio{

    private double h;
    private double w;
    private ButtonYio label = null;
    private double offset;
    public ButtonYio addButton;
    public ButtonYio removeButton;
    public ButtonYio optionsButton;


    @Override
    protected void initialize() {
        w = 0.08;
        h = GraphicsYio.convertToHeight(w);
        offset = 0.04;

        createLabel();
        createIcons();
    }


    private void createIcons() {
        TextureRegion selectionTexture = getSelectionTexture();

        optionsButton = uiFactory.getButton()
                .setParent(label)
                .setSize(w)
                .alignRight(offset)
                .alignBottom()
                .loadTexture("menu/editor/open_icon.png")
                .setTouchOffset(offset / 2)
                .setSelectionTexture(selectionTexture)
                .setReaction(getOptionsReaction());

        addButton = uiFactory.getButton()
                .clone(previousElement)
                .alignLeft(offset)
                .loadTexture("menu/editor/plus_icon.png")
                .setSelectionTexture(selectionTexture)
                .setReaction(getAdditionReaction());

        removeButton = uiFactory.getButton()
                .clone(previousElement)
                .alignRight(previousElement, offset)
                .loadTexture("menu/editor/remove_icon.png")
                .setSelectionTexture(selectionTexture)
                .setReaction(getRemoveReaction());
    }


    private Reaction getRemoveReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                gameController.setTouchMode(TouchMode.tmDeletion);
            }
        };
    }


    private Reaction getAdditionReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                gameController.setTouchMode(TouchMode.tmAddition);
            }
        };
    }


    private Reaction getOptionsReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.editorOptionsPanel.create();
            }
        };
    }


    private void createLabel() {
        label = uiFactory.getButton()
                .setSize(1, h)
                .setTouchable(false)
                .setAnimation(AnimationYio.down)
                .setRenderable(false);
    }


    public boolean isCurrentlyVisible() {
        if (label == null) return false;

        return label.getFactor().get() > 0;
    }
}
