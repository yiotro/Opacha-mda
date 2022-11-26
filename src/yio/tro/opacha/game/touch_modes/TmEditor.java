package yio.tro.opacha.game.touch_modes;

import yio.tro.opacha.game.GameController;
import yio.tro.opacha.game.gameplay.EditorWorker;
import yio.tro.opacha.game.gameplay.model.Planet;
import yio.tro.opacha.game.view.game_renders.GameRender;
import yio.tro.opacha.game.view.game_renders.GameRendersList;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.CircleYio;
import yio.tro.opacha.stuff.LongTapDetector;

public class TmEditor extends TouchMode{

    public Planet selectedPlanet;
    public CircleYio selectionPosition;
    LongTapDetector longTapDetector;


    public TmEditor(GameController gameController) {
        super(gameController);
        selectionPosition = new CircleYio();
        initLongTapDetector();
    }


    private void initLongTapDetector() {
        longTapDetector = new LongTapDetector() {
            @Override
            public void onLongTapDetected() {
                TmEditor.this.onLongTapDetected();
            }
        };
    }


    private void onLongTapDetected() {
        updateSelectedPlanet();
    }


    private void updateSelectedPlanet() {
        selectedPlanet = null;
        Planet closestPlanet = getClosestPlanet();
        if (closestPlanet == null) return;

        float distance = closestPlanet.position.center.distanceTo(gameController.currentTouchConverted);
        if (distance > closestPlanet.position.radius) return;

        selectedPlanet = closestPlanet;
        updateSelectionPosition();
    }


    private void updateSelectionPosition() {
        selectionPosition.setBy(selectedPlanet.position);
        selectionPosition.radius *= 2;
    }


    private Planet getClosestPlanet() {
        return getEditorWorker().findClosestPlanet(gameController.currentTouchConverted);
    }


    private EditorWorker getEditorWorker() {
        return gameController.objectsLayer.editorWorker;
    }


    @Override
    public void onModeBegin() {
        resetSelection();
    }


    private void resetSelection() {
        selectedPlanet = null;
    }


    @Override
    public void onModeEnd() {

    }


    @Override
    public void move() {
        longTapDetector.move();
    }


    @Override
    public boolean isCameraMovementEnabled() {
        return selectedPlanet == null;
    }


    @Override
    public void onTouchDown() {
        resetSelection();
        longTapDetector.onTouchDown(gameController.currentTouchConverted);
    }


    @Override
    public void onTouchDrag() {
        longTapDetector.onTouchDrag(gameController.currentTouchConverted);
        checkToRelocateSelectedPlanet();
    }


    private void checkToRelocateSelectedPlanet() {
        if (selectedPlanet == null) return;
        getEditorWorker().onPlanetRelocationRequested(selectedPlanet, gameController.currentTouchConverted);
        updateSelectionPosition();
    }


    @Override
    public void onTouchUp() {
        longTapDetector.onTouchUp(gameController.currentTouchConverted);
        resetSelection();
    }


    @Override
    public boolean onClick() {
        checkToSelectPlanet();
        return true;
    }


    private void checkToSelectPlanet() {
        Planet closestPlanet = getClosestPlanet();
        if (closestPlanet == null) return;

        float distance = closestPlanet.position.center.distanceTo(gameController.currentTouchConverted);
        if (distance > 1.5 * closestPlanet.position.radius) return;

        Scenes.editSinglePlanet.create();
        Scenes.editSinglePlanet.setSelectedPlanet(closestPlanet);
        closestPlanet.select();
    }


    @Override
    public String getNameKey() {
        return null;
    }


    @Override
    public GameRender getRender() {
        return GameRendersList.getInstance().renderTmEditor;
    }
}
