package yio.tro.opacha.game;

import yio.tro.opacha.Yio;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.game.gameplay.model.MatchResults;
import yio.tro.opacha.game.gameplay.model.Planet;
import yio.tro.opacha.game.gameplay.model.PlanetsManager;
import yio.tro.opacha.game.loading.LoadingParameters;
import yio.tro.opacha.game.loading.LoadingType;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.GradualAttractionManager;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.calendar.CalendarManager;
import yio.tro.opacha.stuff.calendar.CveMonth;

public class DebugActionsController {

    GameController gameController;
    private final YioGdxGame yioGdxGame;
    private final MenuControllerYio menuControllerYio;
    PointYio tempPoint;


    public DebugActionsController(GameController gameController) {
        this.gameController = gameController;
        yioGdxGame = gameController.yioGdxGame;
        menuControllerYio = yioGdxGame.menuControllerYio;
        tempPoint = new PointYio();
    }


    public void debugActions() {
        Scenes.attraction.create();
    }


    private void doShowCalendarColors() {
        System.out.println();
        System.out.println("DebugActionsController.doShowCalendarColors");
        for (int year = 2021; year <= 2030; year++) {
            System.out.println("--- " + year + " ---");
            for (int month = 1; month <= 12; month++) {
                CveMonth cveMonth = new CveMonth();
                cveMonth.setValues(year, month);
                System.out.println(month + ": " + cveMonth.color);
            }
        }
    }


    private void doOpenCalendar() {
        Scenes.calendar.create();
    }


    private void doTagMonthAsCompleted(int year, int month) {
        MatchResults matchResults = new MatchResults();
        matchResults.year = year;
        matchResults.month = month;
        CalendarManager calendarManager = CalendarManager.getInstance();
        int daysQuantity = calendarManager.getDaysQuantity(year, month);
        for (int i = 1; i <= daysQuantity; i++) {
            matchResults.day = i;
            calendarManager.onCalendarDayCompleted(matchResults);
        }
    }


    private void doCheckAttractionsPerDay() {
        int attractionsPerDay = GradualAttractionManager.getInstance().calculateAttractionsPerDay(60000);
        System.out.println("attractionsPerDay = " + attractionsPerDay);
    }


    private void doShowCameraController() {
        CameraController cameraController = yioGdxGame.gameController.cameraController;
        System.out.println("cameraController = " + cameraController);
    }


    private void doCheckGlobalMessage() {
        Scenes.globalMessage.create();
        Scenes.globalMessage.globalMessageElement.setText("d hsjahdjas hjd hjah djashjsah djashjdash djhsajd hasj dhasj dhasjd hasj hsaj hasj hjashd adas");
    }


    private void doCheckForefinger() {
        PlanetsManager planetsManager = getObjectsLayer().planetsManager;
        Planet planetNearFrameCenter = planetsManager.getPlanetNearFrameCenter();
        Scenes.forefinger.create();
        Scenes.forefinger.forefingerElement.setTarget(
                planetNearFrameCenter, true
        );
    }


    private void doChangeVelocity() {
        Scenes.keyboard.create();
        Scenes.keyboard.setValue("");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                double velocity = Double.valueOf(input);
                VelocityManager.getInstance().setValue(velocity);
            }
        });
    }


    private void doTestDifficultyAnalyzer() {
        if (!gameController.yioGdxGame.gamePaused) {
            gameController.objectsLayer.impossibruDetector.doShowResultsInConsole();
            return;
        }
        doLaunchCampaignLevel();
    }


    private void doLaunchCampaignLevel() {
        Scenes.keyboard.create();
        Scenes.keyboard.setValue("");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                doLaunchSpecificLevel(input);
            }
        });
    }


    private void doLaunchSpecificLevel(String input) {
        int levelIndex = Integer.valueOf(input);
        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.addParameter("index", levelIndex);
        yioGdxGame.loadingManager.startInstantly(LoadingType.campaign_create, loadingParameters);
    }


    private void doCheckImpossibruDetector() {
        boolean situationImpossibru = gameController.objectsLayer.impossibruDetector.isSituationImpossibru();
        System.out.println("situationImpossibru = " + situationImpossibru);
    }


    private void doOpenReward() {
        Scenes.reward.create();
    }


    private void doGiveUnits() {
        for (Planet planet : getObjectsLayer().planetsManager.planets) {
            if (!planet.isControlledByPlayer()) continue;
            planet.setUnitsInside(planet.unitsInside + 250);
        }
    }


    private ObjectsLayer getObjectsLayer() {
        return gameController.objectsLayer;
    }


    private void doShowVisibleInterfaceElements() {
        menuControllerYio.showVisibleElementsInConsole();
    }


    public void updateReferences() {
        // no refs currently
    }


}
