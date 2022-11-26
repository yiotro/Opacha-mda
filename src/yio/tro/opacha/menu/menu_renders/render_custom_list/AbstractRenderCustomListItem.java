package yio.tro.opacha.menu.menu_renders.render_custom_list;

import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RenderableTextYio;

public abstract class AbstractRenderCustomListItem extends RenderInterfaceElement{


    public abstract void loadTextures();


    public abstract void renderItem(AbstractCustomListItem item);


    protected void renderDefaultSelection(AbstractCustomListItem item) {
        if (!item.selectionEngineYio.isSelected()) return;

        GraphicsYio.setBatchAlpha(batch, item.selectionEngineYio.getAlpha() * item.customizableListYio.getAlpha());
        GraphicsYio.drawByRectangle(batch, blackPixel, item.viewPosition);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {

    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }


    protected void renderTextOptimized(RenderableTextYio renderableTextYio, float alpha) {
        GraphicsYio.renderTextOptimized(batch, blackPixel, renderableTextYio, alpha);
    }
}
