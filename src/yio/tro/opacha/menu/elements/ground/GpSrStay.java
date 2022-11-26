package yio.tro.opacha.menu.elements.ground;

import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.stuff.GraphicsYio;

public class GpSrStay extends AbstractGpSpawnRule{

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
    }
}
