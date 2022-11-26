package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.tutorial.GlobalMessageElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RenderableTextYio;
import yio.tro.opacha.stuff.VisualTextContainer;

public class RenderGlobalMessageElement extends RenderInterfaceElement{

    private GlobalMessageElement gmElement;
    private float alpha;
    private TextureRegion backgroundPixel;


    @Override
    public void loadTextures() {
        backgroundPixel = GraphicsYio.loadTextureRegion("pixels/white.png", false);
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        gmElement = (GlobalMessageElement) element;
        alpha = gmElement.getAlpha();

        renderBlackout();
        GraphicsYio.setBatchAlpha(batch, 1);
        renderBackground();
        renderText();
    }


    private void renderText() {
        VisualTextContainer visualTextContainer = gmElement.visualTextContainer;
        for (RenderableTextYio renderableTextYio : visualTextContainer.viewList) {
            GraphicsYio.renderText(batch, renderableTextYio);
        }
    }


    private void renderBackground() {
        GraphicsYio.drawByRectangle(batch, backgroundPixel, gmElement.backgroundPosition);
    }


    private void renderBlackout() {
        GraphicsYio.setBatchAlpha(batch, 0.33 * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, gmElement.blackoutPosition);
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
