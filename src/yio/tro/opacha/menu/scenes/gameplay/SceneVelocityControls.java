package yio.tro.opacha.menu.scenes.gameplay;

import yio.tro.opacha.menu.elements.VelocitySliderElement;
import yio.tro.opacha.menu.scenes.ModalSceneYio;
import yio.tro.opacha.stuff.GraphicsYio;

public class SceneVelocityControls extends ModalSceneYio{


    public VelocitySliderElement velocitySliderElement;


    @Override
    protected void initialize() {
        velocitySliderElement = uiFactory.getVelocitySliderElement()
                .setParent(followGameViewElement)
                .setSize(0.4, 0.028)
                .alignTop(GraphicsYio.convertToHeight(0.03) + 0.01)
                .alignLeft(0.03);
    }
}
