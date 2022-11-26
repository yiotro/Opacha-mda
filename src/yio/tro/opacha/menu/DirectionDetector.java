package yio.tro.opacha.menu;

import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;

public class DirectionDetector {

    PointYio touchDownPoint;
    float detectionRadius;
    boolean detected, directionVertical;


    public DirectionDetector() {
        touchDownPoint = new PointYio();
        detectionRadius = 0.02f * GraphicsYio.width;

        reset();
    }


    private void reset() {
        detected = false;
        directionVertical = false;
    }


    public void touchDown(PointYio touchPoint) {
        touchDownPoint.setBy(touchPoint);
        reset();
    }


    public void touchDrag(PointYio touchPoint) {
        if (detected) return;

        if (Math.abs(touchPoint.y - touchDownPoint.y) > detectionRadius) {
            detected = true;
            directionVertical = true;

            return;
        }

        if (Math.abs(touchPoint.x - touchDownPoint.x) > detectionRadius) {
            detected = true;
            directionVertical = false;

            return;
        }
    }


    public boolean isDetected() {
        return detected;
    }


    public boolean isDirectionVertical() {
        return directionVertical;
    }


    public void setDetectionRadius(float detectionRadius) {
        this.detectionRadius = detectionRadius;
    }
}
