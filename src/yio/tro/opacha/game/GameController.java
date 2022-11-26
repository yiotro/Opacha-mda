package yio.tro.opacha.game;

import com.badlogic.gdx.Gdx;
import yio.tro.opacha.OneTimeInfo;
import yio.tro.opacha.SettingsManager;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.editor.EditorSavesManager;
import yio.tro.opacha.game.export_import.ImportManager;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.game.touch_modes.TouchMode;
import yio.tro.opacha.game.tutorial.ScriptManager;
import yio.tro.opacha.menu.ClickDetector;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;

import java.util.ArrayList;

public class GameController {

    public YioGdxGame yioGdxGame;
    public int w, h;
    public int currentTouchCount;
    long currentTime;
    public PointYio touchDownPos, currentTouch;
    public DebugActionsController debugActionsController;
    public GameMode gameMode;
    public ObjectsLayer objectsLayer;
    ClickDetector clickDetector;
    GameResults gameResults;
    public static int currentMoveIndex;
    public LevelSize initialLevelSize;
    public boolean backgroundVisible;
    public float boundWidth, boundHeight;
    public CameraController cameraController;
    public PointYio currentTouchConverted;
    public TouchMode touchMode;
    public SpeedManager speedManager;
    public ArrayList<TouchMode> dyingTms;
    public ImportManager importManager;
    public EditorSavesManager editorSavesManager;
    public ScriptManager scriptManager;


    public GameController(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        touchDownPos = new PointYio();
        currentTouch = new PointYio();
        cameraController = new CameraController(this);
        debugActionsController = new DebugActionsController(this);
        clickDetector = new ClickDetector();
        gameMode = null;
        gameResults = new GameResults();
        currentTouchConverted = new PointYio();
        speedManager = new SpeedManager(this);
        currentMoveIndex = 0;
        dyingTms = new ArrayList<>();
        importManager = new ImportManager(this);
        editorSavesManager = new EditorSavesManager(this);
        scriptManager = new ScriptManager(this);

        TouchMode.createModes(this);
        touchMode = null;
    }


    public void move() {
        currentTime = System.currentTimeMillis();
        currentMoveIndex++;

        cameraController.move();
        moveTouchMode();
        objectsLayer.move();
        scriptManager.moveActually();
        scriptManager.moveVisually();
    }


    private void moveTouchMode() {
        if (touchMode != null) {
            touchMode.move();
        }

        for (int i = dyingTms.size() - 1; i >= 0; i--) {
            TouchMode dtm = dyingTms.get(i);
            dtm.move();
            if (dtm.isReadyToBeRemoved()) {
                dyingTms.remove(dtm);
            }
        }
    }


    public void defaultValues() {
        GameRules.defaultValues();
        cameraController.defaultValues();
        currentTouchCount = 0;
        touchDownPos.set(0, 0);
        speedManager.defaultValues();
        scriptManager.defaultValues();
    }


    public void createMenuOverlay() {
        Scenes.gameOverlay.create();

        if (gameMode == GameMode.editor) {
            Scenes.editorOverlay.create();
        } else {
            doShowMechanicsOverlay();
        }

        if (objectsLayer.spectateManager.active) {
            Scenes.endSpectate.create();
        }

        checkToShowQuickTutorial();
    }


    private void doShowMechanicsOverlay() {
        Scenes.speedControls.create();
        if (SettingsManager.getInstance().velocitySliderEnabled) {
            Scenes.velocityControls.create();
        }
        if (isLockButtonEnabled()) {
            Scenes.lockCamera.create();
        }
    }


    private boolean isLockButtonEnabled() {
        return !GameRules.aiOnlyMode && SettingsManager.getInstance().lockButtonEnabled && gameMode != GameMode.tutorial;
    }


    private void checkToShowQuickTutorial() {
        OneTimeInfo instance = OneTimeInfo.getInstance();
        if (instance.quickVelocityTutorial) return;
        if (!SettingsManager.getInstance().velocitySliderEnabled) return;
        if (gameMode == GameMode.tutorial) return;

        instance.quickVelocityTutorial = true;
        instance.save();

        Scenes.quickVelocityTutorial.create();
    }


    public void createPauseMenu() {
        if (gameMode == GameMode.editor) {
            Scenes.editorPauseMenu.create();
            return;
        }

        Scenes.pauseMenu.create();
    }


    public void createCamera() {
        yioGdxGame.gameView.createOrthoCam();
        cameraController.createCamera();
        yioGdxGame.gameView.updateCam();
    }


    public void createGameObjects() {
        if (objectsLayer != null) {
            objectsLayer.onDestroy();
        }

        objectsLayer = new ObjectsLayer(this);
    }


    public void debugActions() {
        debugActionsController.updateReferences();
        debugActionsController.debugActions();
    }


    public YioGdxGame getYioGdxGame() {
        return yioGdxGame;
    }


    public void touchDown(int screenX, int screenY, int pointer, int button) {
        currentTouchCount++;
        updateTouchPoints(screenX, screenY);
        touchDownPos.setBy(currentTouch);
        clickDetector.onTouchDown(currentTouch);

        if (objectsLayer.onTouchDown(currentTouch)) return;
        touchMode.touchDownReaction();
    }


    boolean touchedAsClick() {
        return clickDetector.isClicked();
    }


    public void updateTouchPoints(int screenX, int screenY) {
        currentTouch.x = screenX;
        currentTouch.y = screenY;
        updateCurrentTouchConverted();
    }


    public void updateCurrentTouchConverted() {
        currentTouchConverted.x = (currentTouch.x - 0.5f * w) * cameraController.orthoCam.zoom + cameraController.orthoCam.position.x;
        currentTouchConverted.y = (currentTouch.y - 0.5f * h) * cameraController.orthoCam.zoom + cameraController.orthoCam.position.y;
    }


    public void touchUp(int screenX, int screenY, int pointer, int button) {
        currentTouchCount--;
        if (currentTouchCount < 0) {
            currentTouchCount = 0;
            return;
        }

        updateTouchPoints(screenX, screenY);
        clickDetector.onTouchUp(currentTouch);
        if (objectsLayer.onTouchUp(currentTouch)) return;
        checkForClick();
        touchMode.touchUpReaction();
    }


    private void checkForClick() {
        if (currentTouchCount != 0) return;
        if (!touchedAsClick()) return;

        onClick();
    }


    public void onMouseWheelScrolled(int amount) {
        if (touchMode != null) {
            if (touchMode.onMouseWheelScrolled(amount)) {
                return; // touch mode can catch mouse wheel scroll
            }
        }

        cameraController.onMouseWheelScrolled(amount);
    }


    public void setTouchMode(TouchMode touchMode) {
        if (this.touchMode == touchMode) return;

        if (this.touchMode != null) {
            onTmEnd();
        }

        this.touchMode = touchMode;
        touchMode.onModeBegin();
        Scenes.gameOverlay.onTouchModeSet(touchMode);

        if (dyingTms.contains(touchMode)) {
            dyingTms.remove(touchMode);
        }
    }


    private void onTmEnd() {
        touchMode.kill();
        touchMode.onModeEnd();

        if (!dyingTms.contains(touchMode)) {
            dyingTms.add(touchMode);
        }
    }


    public void resetTouchMode() {
        if (gameMode == GameMode.editor) {
            setTouchMode(TouchMode.tmEditor);
            return;
        }

        setTouchMode(TouchMode.tmDefault);
    }


    public void initLevelSize(LevelSize levelSize) {
        initialLevelSize = levelSize;
        switch (levelSize) {
            case tiny:
                setBounds(1);
                break;
            case small:
                setBounds(1.5);
                break;
            case normal:
                setBounds(2);
                break;
            case big:
                setBounds(3);
                break;
            case giant:
                setBounds(4);
                break;
        }
        onLevelSizeChanged();
    }


    private void onLevelSizeChanged() {

    }


    void setBounds(double multiplier) {
        this.boundWidth = (float) (multiplier * GraphicsYio.width);
        boundHeight = 1.6f * boundWidth;
        cameraController.setBounds(boundWidth, boundHeight);
    }


    private void onClick() {
        if (touchMode.onClick()) return;
        objectsLayer.onClick();
    }


    public void touchDragged(int screenX, int screenY, int pointer) {
        updateTouchPoints(screenX, screenY);
        clickDetector.onTouchDrag(currentTouch);
        if (objectsLayer.onTouchDrag(currentTouch)) return;
        touchMode.touchDragReaction();
    }


    public void onEndCreation() {
        resetTouchMode();
        cameraController.onEndCreation();
        objectsLayer.onEndCreation();
        scriptManager.onEndCreation();
    }


    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        onGameModeChanged();
    }


    private void onGameModeChanged() {
        //
    }


    public void onEscapedToPauseMenu() {
        resetTouchMode();
        objectsLayer.planetsManager.deselect();
    }


    public void onPause() {

    }


    public void onResume() {
        currentTouchCount = 0;
    }


    public void setBackgroundVisible(boolean backgroundVisible) {
        this.backgroundVisible = backgroundVisible;
    }


    public float getTrackerZoom() {
        return cameraController.viewZoomLevel;
    }

}
