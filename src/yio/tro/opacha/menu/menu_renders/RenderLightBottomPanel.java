package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.LightBottomPanelElement;
import yio.tro.opacha.stuff.GraphicsYio;

public class RenderLightBottomPanel extends RenderInterfaceElement{


    private TextureRegion backgroundTexture;
    private LightBottomPanelElement lbpElement;


    @Override
    public void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("menu/round_shape/bg5.png", false);
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        lbpElement = (LightBottomPanelElement) element;

        GraphicsYio.drawByRectangle(batch, backgroundTexture, lbpElement.renderPosition);
        GraphicsYio.renderTextOptimized(batch, blackPixel, lbpElement.title, lbpElement.getAlpha());
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
