package yio.tro.opacha.menu.elements.scrollable_list;

import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.RectangleYio;
import yio.tro.opacha.stuff.factor_yio.FactorYio;

public class ScrItem {


    ScrollableListElement scrollableListElement;
    public RectangleYio viewPosition;
    public String title, description, key;
    public FactorYio selectionFactor;
    PointYio titleDelta, descriptionDelta, positionDelta;
    public PointYio titlePosition, descriptionPosition;


    public ScrItem(ScrollableListElement scrollableListElement) {
        this.scrollableListElement = scrollableListElement;

        viewPosition = new RectangleYio();
        title = "";
        description = "";
        key = "";
        selectionFactor = new FactorYio();
        titleDelta = new PointYio();
        descriptionDelta = new PointYio();
        positionDelta = new PointYio();
        titlePosition = new PointYio();
        descriptionPosition = new PointYio();
    }


    void updatePositions() {
        updateViewPosition();
        updateTitlePosition();
        updateDescriptionPosition();
    }


    private void updateDescriptionPosition() {
        descriptionPosition.x = viewPosition.x + descriptionDelta.x;
        descriptionPosition.y = viewPosition.y + descriptionDelta.y;
    }


    private void updateTitlePosition() {
        titlePosition.x = viewPosition.x + titleDelta.x;
        titlePosition.y = viewPosition.y + titleDelta.y;
    }


    private void updateViewPosition() {
        viewPosition.x = scrollableListElement.getViewPosition().x + positionDelta.x;
        viewPosition.y = scrollableListElement.getViewPosition().y + positionDelta.y - scrollableListElement.hook;
        viewPosition.height = (float) scrollableListElement.itemHeight;
        viewPosition.width = scrollableListElement.getPosition().width;
    }


    void moveSelection() {
        if (!isSelected()) return;

        selectionFactor.move();
    }


    boolean isTouched(PointYio touchPoint) {
        return InterfaceElement.isTouchInsideRectangle(touchPoint, viewPosition);
    }


    public void select() {
        selectionFactor.setValues(1, 0);
        selectionFactor.destroy(1, 3);
    }


    public boolean isVisible() {
        if (viewPosition.y + viewPosition.height < 0) return false;
        if (viewPosition.y > GraphicsYio.height) return false;

        return true;
    }


    void updateDeltas() {
        titleDelta.x = 0.1f * GraphicsYio.width;
        titleDelta.y = 0.8f * viewPosition.height;

        descriptionDelta.x = 0.1f * GraphicsYio.width;
        descriptionDelta.y = 0.45f * viewPosition.height;
    }


    public boolean isSelected() {
        return selectionFactor.get() > 0;
    }


    public void set(String title, String description, String key) {
        this.title = title;
        this.description = description;
        this.key = key;
        updateDeltas();
    }


    public void setTitle(String title) {
        this.title = title;
        updateDeltas();
    }
}
