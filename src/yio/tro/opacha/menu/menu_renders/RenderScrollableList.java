package yio.tro.opacha.menu.menu_renders;

import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.scrollable_list.ScrItem;
import yio.tro.opacha.menu.elements.scrollable_list.ScrollableListElement;
import yio.tro.opacha.stuff.GraphicsYio;

public class RenderScrollableList extends RenderInterfaceElement{


    private ScrollableListElement scrollableList;
    private float alpha;
    private ScrItem currentItem;
    private float cornerRadius;


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        scrollableList = (ScrollableListElement) element;
        alpha = scrollableList.getAlpha();
        cornerRadius = scrollableList.cornerRadius;

        renderInternals();

        GraphicsYio.setBatchAlpha(batch, 1);
        GraphicsYio.setFontAlpha(scrollableList.titleFont, 1);
        GraphicsYio.setFontAlpha(scrollableList.descFont, 1);
    }


    private void renderInternals() {
        for (ScrItem item : scrollableList.items) {
            if (!item.isVisible()) continue;

            renderSingleItem(item);
        }
    }


    private void renderSingleItem(ScrItem item) {
        currentItem = item;

        renderItemBackground();
        renderItemBorder();
        renderItemText();
        renderItemSelection();
    }


    private void renderItemSelection() {
        if (!currentItem.isSelected()) return;

        GraphicsYio.setBatchAlpha(batch, currentItem.selectionFactor.get() * 0.25);
        MenuRenders.renderRoundShape.renderRoundShape(currentItem.viewPosition, -1, cornerRadius);
    }


    private void renderItemText() {
        if (areSomeDetailsCurrentlyHidden()) return;

        GraphicsYio.setFontAlpha(scrollableList.titleFont, alpha);
        GraphicsYio.renderText(batch, scrollableList.titleFont, currentItem.title, currentItem.titlePosition);

        GraphicsYio.setFontAlpha(scrollableList.descFont, alpha);
        GraphicsYio.renderText(batch, scrollableList.descFont, currentItem.description, currentItem.descriptionPosition);
    }


    private void renderItemBorder() {
        GraphicsYio.setBatchAlpha(batch, 0.25f * alpha);
        MenuRenders.renderRoundBorder.renderRoundBorder(currentItem.viewPosition, cornerRadius);
    }


    private void renderItemBackground() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        int groundIndex = scrollableList.getSceneOwner().getGround().getGroundIndex();
        MenuRenders.renderRoundShape.renderRoundShape(currentItem.viewPosition, groundIndex, cornerRadius);
        GraphicsYio.setBatchAlpha(batch, 0.15f * alpha);
        MenuRenders.renderRoundShape.renderRoundShape(currentItem.viewPosition, 5, cornerRadius);
    }


    private boolean areSomeDetailsCurrentlyHidden() {
        return alpha < 0.5;
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
