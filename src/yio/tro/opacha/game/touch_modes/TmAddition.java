package yio.tro.opacha.game.touch_modes;

import yio.tro.opacha.game.GameController;
import yio.tro.opacha.game.gameplay.EditorWorker;
import yio.tro.opacha.game.gameplay.model.Planet;
import yio.tro.opacha.game.view.game_renders.GameRender;
import yio.tro.opacha.game.view.game_renders.GameRendersList;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.CircleYio;
import yio.tro.opacha.stuff.PointYio;

public class TmAddition extends TouchMode{

    public Planet touchDownPlanet;
    public CircleYio touchDownSelectionPosition;
    public PointYio currentTouchPoint;
    public Planet touchUpPlanet;
    public CircleYio touchUpSelectionPosition;


    public TmAddition(GameController gameController) {
        super(gameController);
        touchDownPlanet = null;
        touchDownSelectionPosition = new CircleYio();
        currentTouchPoint = new PointYio();
        touchUpPlanet = null;
        touchUpSelectionPosition = new CircleYio();
    }


    @Override
    public void onModeBegin() {
        Scenes.resetTouchMode.create();
    }


    @Override
    public void onModeEnd() {
        resetSelection();
    }


    @Override
    public void move() {

    }


    @Override
    public boolean isCameraMovementEnabled() {
        return false;
    }


    private void updateCurrentTouchPoint() {
        currentTouchPoint.setBy(gameController.currentTouchConverted);
    }


    @Override
    public void onTouchDown() {
        updateCurrentTouchPoint();
        updateTouchDownPlanet();
    }


    private void updateTouchDownPlanet() {
        touchDownPlanet = null;
        Planet closestPlanet = getClosestPlanet();
        if (closestPlanet == null) return;

        float distance = closestPlanet.position.center.distanceTo(currentTouchPoint);
        if (distance > 1.5 * closestPlanet.position.radius) return;

        touchDownPlanet = closestPlanet;
        touchDownSelectionPosition.setBy(touchDownPlanet.position);
        touchDownSelectionPosition.radius *= 2;
    }


    private Planet getClosestPlanet() {
        return getEditorWorker().findClosestPlanet(currentTouchPoint);
    }


    @Override
    public void onTouchDrag() {
        updateCurrentTouchPoint();
        updateTouchUpPlanet();
    }


    @Override
    public void onTouchUp() {
        updateCurrentTouchPoint();
        updateTouchUpPlanet();
        checkToUnite();

        resetSelection();
    }


    private void resetSelection() {
        touchDownPlanet = null;
        touchUpPlanet = null;
    }


    private void checkToUnite() {
        if (touchDownPlanet == null) return;
        if (touchUpPlanet == null) return;

        getEditorWorker().onLinkAdditionRequested(touchDownPlanet, touchUpPlanet);
    }


    private void updateTouchUpPlanet() {
        touchUpPlanet = null;
        Planet closestPlanet = getClosestPlanet();
        if (closestPlanet == null) return;
        if (closestPlanet == touchDownPlanet) return;

        float distance = closestPlanet.position.center.distanceTo(currentTouchPoint);
        if (distance > 1.5 * closestPlanet.position.radius) return;

        touchUpPlanet = closestPlanet;
        touchUpSelectionPosition.setBy(touchUpPlanet.position);
        touchUpSelectionPosition.radius *= 2;
    }


    @Override
    public boolean onClick() {
        getEditorWorker().onPlanetAdditionRequested(currentTouchPoint);
        return true;
    }


    private EditorWorker getEditorWorker() {
        return gameController.objectsLayer.editorWorker;
    }


    @Override
    public String getNameKey() {
        return "addition";
    }


    @Override
    public GameRender getRender() {
        return GameRendersList.getInstance().renderTmAddition;
    }
}
