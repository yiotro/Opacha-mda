package yio.tro.opacha.menu.elements.ground;

import yio.tro.opacha.stuff.RepeatYio;

public abstract class AbstractGpDeathRule {

    GroundElement groundElement;
    RepeatYio<AbstractGpDeathRule> repeatDie;


    public AbstractGpDeathRule() {
        repeatDie = new RepeatYio<AbstractGpDeathRule>(this, getFrequency()) {
            @Override
            public void performAction() {
                performDeath();
            }
        };
    }


    protected abstract int getFrequency();


    protected abstract void performDeath();


    void move() {
        repeatDie.move();
    }


    public void setGroundElement(GroundElement groundElement) {
        this.groundElement = groundElement;
    }
}
