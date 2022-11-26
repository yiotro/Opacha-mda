package yio.tro.opacha.game;

import yio.tro.opacha.menu.scenes.Scenes;

public class SpeedManager {

    GameController gameController;
    int speed;
    boolean normal;


    public SpeedManager(GameController gameController) {
        this.gameController = gameController;
    }


    public void setSpeed(int speed) {
        this.speed = speed;
        normal = (speed == 1);
    }


    public void defaultValues() {
        resetSpeed();
    }


    public void setPlayState(boolean state) {
        if (state) {
            resetSpeed();
        } else {
            applyTacticalPause();
        }
    }


    private void applyTacticalPause() {
        setSpeed(0);
    }


    private void resetSpeed() {
        setSpeed(1);
    }


    public void onPlayPauseButtonPressed() {
        if (speed == 0) {
            resetSpeed();
        } else {
            applyTacticalPause();
        }

        notifyUiAboutSpeedChange();
    }


    public boolean isSpeedNormal() {
        return normal;
    }


    public void onFastForwardButtonPressed() {
        if (speed == GameRules.fastForwardSpeed) {
            resetSpeed();
        } else {
            setSpeed(GameRules.fastForwardSpeed);
        }

        notifyUiAboutSpeedChange();
    }


    private void notifyUiAboutSpeedChange() {
        Scenes.speedControls.onSpeedStateChanged();
    }


    public int getSpeed() {
        return speed;
    }
}
