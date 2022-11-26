package yio.tro.opacha.menu.elements.calendar;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.stuff.CircleYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.RenderableTextYio;
import yio.tro.opacha.stuff.SelectionEngineYio;

public class CveDayButton {

    CveTab tab;
    public CircleYio position;
    PointYio delta;
    int index;
    public RenderableTextYio title;
    public SelectionEngineYio selectionEngineYio;
    public CveDayState state;


    public CveDayButton(CveTab tab) {
        this.tab = tab;
        position = new CircleYio();
        delta = new PointYio();
        index = -1;
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        selectionEngineYio = new SelectionEngineYio();
        state = null;
    }


    void move() {
        updatePosition();
        moveTitle();
        moveSelection();
    }


    private void moveSelection() {
        if (tab.calendarViewElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    boolean isTouchedBy(PointYio touchPoint) {
        if (Math.abs(position.center.x - touchPoint.x) > position.radius) return false;
        if (Math.abs(position.center.y - touchPoint.y) > position.radius) return false;
        return true;
    }


    private void moveTitle() {
        if (state != CveDayState.unlocked) return;
        title.position.x = position.center.x - title.width / 2;
        title.position.y = position.center.y + title.height / 2;
        title.updateBounds();
    }


    private void updatePosition() {
        position.center.x = tab.position.x + delta.x;
        position.center.y = tab.position.y + delta.y;
    }


    public void setIndex(int index) {
        this.index = index;
        title.setString("" + index);
        title.updateMetrics();
    }


    public void setState(CveDayState state) {
        this.state = state;
    }
}
