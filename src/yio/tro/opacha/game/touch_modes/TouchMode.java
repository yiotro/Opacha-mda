package yio.tro.opacha.game.touch_modes;

import yio.tro.opacha.game.GameController;
import yio.tro.opacha.game.view.game_renders.GameRender;

import java.util.ArrayList;

public abstract class TouchMode {

    protected GameController gameController;
    public static ArrayList<TouchMode> touchModes;
    protected boolean alive;

    public static TmDefault tmDefault;
    public static TmMatchEnded tmMatchEnded;
    public static TmEditor tmEditor;
    public static TmAddition tmAddition;
    public static TmDeletion tmDeletion;
    // don't forget to initialize touch mode lower


    public TouchMode(GameController gameController) {
        this.gameController = gameController;
        alive = true;

        touchModes.add(this);
    }


    public static void createModes(GameController gameController) {
        touchModes = new ArrayList<>();

        tmDefault = new TmDefault(gameController);
        tmMatchEnded = new TmMatchEnded(gameController);
        tmEditor = new TmEditor(gameController);
        tmAddition = new TmAddition(gameController);
        tmDeletion = new TmDeletion(gameController);
    }


    public abstract void onModeBegin();


    public abstract void onModeEnd();


    public abstract void move();


    public abstract boolean isCameraMovementEnabled();


    public boolean isDoubleClickDisabled() {
        return true;
    }


    public void touchDownReaction() {
        if (isCameraMovementEnabled()) {
            gameController.cameraController.onTouchDown(gameController.currentTouch);
        }

        onTouchDown();
    }


    public abstract void onTouchDown();


    public void touchDragReaction() {
        if (isCameraMovementEnabled()) {
            gameController.cameraController.onTouchDrag(gameController.currentTouch);
        }

        onTouchDrag();
    }


    public abstract void onTouchDrag();


    public void touchUpReaction() {
        if (isCameraMovementEnabled()) {
            gameController.cameraController.onTouchUp(gameController.currentTouch);
        }

        onTouchUp();
    }


    public abstract void onTouchUp();


    public abstract boolean onClick();


    public abstract String getNameKey();


    public boolean onMouseWheelScrolled(int amount) {
        return false;
    }


    public void resetTouchMode() {
        gameController.resetTouchMode();
    }


    public GameRender getRender() {
        return null;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName();
    }


    public boolean isReadyToBeRemoved() {
        return !alive;
    }


    public boolean isAlive() {
        return alive;
    }


    public void kill() {
        setAlive(false);
    }


    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
