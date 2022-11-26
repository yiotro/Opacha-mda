package yio.tro.opacha.menu.menu_renders;

import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.ScrollHelperElement;
import yio.tro.opacha.stuff.GraphicsYio;

public class RenderScrollHelperElement extends RenderInterfaceElement{


    private ScrollHelperElement shElement;
    private float alpha;


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        shElement = (ScrollHelperElement) element;
        alpha = shElement.getAlpha();

        renderRoad();
        renderWagon();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderRoad() {
        if (!shElement.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, 0.5 * shElement.selectionEngineYio.getAlpha() * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, shElement.roadPosition);
    }


    private void renderWagon() {
        if (shElement.fadeInFactor.get() == 0) return;
        GraphicsYio.setBatchAlpha(batch, (0.25 + 3 * shElement.selectionEngineYio.getAlpha()) * alpha * shElement.fadeInFactor.get());
        GraphicsYio.drawByRectangle(batch, blackPixel, shElement.wagonPosition);
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
