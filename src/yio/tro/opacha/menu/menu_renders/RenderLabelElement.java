package yio.tro.opacha.menu.menu_renders;

import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.LabelElement;
import yio.tro.opacha.stuff.GraphicsYio;

public class RenderLabelElement extends RenderInterfaceElement{


    private LabelElement labelElement;


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        labelElement = (LabelElement) element;

        GraphicsYio.renderTextOptimized(batch, blackPixel, labelElement.title, labelElement.getFactor().get());
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
