package yio.tro.opacha.menu.elements.customizable_list;

import yio.tro.opacha.game.GameController;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.opacha.stuff.*;

public abstract class AbstractCustomListItem {

    public CustomizableListYio customizableListYio;
    public RectangleYio viewPosition;
    public PointYio positionDelta;
    public SelectionEngineYio selectionEngineYio;


    public AbstractCustomListItem() {
        customizableListYio = null;
        viewPosition = new RectangleYio();
        positionDelta = new PointYio();
        selectionEngineYio = new SelectionEngineYio();
        initialize();
    }


    public void moveItem() {
        updateViewPosition();
        move();
    }


    protected abstract void initialize();


    protected abstract void move();


    private void updateViewPosition() {
        viewPosition.x = customizableListYio.maskPosition.x + positionDelta.x;
        viewPosition.y = customizableListYio.maskPosition.y + positionDelta.y + customizableListYio.hook;
        viewPosition.width = (float) getWidth();
        viewPosition.height = (float) getHeight();
    }


    public boolean isCurrentlyVisible() {
        if (viewPosition.y + viewPosition.height < customizableListYio.getPosition().y) return false;
        if (viewPosition.y > customizableListYio.getPosition().y + customizableListYio.getPosition().height) return false;

        return true;
    }


    public boolean isTouched(PointYio touchPoint) {
        return InterfaceElement.isTouchInsideRectangle(touchPoint, viewPosition);
    }


    protected abstract double getWidth();


    protected double getDefaultWidth() {
        return 0.98 * customizableListYio.maskPosition.width;
    }


    protected abstract double getHeight();


    protected abstract void onPositionChanged();


    public void onTouchDown(PointYio touchPoint) {
        // nothing by default
    }


    protected abstract void onClicked();


    protected GameController getGameController() {
        return customizableListYio.menuControllerYio.yioGdxGame.gameController;
    }


    protected abstract void onLongTapped();


    public abstract AbstractRenderCustomListItem getRender();


    protected void moveRenderableTextByDefault(RenderableTextYio renderableTextYio) {
        renderableTextYio.position.x = viewPosition.x + renderableTextYio.delta.x;
        renderableTextYio.position.y = viewPosition.y + renderableTextYio.delta.y;
        renderableTextYio.updateBounds();
    }


    public void setCustomizableListYio(CustomizableListYio customizableListYio) {
        this.customizableListYio = customizableListYio;
    }
}
