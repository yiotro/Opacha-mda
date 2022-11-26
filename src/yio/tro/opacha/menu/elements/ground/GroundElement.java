package yio.tro.opacha.menu.elements.ground;

import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.stuff.RepeatYio;
import yio.tro.opacha.stuff.factor_yio.FactorYio;
import yio.tro.opacha.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class GroundElement extends InterfaceElement<GroundElement> {

    int groundIndex;
    public FactorYio pFactor;
    public ArrayList<GeParticle> particles;
    ObjectPoolYio<GeParticle> poolParticles;
    AbstractGpSpawnRule spawnRule;
    RepeatYio<GroundElement> repeatRemove;
    AbstractGpDeathRule deathRule;
    boolean firstLaunch;
    public boolean invertedParticlesMode;
    public float scrollDelta;


    public GroundElement(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);

        groundIndex = 0;
        pFactor = new FactorYio();
        particles = new ArrayList<>();
        setSpawnRule(new GpSrDefault());
        setDeathRule(new GpDrDefault());
        firstLaunch = true;
        invertedParticlesMode = false;
        scrollDelta = 0;

        initPool();
        initRepeats();
    }


    private void initRepeats() {
        repeatRemove = new RepeatYio<GroundElement>(this, 60) {
            @Override
            public void performAction() {
                removeDeadParticles();
            }
        };
    }


    private void removeDeadParticles() {
        for (int i = particles.size() - 1; i >= 0; i--) {
            GeParticle geParticle = particles.get(i);
            if (geParticle.fresh) continue;
            if (geParticle.viewPosition.radius > 0) continue;

            particles.remove(i);
            poolParticles.add(geParticle);
        }
    }


    GeParticle spawnParticle() {
        GeParticle newParticle = poolParticles.getNext();
        particles.add(newParticle);
        return newParticle;
    }


    private void initPool() {
        poolParticles = new ObjectPoolYio<GeParticle>() {
            @Override
            public GeParticle makeNewObject() {
                return new GeParticle(GroundElement.this);
            }
        };
    }


    @Override
    protected GroundElement getThis() {
        return this;
    }


    @Override
    public void move() {
        updateViewPosition();
        moveParticleFactor();
        moveParticles();
        spawnRule.move();
        deathRule.move();
        repeatRemove.move();
        checkForFirstLaunch();
    }


    private void checkForFirstLaunch() {
        if (!firstLaunch) return;

        firstLaunch = false;

        for (int i = 0; i < 250; i++) {
            move();
        }
    }


    private void moveParticles() {
        for (GeParticle particle : particles) {
            particle.move();
        }
    }


    private void moveParticleFactor() {
        pFactor.move();
    }


    @Override
    public void onDestroy() {
        pFactor.destroy(1, 3);
    }


    @Override
    public void onAppear() {
        pFactor.reset();
        pFactor.appear(3, 0.5);
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean isTouchable() {
        return false;
    }


    @Override
    public boolean touchDown() {
        return false;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        return false;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderGround;
    }


    public int getGroundIndex() {
        return groundIndex;
    }


    public GroundElement setSpawnRule(AbstractGpSpawnRule spawnRule) {
        this.spawnRule = spawnRule;
        spawnRule.setGroundElement(this);
        return this;
    }


    @Override
    protected void onChildAdded(InterfaceElement child) {
        super.onChildAdded(child);
        child.setAnimation(AnimationYio.none);
    }


    public GroundElement setDeathRule(AbstractGpDeathRule deathRule) {
        this.deathRule = deathRule;
        deathRule.setGroundElement(this);
        return this;
    }


    public GroundElement setGroundIndex(int groundIndex) {
        this.groundIndex = groundIndex;
        return this;
    }


    public GroundElement setInvertedParticlesMode(boolean invertedParticlesMode) {
        this.invertedParticlesMode = invertedParticlesMode;
        return this;
    }


    public void setScrollDelta(float scrollDelta) {
        this.scrollDelta = scrollDelta;

        for (GeParticle particle : particles) {
            particle.updatePos();
        }
    }
}
