package yio.tro.opacha.menu.menu_renders;

import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.ScrollableAreaYio;

public class RenderScrollableArea extends RenderInterfaceElement {

    @Override
    public void loadTextures() {

    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        ScrollableAreaYio scrollableAreaYio = (ScrollableAreaYio) element;

//        renderDebug(scrollableAreaYio);
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
