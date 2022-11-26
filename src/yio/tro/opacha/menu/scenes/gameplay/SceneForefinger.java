package yio.tro.opacha.menu.scenes.gameplay;

import yio.tro.opacha.menu.elements.tutorial.ForefingerElement;
import yio.tro.opacha.menu.scenes.ModalSceneYio;

public class SceneForefinger extends ModalSceneYio{

    public ForefingerElement forefingerElement;


    public SceneForefinger() {
        forefingerElement = null;
    }


    @Override
    protected void initialize() {
        forefingerElement = uiFactory.getForefingerElement()
                .setFakeDyingStatusEnabled(true)
                .setSize(1, 1);
    }


    public boolean isCurrentlyVisible() {
        return forefingerElement != null && forefingerElement.getFactor().get() > 0;
    }
}
