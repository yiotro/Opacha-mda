package yio.tro.opacha.stuff;

import yio.tro.opacha.RefreshRateDetector;

public class JumpEngineYio {

    float dx;
    float x;
    float value;
    float maxValue;
    float temp;
    boolean needsToMove;


    public JumpEngineYio() {

    }


    public void apply(double speed, double maxValue) {
        // old          -> new
        // (2.0, 0.22)  -> (1.0, 1.1)
        // (1.0, 0.1)   -> (1.0, 0.45)
        needsToMove = true;
        value = 0;
        x = 0;
        this.maxValue = (float) maxValue;
        dx = (float) (RefreshRateDetector.getInstance().multiplier * speed) / 20f;
    }


    public void move() {
        if (!needsToMove) return;
        x += dx;
        if (x >= 1) {
            x = 1;
            needsToMove = false;
        }
        temp = 2 * x - 1;
        value = 1 - (temp * temp);
        value *= maxValue;
    }


    public void reset() {
        needsToMove = false;
        value = 0;
    }


    public float getValue() {
        return value;
    }


    public boolean hasToMove() {
        return needsToMove;
    }
}
