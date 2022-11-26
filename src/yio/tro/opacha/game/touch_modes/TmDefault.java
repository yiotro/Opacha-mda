package yio.tro.opacha.game.touch_modes;

import yio.tro.opacha.Yio;
import yio.tro.opacha.game.GameController;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.game.gameplay.model.Planet;
import yio.tro.opacha.game.gameplay.model.PlanetsManager;
import yio.tro.opacha.game.view.game_renders.GameRender;
import yio.tro.opacha.game.view.game_renders.GameRendersList;
import yio.tro.opacha.menu.two_finger_touch.TwoFingerListener;
import yio.tro.opacha.menu.two_finger_touch.TwoFingerTouchManager;
import yio.tro.opacha.stuff.LongTapDetector;
import yio.tro.opacha.stuff.factor_yio.FactorYio;

public class TmDefault extends TouchMode implements TwoFingerListener{

    LongTapDetector longTapDetector;
    public FactorYio iconAnimFactor;
    boolean cameraBlocked;
    Planet touchDownPlanet;
    long touchDownTime;
    TwoFingerTouchManager twoFingerTouchManager;
    boolean twoFingerDetected;


    public TmDefault(GameController gameController) {
        super(gameController);

        iconAnimFactor = new FactorYio();
        twoFingerTouchManager = new TwoFingerTouchManager();

        initLongTapDetector();
    }


    private void initLongTapDetector() {
        longTapDetector = new LongTapDetector() {
            @Override
            public void onLongTapDetected() {
                TmDefault.this.onLongTapDetected();
            }
        };
    }


    @Override
    public void onModeBegin() {
        iconAnimFactor.reset();
        cameraBlocked = false;
        touchDownPlanet = null;
        twoFingerDetected = false;
    }


    @Override
    public void onModeEnd() {

    }


    @Override
    public void move() {
        longTapDetector.move();
        iconAnimFactor.move();
        twoFingerTouchManager.move();
    }


    @Override
    public boolean isCameraMovementEnabled() {
        return !cameraBlocked && gameController.objectsLayer.spectateManager.cameraMovementAllowed;
    }


    @Override
    public void onTouchDown() {
        if (GameRules.aiOnlyMode) return;
        if (gameController.objectsLayer.spectateManager.active) return;

        twoFingerDetected = false;
        longTapDetector.onTouchDown(gameController.currentTouch);
        twoFingerTouchManager.onTouchDown(gameController.currentTouch);

        cameraBlocked = false;
        checkToBlockCamera();
        checkToHighlightPlanet();
    }


    private void checkToHighlightPlanet() {
        Planet touchedPlanet = getPlanetsManager().findTouchedPlanet(gameController.currentTouchConverted);
        if (touchedPlanet == null) return;

        touchedPlanet.setHoldHighlight(true);
        touchedPlanet.highlightEngine.select();
    }


    private void checkToBlockCamera() {
        Planet touchedPlanet = getPlanetsManager().findTouchedPlanet(gameController.currentTouchConverted);
        if (touchedPlanet == null) return;
        if (!touchedPlanet.isControlledByPlayer()) return;

        cameraBlocked = true;
        touchDownPlanet = touchedPlanet;
        touchDownTime = System.currentTimeMillis();
    }


    private PlanetsManager getPlanetsManager() {
        return gameController.objectsLayer.planetsManager;
    }


    @Override
    public void onTouchDrag() {
        if (GameRules.aiOnlyMode) return;
        longTapDetector.onTouchDrag(gameController.currentTouch);
        twoFingerTouchManager.onTouchDrag(gameController.currentTouch);
    }


    @Override
    public void onTouchUp() {
        if (GameRules.aiOnlyMode) return;
        longTapDetector.onTouchUp(gameController.currentTouch);
        twoFingerTouchManager.onTouchUp(gameController.currentTouch);
        checkToUnblockCamera();
        getPlanetsManager().releaseHighlights();
    }


    private void checkToUnblockCamera() {
        if (twoFingerDetected) return;
        if (!cameraBlocked) return;
        cameraBlocked = false;

        if (System.currentTimeMillis() - touchDownTime > 900) return;
        float distance = touchDownPlanet.viewPosition.center.distanceTo(gameController.currentTouchConverted);
        if (distance < 2 * touchDownPlanet.position.radius) return;

        double targetAngle = touchDownPlanet.viewPosition.center.angleTo(gameController.currentTouchConverted);
        Planet targetPlanet = getPlanetsManager().findAdjoinedPlanetClosestToAngle(touchDownPlanet, targetAngle);
        if (targetPlanet == null) return;

        double a = touchDownPlanet.viewPosition.center.angleTo(targetPlanet.viewPosition.center);
        if (Yio.distanceBetweenAngles(a, targetAngle) > Math.PI / 4) return;

        touchDownPlanet.setAutoTarget(targetPlanet);
        touchDownPlanet.prepareToSendAutoTargetQuickly();
    }


    @Override
    public boolean onClick() {
        if (GameRules.aiOnlyMode) return false;
        if (gameController.objectsLayer.spectateManager.active) return false;
        ObjectsLayer objectsLayer = gameController.objectsLayer;
        objectsLayer.planetsManager.onClick(gameController.currentTouchConverted);
        return true;
    }


    private void onLongTapDetected() {
        if (GameRules.aiOnlyMode) return;

        Planet touchedPlanet = getPlanetsManager().findTouchedPlanet(gameController.currentTouchConverted);
        if (touchedPlanet == null) return;
        if (!touchedPlanet.isControlledByPlayer()) return;

        cameraBlocked = false; // no auto target after long tap
        ObjectsLayer objectsLayer = gameController.objectsLayer;
        objectsLayer.planetsManager.onLongTappedPlanet(touchedPlanet);
    }


    @Override
    public GameRender getRender() {
        return GameRendersList.getInstance().renderTmDefault;
    }


    @Override
    public String getNameKey() {
        return null;
    }


    @Override
    public void onReachedTwoFingerState() {
        twoFingerDetected = true;
        cameraBlocked = true;
    }


    @Override
    public void onExitedTwoFingerState() {

    }


    @Override
    public void onSecondFingerDragged() {

    }


    @Override
    public void onTwoFingerRotated(double deltaAngle) {

    }
}
