package yio.tro.opacha.menu.menu_renders.render_custom_list;

import yio.tro.opacha.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.opacha.menu.elements.customizable_list.ScrollListItem;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RectangleYio;

public class RenderScrollListItem extends AbstractRenderCustomListItem{

    ScrollListItem scrollListItem;
    RectangleYio tempRectangle;
    private float alpha;


    public RenderScrollListItem() {
        tempRectangle = new RectangleYio();
    }


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        scrollListItem = (ScrollListItem) item;
        alpha = scrollListItem.customizableListYio.getAlpha();

        renderDarken();
        renderHighlight();
        renderTextOptimized(scrollListItem.title, alpha);
        renderDefaultSelection(scrollListItem);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderHighlight() {
        if (!scrollListItem.isHighlighted()) return;

        RectangleYio pos = scrollListItem.viewPosition;
        tempRectangle.x = pos.x;
        tempRectangle.width = 4 * GraphicsYio.borderThickness;
        tempRectangle.y = pos.y + 0.2f * pos.height;
        tempRectangle.height = 0.6f * pos.height;

        GraphicsYio.setBatchAlpha(batch, 0.5 * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, tempRectangle);
    }


    private void renderDarken() {
        if (!scrollListItem.darken) return;
        GraphicsYio.setBatchAlpha(batch, scrollListItem.darkValue * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, scrollListItem.viewPosition);
    }
}
