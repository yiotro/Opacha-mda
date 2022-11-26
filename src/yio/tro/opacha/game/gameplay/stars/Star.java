package yio.tro.opacha.game.gameplay.stars;

import yio.tro.opacha.game.GameController;
import yio.tro.opacha.game.gameplay.AcceleratableYio;
import yio.tro.opacha.stuff.CircleYio;
import yio.tro.opacha.stuff.factor_yio.FactorYio;
import yio.tro.opacha.stuff.object_pool.ReusableYio;

public class Star implements ReusableYio, AcceleratableYio{

    public StarsManager starsManager;
    public CircleYio position;
    public CircleYio viewPosition;
    public FactorYio appearFactor;
    boolean needToUpdateViewRadius;
    boolean alive;
    FactorYio lifeFactor;


    public Star(StarsManager starsManager) {
        this.starsManager = starsManager;
        position = new CircleYio();
        viewPosition = new CircleYio();
        appearFactor = new FactorYio();
        lifeFactor = new FactorYio();
    }


    @Override
    public void reset() {
        position.reset();
        viewPosition.reset();
        appearFactor.reset();
        needToUpdateViewRadius = false;
        lifeFactor.reset();
        alive = true;
    }


    void spawn() {
        appearFactor.appear(3, 0.5);
        lifeFactor.appear(0, 0.1);
    }


    public boolean isCurrentlyVisible() {
        GameController gameController = starsManager.objectsLayer.gameController;
        return gameController.cameraController.isCircleInViewFrame(viewPosition);
    }


    void setPosition(double x, double y) {
        position.center.set(x, y);
        viewPosition.center.setBy(position.center);
    }


    @Override
    public void moveActually() {
        if (appearFactor.move()) {
            needToUpdateViewRadius = true;
        }
        if (lifeFactor.move() && lifeFactor.get() == 1) {
            destroy();
        }
    }


    private void destroy() {
        appearFactor.destroy(1, 0.2);
        alive = false;
    }


    public boolean isAlive() {
        return alive;
    }


    private void updateViewRadius() {
        if (!needToUpdateViewRadius) return;
        needToUpdateViewRadius = false;
        viewPosition.radius = appearFactor.get() * position.radius;
    }


    @Override
    public void moveVisually() {
        updateViewRadius();
    }
}
