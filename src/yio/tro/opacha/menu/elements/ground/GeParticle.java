package yio.tro.opacha.menu.elements.ground;

import yio.tro.opacha.RefreshRateDetector;
import yio.tro.opacha.stuff.CircleYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.factor_yio.FactorYio;
import yio.tro.opacha.stuff.object_pool.ReusableYio;

public class GeParticle implements ReusableYio{

    GroundElement groundElement;
    public CircleYio viewPosition;
    PointYio position, speed;
    boolean fresh;
    FactorYio appearFactor;
    float targetRadius;


    public GeParticle(GroundElement groundElement) {
        this.groundElement = groundElement;

        viewPosition = new CircleYio();
        position = new PointYio();
        speed = new PointYio();
        appearFactor = new FactorYio();
    }


    @Override
    public void reset() {
        viewPosition.reset();
        position.reset();
        speed.reset();
        appearFactor.reset();
        appearFactor.appear(3, 0.25);
        targetRadius = 0;
        fresh = true;
    }


    void move() {
        appearFactor.move();
        applySpeed();
        updatePos();
    }


    void updatePos() {
        updateViewPosition();
        applyScrollDelta();
    }


    private void applyScrollDelta() {
        if (groundElement.scrollDelta == 0) return;

        viewPosition.center.y += groundElement.scrollDelta;

        while (viewPosition.center.y > groundElement.getViewPosition().y + groundElement.getViewPosition().height) {
            viewPosition.center.y -= groundElement.getViewPosition().height;
        }

        while (viewPosition.center.y < groundElement.getViewPosition().y) {
            viewPosition.center.y += groundElement.getViewPosition().height;
        }
    }


    private void updateViewPosition() {
        viewPosition.center.x = groundElement.getViewPosition().x + position.x;
        viewPosition.center.y = groundElement.getViewPosition().y + position.y;
        viewPosition.radius = appearFactor.get() * targetRadius;
    }


    private void applySpeed() {
        position.x += speed.x * RefreshRateDetector.getInstance().multiplier;
        position.y += speed.y * RefreshRateDetector.getInstance().multiplier;
    }


    public void setTargetRadius(float targetRadius) {
        this.targetRadius = targetRadius;
    }


    void prepareToDie() {
        fresh = false;
    }
}
