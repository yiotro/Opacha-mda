package yio.tro.opacha.menu.elements.ground;

import yio.tro.opacha.stuff.RepeatYio;

public abstract class AbstractGpSpawnRule {

    GroundElement groundElement;
    RepeatYio<AbstractGpSpawnRule> repeatSpawn;


    public AbstractGpSpawnRule() {
        repeatSpawn = new RepeatYio<AbstractGpSpawnRule>(this, getFrequency()) {
            @Override
            public void performAction() {
                performSpawn();
            }
        };
    }


    protected abstract int getFrequency();


    protected abstract void performSpawn();


    void move() {
        repeatSpawn.move();
    }


    public void setGroundElement(GroundElement groundElement) {
        this.groundElement = groundElement;
    }
}
