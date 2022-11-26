package yio.tro.opacha.menu.scenes.gameplay;

import yio.tro.opacha.menu.elements.tutorial.GlobalMessageElement;
import yio.tro.opacha.menu.scenes.ModalSceneYio;

public class SceneGlobalMessage extends ModalSceneYio{

    public GlobalMessageElement globalMessageElement;


    public SceneGlobalMessage() {
        globalMessageElement = null;
    }


    @Override
    protected void initialize() {
        globalMessageElement = uiFactory.getGlobalMessageElement()
                .setFakeDyingStatusEnabled(true)
                .setSize(1, 1);
    }


    public boolean isCurrentlyVisible() {
        return globalMessageElement != null && globalMessageElement.getFactor().get() > 0;
    }

}
