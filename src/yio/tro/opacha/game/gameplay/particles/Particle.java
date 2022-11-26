package yio.tro.opacha.game.gameplay.particles;

import yio.tro.opacha.RefreshRateDetector;
import yio.tro.opacha.game.gameplay.AcceleratableYio;
import yio.tro.opacha.stuff.CircleYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.factor_yio.FactorYio;
import yio.tro.opacha.stuff.object_pool.ReusableYio;

public class Particle implements ReusableYio, AcceleratableYio{

    ParticlesManager particlesManager;
    public CircleYio viewPosition;
    PointYio speed;
    float friction;
    boolean destroyMode;
    FactorYio destroyFactor;
    float cutSpeedValue;
    float maxRadius;
    public int id;
    public PaViewType viewType;
    boolean hasAngle;
    double deltaAngle;


    public Particle(ParticlesManager particlesManager) {
        this.particlesManager = particlesManager;

        viewPosition = new CircleYio();
        speed = new PointYio();
        destroyFactor = new FactorYio();
    }


    public boolean isCurrentlyVisible() {
        return particlesManager.objectsLayer.gameController.cameraController.isCircleInViewFrame(viewPosition);
    }


    @Override
    public void reset() {
        viewPosition.reset();
        speed.reset();
        friction = 0;
        destroyMode = false;
        destroyFactor.setValues(1, 0);
        destroyFactor.stop();
        cutSpeedValue = 0;
        maxRadius = 0;
        hasAngle = false;
        deltaAngle = 0;
        id = 0;
        viewType = null;
    }


    @Override
    public void moveActually() {
        applySpeed();
        applyFriction();
        updateViewRadius();
        moveDestroy();
        updateAngle();
        applyDeltaAngle();
    }


    private void applyDeltaAngle() {
        if (deltaAngle == 0) return;
        viewPosition.angle += deltaAngle;
    }


    private void updateAngle() {
        if (!hasAngle) return;

        viewPosition.setAngle(speed.getAngle());
    }


    private void updateViewRadius() {
        if (!destroyMode) return;

        viewPosition.radius = destroyFactor.get() * maxRadius;
    }


    boolean isReadyToBeRemoved() {
        return destroyMode && destroyFactor.get() == 0;
    }


    private void moveDestroy() {
        if (destroyMode) {
            destroyFactor.move();
        } else {
            if (Math.abs(speed.x) + Math.abs(speed.y) < cutSpeedValue) {
                destroyMode = true;
                destroyFactor.destroy(1, 0.5);
            }
        }
    }


    private void applyFriction() {
        if (destroyMode && destroyFactor.get() < 0.999f) return;
        speed.x *= 1 - friction / Math.sqrt(RefreshRateDetector.getInstance().multiplier);
        speed.y *= 1 - friction / Math.sqrt(RefreshRateDetector.getInstance().multiplier);
    }


    private void applySpeed() {
        viewPosition.center.add(speed);
    }


    @Override
    public void moveVisually() {

    }


    public void setFriction(float friction) {
        this.friction = friction;
    }


    public void updateMetrics() {
        updateMetrics(0.1);
    }


    public void updateMetrics(double csRatio) {
        maxRadius = viewPosition.radius;
        cutSpeedValue = (float) (csRatio * maxRadius);
    }


    public void setId(int id) {
        this.id = id;
    }


    public void setViewType(PaViewType viewType) {
        this.viewType = viewType;
    }


    public void setHasAngle(boolean hasAngle) {
        this.hasAngle = hasAngle;
    }


    public void setDeltaAngle(double deltaAngle) {
        this.deltaAngle = deltaAngle;
    }
}
