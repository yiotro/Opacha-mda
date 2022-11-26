package yio.tro.opacha.game.gameplay.model;

import yio.tro.opacha.RefreshRateDetector;
import yio.tro.opacha.stuff.object_pool.ReusableYio;

public class AcceleratableFactor implements ReusableYio{

    public float value;
    float speed;
    boolean needsToMove;


    public AcceleratableFactor() {
        reset();
    }


    @Override
    public void reset() {
        value = 0;
        speed = 0;
        needsToMove = false;
    }


    public boolean move(float velocity) {
        if (!needsToMove) return false;
        value += speed * velocity * RefreshRateDetector.getInstance().multiplier;
        applyLimits();
        return true;
    }


    public void appear(double s) {
        value = 0;
        speed = (float) s;
        needsToMove = true;
    }


    public void destroy(double s) {
        value = 1;
        speed = (float) -s;
        needsToMove = true;
    }


    public boolean isInAppearState() {
        return speed > 0;
    }


    private void applyLimits() {
        if (value >= 1) {
            value = 1;
            needsToMove = false;
        }
        if (value <= 0) {
            value = 0;
            needsToMove = false;
        }
    }


    public boolean move() {
        return move(1);
    }
}
