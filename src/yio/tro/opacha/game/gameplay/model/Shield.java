package yio.tro.opacha.game.gameplay.model;

import yio.tro.opacha.stuff.CircleYio;
import yio.tro.opacha.stuff.object_pool.ReusableYio;

public class Shield implements ReusableYio{

    public Planet planet;
    public CircleYio viewPosition;
    public double angle;
    public boolean active;


    public Shield(Planet planet) {
        this.planet = planet;
        viewPosition = new CircleYio();
        reset();
    }


    @Override
    public void reset() {
        viewPosition.reset();
        angle = 0;
        active = false;
    }


    void move() {
        updateViewPosition();
    }


    private void updateViewPosition() {
        viewPosition.setBy(planet.viewPosition);
        viewPosition.setAngle(angle);
    }


    public boolean isActive() {
        return active;
    }


    public void setAngle(double angle) {
        this.angle = angle;
    }


    public void setActive(boolean active) {
        this.active = active;
    }
}
