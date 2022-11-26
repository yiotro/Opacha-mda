package yio.tro.opacha.menu.scenes.gameplay;

import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.BackgroundYio;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.ModalSceneYio;

public class SceneEndSpectate extends ModalSceneYio{

    @Override
    protected void initialize() {
        uiFactory.getButton()
                .setSize(0.5, 0.06)
                .centerHorizontal()
                .alignBottom(0.02)
                .setTouchOffset(0.07)
                .setKey("end_spectate")
                .applyText("next")
                .setBackgroundEnabled(true)
                .setGroundIndex(GroundIndex.GREEN)
                .setReaction(getReaction())
                .setAnimation(AnimationYio.down);
    }


    private Reaction getReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                gameController.objectsLayer.spectateManager.doFinish();
            }
        };
    }
}
