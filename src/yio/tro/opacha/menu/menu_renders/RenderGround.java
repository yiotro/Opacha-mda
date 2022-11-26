package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.ground.GeParticle;
import yio.tro.opacha.menu.elements.ground.GroundElement;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;

public class RenderGround extends RenderInterfaceElement{

    public static final int QUANTITY = 7;
    TextureRegion groundTextures[];
    private GroundElement groundElement;
    private TextureRegion particleTexture1, particleTexture2;


    @Override
    public void loadTextures() {
        groundTextures = new TextureRegion[QUANTITY];

        for (int i = 0; i < groundTextures.length; i++) {
            groundTextures[i] = GraphicsYio.loadTextureRegion("menu/ground/ground" + i + ".png", false);
        }

        particleTexture1 = GraphicsYio.loadTextureRegion("menu/ground/particle1.png", false);
        particleTexture2 = GraphicsYio.loadTextureRegion("menu/ground/particle2.png", false);
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        groundElement = (GroundElement) element;

        updateAlpha();
        renderGround();
        renderParticles();

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderParticles() {
        GraphicsYio.setBatchAlpha(batch, 0.2f * groundElement.pFactor.get() * groundElement.getFactor().get());

        for (GeParticle particle : groundElement.particles) {
            GraphicsYio.drawByCircle(
                    batch,
                    getParticleTexture(),
                    particle.viewPosition
            );
        }
    }


    private TextureRegion getParticleTexture() {
        if (groundElement.invertedParticlesMode) {
            return particleTexture2;
        }

        return particleTexture1;
    }


    private void renderGround() {
        GraphicsYio.drawByRectangle(
                batch,
                groundTextures[groundElement.getGroundIndex()],
                groundElement.getViewPosition()
        );
    }


    private void updateAlpha() {
        if (groundElement.getFactor().isInAppearState() && groundElement.getFactor().get() < 1) {
            GraphicsYio.setBatchAlpha(batch, Math.sqrt(groundElement.getFactor().get()));
            return;
        }

        GraphicsYio.setBatchAlpha(batch, groundElement.getFactor().get());
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
