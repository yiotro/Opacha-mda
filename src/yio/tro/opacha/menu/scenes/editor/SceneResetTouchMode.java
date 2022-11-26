package yio.tro.opacha.menu.scenes.editor;

import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.ModalSceneYio;
import yio.tro.opacha.stuff.GraphicsYio;

public class SceneResetTouchMode extends ModalSceneYio{

    public ButtonYio applyButton;


    @Override
    protected void initialize() {
        applyButton = uiFactory.getButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .centerHorizontal()
                .alignBottom()
                .setAnimation(AnimationYio.down)
                .setSelectionTexture(getSelectionTexture())
                .loadTexture("menu/editor/apply_icon.png")
                .setReaction(getApplyReaction());
    }


    private Reaction getApplyReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                gameController.resetTouchMode();
                destroy();
            }
        };
    }

}
