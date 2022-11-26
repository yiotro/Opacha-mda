package yio.tro.opacha.menu.elements.egg;

import yio.tro.opacha.Yio;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.stuff.CircleYio;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.factor_yio.FactorYio;
import yio.tro.opacha.stuff.object_pool.ReusableYio;

public class AeItem implements ReusableYio{


    AnimationEggElement animationEggElement;
    public FactorYio appearFactor;
    public CircleYio viewPosition;
    public CircleYio startPosition, endPosition;
    FactorYio lifeFactor;
    PointYio speed;
    public int viewIndex;
    float speedDelta;
    private double speedAngle;
    float gravity;
    boolean goToCenterAfterDeath;


    public AeItem(AnimationEggElement animationEggElement) {
        this.animationEggElement = animationEggElement;

        appearFactor = new FactorYio();
        viewPosition = new CircleYio();
        startPosition = new CircleYio();
        endPosition = new CircleYio();
        lifeFactor = new FactorYio();
        speed = new PointYio();
    }


    @Override
    public void reset() {
        appearFactor.reset();
        viewPosition.reset();
        startPosition.reset();
        endPosition.reset();
        lifeFactor.reset();
        speed.reset();
        viewIndex = -1;
        speedDelta = 0;
        goToCenterAfterDeath = false;
    }


    void onSpawned() {
        lifeFactor.appear(0, 0.05 + 0.05 * YioGdxGame.random.nextDouble());

        speedAngle = Yio.getRandomAngle();
        speed.relocateRadial(
                YioGdxGame.random.nextDouble() * 0.002 * GraphicsYio.width,
                speedAngle
        );

        gravity = 0.00005f * GraphicsYio.width;
        speedDelta = (2 * YioGdxGame.random.nextFloat() - 1) * 0.00002f * GraphicsYio.width;
    }


    public void setGravity(float gravity) {
        this.gravity = gravity;
    }


    boolean isAlive() {
        if (lifeFactor.get() < 1) return true;
        if (appearFactor.get() > 0) return true;

        return false;
    }


    void move() {
        moveAppearFactor();
        updateViewPosition();
        moveLifeFactor();
        checkToDie();
        applySpeed();
        applySpeedDelta();
        applyGravityToCenter();
    }


    private void applyGravityToCenter() {
        speed.relocateRadial(
                gravity,
                viewPosition.center.angleTo(animationEggElement.centerOfGravity)
        );
    }


    private void applySpeedDelta() {
        speed.relocateRadial(
                speedDelta,
                speedAngle
        );
    }


    private void applySpeed() {
        endPosition.center.x += speed.x;
        endPosition.center.y += speed.y;

        if (followEndConditions()) {
            startPosition.center.setBy(endPosition.center);
        }
    }


    private boolean followEndConditions() {
        if (goToCenterAfterDeath && appearFactor.isInDestroyState()) return false;

        return appearFactor.get() == 1 || appearFactor.isInDestroyState();
    }


    private void checkToDie() {
        if (lifeFactor.get() < 1) return;
        if (appearFactor.isInDestroyState()) return;

        kill();
    }


    void kill() {
        appearFactor.destroy(2, 0.6 + 0.3 * YioGdxGame.random.nextDouble());
        lifeFactor.setValues(1, 0);

        if (goToCenterAfterDeath) {
            startPosition.center.setBy(animationEggElement.centerOfGravity);
        }
    }


    private void moveLifeFactor() {
        lifeFactor.move();
    }


    private void updateViewPosition() {
        viewPosition.center.x = startPosition.center.x + appearFactor.get() * (endPosition.center.x - startPosition.center.x);
        viewPosition.center.y = startPosition.center.y + appearFactor.get() * (endPosition.center.y - startPosition.center.y);
        viewPosition.radius = startPosition.radius + appearFactor.get() * (endPosition.radius - startPosition.radius);
    }


    private void moveAppearFactor() {
        appearFactor.move();
    }


    public void setViewIndex(int viewIndex) {
        this.viewIndex = viewIndex;
    }


    public void setSpeedDelta(float speedDelta) {
        this.speedDelta = speedDelta;
    }


    public void setGoToCenterAfterDeath(boolean goToCenterAfterDeath) {
        this.goToCenterAfterDeath = goToCenterAfterDeath;
    }

}
