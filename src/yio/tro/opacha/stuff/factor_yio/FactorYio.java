package yio.tro.opacha.stuff.factor_yio;

import yio.tro.opacha.RefreshRateDetector;
import yio.tro.opacha.Yio;

public class FactorYio {

    boolean inAppearState;
    boolean inDestroyState;
    double value;
    double gravity;
    double dy;
    double speedMultiplier;
    AbstractMoveBehavior behavior;
    boolean needsToMove;


    public FactorYio() {
        behavior = null;
        inAppearState = false;
        inDestroyState = false;
    }


    public boolean move() {
        if (!needsToMove) return false;
        behavior.move(this);
        behavior.updateNeedsToMoveValue(this);
        return true;
    }


    public void appear(int movementIndex, double speed) {
        // this method only exists to avoid changing legacy code
        switch (movementIndex) {
            default:
            case 0:
                appear(MovementType.simple, speed);
                break;
            case 1:
                appear(MovementType.lighty, speed + 1);
                break;
            case 2:
                appear(MovementType.material, 1.6 * speed);
                break;
            case 3:
                appear(MovementType.approach, Math.max(0.75, 2.5 * speed));
                break;
            case 6:
                appear(MovementType.inertia, speed);
                break;
            case 7:
                System.out.println("FactorYio.appear: should use jump engine instead");
                break;
        }
    }


    public void destroy(int movementIndex, double speed) {
        // this method only exists to avoid changing legacy code
        switch (movementIndex) {
            default:
            case 0:
                destroy(MovementType.simple, speed);
                break;
            case 1:
                destroy(MovementType.lighty, speed + 1);
                break;
            case 2:
                destroy(MovementType.material, 1.6 * speed);
                break;
            case 3:
                destroy(MovementType.approach, Math.max(0.75, speed + 0.1 * (1.5 - speed)));
                break;
            case 6:
                destroy(MovementType.inertia, speed);
                break;
        }
    }


    public boolean hasToMove() {
        // this method only exists to avoid changing legacy code
        return needsToMove;
    }


    public float getGravity() {
        // this method only exists to avoid changing legacy code
        if (inAppearState) {
            return 1;
        }
        if (inDestroyState) {
            return -1;
        }
        return 0;
    }


    public void appear(MovementType movementType, double speed) {
        setBehavior(movementType);
        prepareForMovement(speed);
        gravity = 0.01;
        inAppearState = true;
        behavior.onAppear(this);
    }


    public void destroy(MovementType movementType, double speed) {
        setBehavior(movementType);
        prepareForMovement(speed);
        gravity = -0.01;
        inDestroyState = true;
        behavior.onDestroy(this);
    }


    private void prepareForMovement(double speed) {
        setDy(0);
        speedMultiplier = 0.3 * speed * RefreshRateDetector.getInstance().multiplier;
        inDestroyState = false;
        inAppearState = false;
        needsToMove = true;
    }


    private void setBehavior(MovementType movementType) {
        behavior = MbFactoryYio.getInstance().getBehavior(movementType);
    }


    public void setValues(double f, double dy) {
        this.value = f;
        this.dy = dy;
    }


    public void setDy(double dy) {
        this.dy = dy;
    }


    public void stop() {
        appear(MovementType.stay, 1);
        needsToMove = false;
        inAppearState = false;
        inDestroyState = false;
    }


    public float getValue() {
        return (float) value;
    }


    public float get() {
        // would be better to fully replace this method with getValue()
        // but it's used 300+ times, so I'll just leave it like this...
        return (float) value;
    }


    public void setValue(double value) {
        this.value = value;
    }


    public boolean isInAppearState() {
        return inAppearState;
    }


    public boolean isInDestroyState() {
        return inDestroyState;
    }



    public void reset() {
        setValues(0, 0);
        stop();
    }


    @Override
    public String toString() {
        return "Factor: " +
                "state(" + inAppearState + ", " + inDestroyState + "), " +
                "value(" + Yio.roundUp(value, 2) + "), " +
                "dy(" + Yio.roundUp(dy, 2) + "), " +
                "gravity(" + Yio.roundUp(gravity, 2) + ")";
    }
}