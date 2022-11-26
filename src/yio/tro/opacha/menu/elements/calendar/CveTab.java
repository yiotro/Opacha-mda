package yio.tro.opacha.menu.elements.calendar;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.Yio;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.RectangleYio;
import yio.tro.opacha.stuff.RenderableTextYio;
import yio.tro.opacha.stuff.calendar.CalendarManager;
import yio.tro.opacha.stuff.calendar.CveColorYio;
import yio.tro.opacha.stuff.calendar.CveMonth;

import java.util.ArrayList;

public class CveTab {

    CalendarViewElement calendarViewElement;
    RectangleYio defaultPosition;
    public RectangleYio position;
    public RectangleYio topArea;
    public CveMonth month;
    public RenderableTextYio title;
    public ArrayList<CveDayButton> dayButtons;
    float vDelta;
    float separatorY;
    public boolean completed;
    public boolean fullYearCompletedMode;


    public CveTab(CalendarViewElement calendarViewElement) {
        this.calendarViewElement = calendarViewElement;
        defaultPosition = new RectangleYio();
        position = new RectangleYio();
        topArea = new RectangleYio();
        month = null;
        title = new RenderableTextYio();
        title.setFont(Fonts.buttonFont);
        dayButtons = new ArrayList<>();
        vDelta = 0.05f * GraphicsYio.height;
        separatorY = 0.6f * GraphicsYio.height;
        completed = false;
        fullYearCompletedMode = false;
    }


    void move() {
        updatePosition();
        if (!isCurrentlyVisible()) return;
        updateTopArea();
        updateTitlePosition();
        moveDayButtons();
    }


    private void moveDayButtons() {
        for (CveDayButton dayButton : dayButtons) {
            dayButton.move();
        }
    }


    private void updateTitlePosition() {
        title.centerHorizontal(topArea);
        if (completed) {
            title.position.y = 0.45f * GraphicsYio.height + title.height / 2;
        } else {
            title.position.y = topArea.y + vDelta + title.height;
        }
        title.updateBounds();
    }


    private void updateTopArea() {
        if (completed) {
            topArea.setBy(position);
            return;
        }
        topArea.x = position.x;
        topArea.width = position.width;
        topArea.height = position.height - separatorY;
        topArea.y = separatorY;
    }


    private void updatePosition() {
        position.setBy(defaultPosition);
        position.x = (float) (calendarViewElement.getViewPosition().x + defaultPosition.x + calendarViewElement.tabsEngineYio.getSlider().a);
    }


    public boolean isCurrentlyVisible() {
        if (position.x > GraphicsYio.width) return false;
        if (position.x + position.width < 0) return false;
        return true;
    }


    boolean areAllDaysTaggedAsCompleted() {
        for (CveDayButton cveDayButton : dayButtons) {
            if (cveDayButton.state == CveDayState.completed) continue;
            return false;
        }
        return true;
    }


    public void setMonth(CveMonth month) {
        this.month = month;
        updateTitleString();
        initDayButtons();
    }


    CveDayButton getCurrentlyTouchedButton(PointYio touchPoint) {
        if (!isCurrentlyVisible()) return null;
        for (CveDayButton dayButton : dayButtons) {
            if (!dayButton.isTouchedBy(touchPoint)) continue;
            return dayButton;
        }
        return null;
    }


    private void initDayButtons() {
        dayButtons.clear();
        int rowQuantity = 7;
        float delta = 0.05f * GraphicsYio.width;
        float rowWidth = GraphicsYio.width - 2 * delta;
        float r = (rowWidth / rowQuantity) / 2;
        int weeklyIndex = CalendarManager.getInstance().getWeeklyIndex(month.year, month.monthIndex, 1);
        float x = delta + r + 2 * r * weeklyIndex;
        float y = (float) (separatorY - vDelta - GraphicsYio.convertToHeight(r));
        for (int i = 1; i <= month.daysQuantity; i++) {
            CveDayButton cveDayButton = new CveDayButton(this);
            cveDayButton.setIndex(i);
            cveDayButton.position.radius = r;
            cveDayButton.delta.set(x, y);
            dayButtons.add(cveDayButton);
            x += 2 * r;
            if (x > GraphicsYio.width - delta) {
                x = delta + r;
                y -= 2 * r;
            }
        }
        moveDayButtons();
    }


    private void updateTitleString() {
        String name = LanguagesManager.getInstance().getString(month.nameKey);
        title.setString(Yio.getCapitalizedString(name) + " " + month.year);
        if (fullYearCompletedMode) {
            title.setString("" + month.year);
        }
        title.updateMetrics();
    }


    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


    public void setFullYearCompletedMode(boolean fullYearCompletedMode) {
        this.fullYearCompletedMode = fullYearCompletedMode;
        updateTitleString();
    }
}
