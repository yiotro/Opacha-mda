package yio.tro.opacha.menu.scenes.gameplay;

import yio.tro.opacha.menu.scenes.ModalSceneYio;

public class SceneQuickVelocityTutorial extends ModalSceneYio{

    @Override
    protected void initialize() {
        uiFactory.getQuickVelocityTutorialElement()
                .setAppearParameters(3, 0.5)
                .setSize(1, 1);
    }
}
