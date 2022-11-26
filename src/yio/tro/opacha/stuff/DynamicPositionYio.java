package yio.tro.opacha.stuff;

import yio.tro.opacha.Yio;
import yio.tro.opacha.stuff.factor_yio.FactorYio;
import yio.tro.opacha.stuff.object_pool.ReusableYio;

public class DynamicPositionYio implements ReusableYio {

    public CircleYio viewPosition;
    CircleYio position, previousPosition;
    public FactorYio relocationFactor;


    public DynamicPositionYio() {
        viewPosition = new CircleYio();
        position = new CircleYio();
        previousPosition = new CircleYio();
        relocationFactor = new FactorYio();
    }


    @Override
    public void reset() {
        viewPosition.reset();
        position.reset();
        previousPosition.reset();
        relocationFactor.reset();
    }


    public void move() {
        if (relocationFactor.move()) {
            updateViewPosition();
        }
    }


    public void set(double x, double y) {
        set(x, y, position.radius);
    }


    public void forceViewPosition() {
        viewPosition.setBy(position);
    }


    public void setUpSpawn(double x, double y, double r) {
        previousPosition.set(x, y, 0);
        viewPosition.set(x, y, 0);
        position.set(x, y, r);
        launchFactor();
    }


    public void set(double x, double y, double r) {
        if (isQuickCorrectionPossible(x, y, r)) {
            position.set(x, y, r);
            return;
        }

        previousPosition.setBy(viewPosition);
        position.set(x, y, r);
        launchFactor();
    }


    private boolean isQuickCorrectionPossible(double x, double y, double r) {
        if (Math.abs(position.radius - r) >= 1) return false;
        if (Yio.distance(x, y, position.center.x, position.center.y) >= position.radius) return false;
        if (relocationFactor.get() > 0.9) return false;
        if (relocationFactor.get() < 0.05) return false;
        if (position.center.distanceTo(previousPosition.center) < position.radius) return false;

        return true;
    }


    public boolean isInDestroyState() {
        return previousPosition.radius > position.radius && position.radius < 1;
    }


    private void launchFactor() {
        relocationFactor.reset();
        relocationFactor.appear(6, 1);
    }


    private void updateViewPosition() {
        viewPosition.center.x = previousPosition.center.x + relocationFactor.get() * (position.center.x - previousPosition.center.x);
        viewPosition.center.y = previousPosition.center.y + relocationFactor.get() * (position.center.y - previousPosition.center.y);
        viewPosition.radius = previousPosition.radius + relocationFactor.get() * (position.radius - previousPosition.radius);
    }

}
