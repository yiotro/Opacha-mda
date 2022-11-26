package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.menu.elements.calendar.CalendarViewElement;
import yio.tro.opacha.menu.elements.ground.GpSrNone;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;

public class SceneCalendar extends SceneYio{


    private CalendarViewElement calendarViewElement;


    @Override
    protected void initialize() {
        getGround().setSpawnRule(new GpSrNone());
        getGround().setGroundIndex(GroundIndex.WHITE);
        createCalendarView();
        spawnBackButton(getBackReaction());
    }


    private void createCalendarView() {
        calendarViewElement = uiFactory.getCalendarViewElement()
                .setSize(1, 1);
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.chooseGameMode.create();
            }
        };
    }
}
