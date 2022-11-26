package yio.tro.opacha.menu.elements.ground;

import yio.tro.opacha.YioGdxGame;

import java.util.ArrayList;

public class GpDrDefault extends AbstractGpDeathRule{


    @Override
    protected int getFrequency() {
        return 4;
    }


    @Override
    protected void performDeath() {
        ArrayList<GeParticle> particles = groundElement.particles;
        if (particles.size() < 20) return;

        int index = YioGdxGame.random.nextInt(particles.size());
        GeParticle geParticle = particles.get(index);

        geParticle.prepareToDie();
        geParticle.appearFactor.destroy(1, 0.2);
    }
}
