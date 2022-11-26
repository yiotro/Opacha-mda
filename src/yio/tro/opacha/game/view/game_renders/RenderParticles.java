package yio.tro.opacha.game.view.game_renders;

import yio.tro.opacha.game.gameplay.particles.PaViewType;
import yio.tro.opacha.game.gameplay.particles.Particle;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.Storage3xTexture;

import java.util.HashMap;

public class RenderParticles extends GameRender{

    HashMap<PaViewType, Storage3xTexture> mapViewTypes;


    @Override
    protected void loadTextures() {
        mapViewTypes = new HashMap<>();
        for (PaViewType paViewType : PaViewType.values()) {
            Storage3xTexture storage3xTexture = load3xTexture("p_" + paViewType);
            mapViewTypes.put(paViewType, storage3xTexture);
        }
    }


    @Override
    public void render() {
        for (Particle particle : gameController.objectsLayer.particlesManager.particles) {
            if (!particle.isCurrentlyVisible()) continue;
            if (particle.id > getCurrentZoomQuality() && particle.viewPosition.radius < 0.05f * GraphicsYio.width) continue;

            GraphicsYio.drawByCircle(
                    batchMovable,
                    mapViewTypes.get(particle.viewType).getTexture(getCurrentZoomQuality()),
                    particle.viewPosition
            );
        }
    }


    @Override
    protected void disposeTextures() {

    }


}
