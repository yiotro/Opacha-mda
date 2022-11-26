package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.VelocitySliderElement;
import yio.tro.opacha.stuff.GraphicsYio;

public class RenderVelocitySlider extends RenderInterfaceElement{


    private VelocitySliderElement vsElement;
    private float alpha;
    private TextureRegion frameTexture;
    private TextureRegion lineTexture;


    @Override
    public void loadTextures() {
        frameTexture = GraphicsYio.loadTextureRegion("menu/gameplay/velocity_frame.png", true);
        lineTexture = GraphicsYio.loadTextureRegion("menu/gameplay/velocity_line.png", true);
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        vsElement = (VelocitySliderElement) element;
        alpha = vsElement.getAlpha();
        if (getGameView().appearFactor.get() < 0.1) return;

        renderLine();
        renderFrame();
        renderText();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderText() {
        GraphicsYio.setFontAlpha(vsElement.title.font, 0.5 + 0.5 * vsElement.selectionEngineYio.factorYio.get());
        GraphicsYio.renderText(batch, vsElement.title);
        GraphicsYio.setFontAlpha(vsElement.title.font, 1);
    }


    private void renderFrame() {
        GraphicsYio.setBatchAlpha(batch, 0.5 + 0.5 * vsElement.selectionEngineYio.factorYio.get());
        GraphicsYio.drawByCircle(batch, frameTexture, vsElement.visualFrame);
    }


    private void renderLine() {
        float f = vsElement.selectionEngineYio.factorYio.get();
        if (f == 0) return;
        GraphicsYio.setBatchAlpha(batch, f);
        GraphicsYio.drawByRectangle(batch, lineTexture, vsElement.centralLine);
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
