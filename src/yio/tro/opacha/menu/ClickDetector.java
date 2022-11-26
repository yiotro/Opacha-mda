package yio.tro.opacha.menu;

import yio.tro.opacha.stuff.TouchableYio;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;

public class ClickDetector implements TouchableYio{

    public static final int CLICK_DELAY = 250;

    PointYio touchDownPoint;
    float maxDistance, clickDistance, currentDistance;
    long touchDownTime;


    public ClickDetector() {
        clickDistance = 0.04f * GraphicsYio.width;

        touchDownPoint = new PointYio();
    }


    public boolean isClicked() {
        if (System.currentTimeMillis() - touchDownTime > CLICK_DELAY) return false;
        if (maxDistance > clickDistance) return false;

        return true;
    }


    @Override
    public boolean onTouchDown(PointYio touchPoint) {
        touchDownPoint.setBy(touchPoint);
        touchDownTime = System.currentTimeMillis();
        maxDistance = 0;

        return false;
    }


    @Override
    public boolean onTouchDrag(PointYio touchPoint) {
        checkToUpdateMaxDistance(touchPoint);

        return false;
    }


    @Override
    public boolean onTouchUp(PointYio touchPoint) {
        checkToUpdateMaxDistance(touchPoint);

        return false;
    }


    private void checkToUpdateMaxDistance(PointYio touchPoint) {
        currentDistance = touchDownPoint.distanceTo(touchPoint);

        if (currentDistance > maxDistance) {
            maxDistance = currentDistance;
        }
    }

}
