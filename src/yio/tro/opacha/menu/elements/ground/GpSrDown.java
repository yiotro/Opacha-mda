package yio.tro.opacha.menu.elements.ground;

import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.stuff.GraphicsYio;

public class GpSrDown extends AbstractGpSpawnRule{

    @Override
    protected int getFrequency() {
        return 5;
    }


    @Override
    protected void performSpawn() {
        GeParticle geParticle = groundElement.spawnParticle();

        geParticle.position.x = YioGdxGame.random.nextFloat() * groundElement.getPosition().width;
        geParticle.position.y = YioGdxGame.random.nextFloat() * groundElement.getPosition().height;
        geParticle.setTargetRadius((0.5f + 0.5f * YioGdxGame.random.nextFloat()) * 0.015f * GraphicsYio.width);
        geParticle.speed.relocateRadial(
                YioGdxGame.random.nextDouble() * 0.1 * geParticle.targetRadius,
                - Math.PI / 2 + (2 * YioGdxGame.random.nextDouble() - 1) * (Math.PI / 6)
        );
    }
}
