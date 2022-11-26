package yio.tro.opacha.menu.scenes.gameplay;

import yio.tro.opacha.menu.elements.LockCameraElement;
import yio.tro.opacha.menu.scenes.ModalSceneYio;
import yio.tro.opacha.stuff.GraphicsYio;

public class SceneLockCamera extends ModalSceneYio{


    public LockCameraElement lockCameraElement;


    @Override
    protected void initialize() {
        lockCameraElement = uiFactory.getLockCameraElement()
                .setParent(followGameViewElement)
                .setSize(GraphicsYio.convertToWidth(0.05))
                .alignRight(0.03)
                .alignBottom(GraphicsYio.convertToHeight(0.03));
    }
}
