package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.calendar.CalendarViewElement;
import yio.tro.opacha.menu.elements.calendar.CveDayButton;
import yio.tro.opacha.menu.elements.calendar.CveTab;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.calendar.CveColorYio;

import java.util.HashMap;

public class RenderCalendarViewElement extends RenderInterfaceElement{


    private CalendarViewElement cvElement;
    HashMap<CveColorYio, TextureRegion> mapColors;
    private float alpha;
    private TextureRegion completedTexture;
    private TextureRegion lockedTexture;


    @Override
    public void loadTextures() {
        mapColors = new HashMap<>();
        for (CveColorYio cveColorYio : CveColorYio.values()) {
            mapColors.put(cveColorYio, GraphicsYio.loadTextureRegion("menu/calendar/" + cveColorYio + ".png", false));
        }
        completedTexture = GraphicsYio.loadTextureRegion("menu/calendar/completed.png", true);
        lockedTexture = GraphicsYio.loadTextureRegion("menu/calendar/calendar_locked.png", true);
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        cvElement = (CalendarViewElement) element;
        alpha = cvElement.getAlpha();

        for (CveTab tab : cvElement.tabsList) {
            if (!tab.isCurrentlyVisible()) continue;
            renderTab(tab);
        }
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderTab(CveTab tab) {
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangle(batch, mapColors.get(tab.month.color), tab.topArea);
        GraphicsYio.renderTextOptimized(batch, blackPixel, tab.title, alpha);
        if (tab.completed) return;
        for (CveDayButton dayButton : tab.dayButtons) {
            renderSingleDayButton(dayButton);
        }
    }


    private void renderSingleDayButton(CveDayButton dayButton) {
        switch (dayButton.state) {
            default:
                System.out.println("RenderCalendarViewElement.renderSingleDayButton");
                break;
            case locked:
                GraphicsYio.setBatchAlpha(batch, alpha);
                GraphicsYio.drawByCircle(batch, lockedTexture, dayButton.position);
                break;
            case unlocked:
                GraphicsYio.renderTextOptimized(batch, blackPixel, dayButton.title, alpha);
                break;
            case completed:
                GraphicsYio.setBatchAlpha(batch, alpha);
                GraphicsYio.drawByCircle(batch, completedTexture, dayButton.position);
                break;
        }
        if (dayButton.selectionEngineYio.isSelected()) {
            GraphicsYio.setBatchAlpha(batch, dayButton.selectionEngineYio.getAlpha());
            GraphicsYio.drawByCircle(batch, blackPixel, dayButton.position);
        }
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
