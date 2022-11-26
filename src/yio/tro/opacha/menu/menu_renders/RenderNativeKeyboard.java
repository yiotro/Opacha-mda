package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.keyboard.NativeKeyboardElement;
import yio.tro.opacha.stuff.GraphicsYio;

public class RenderNativeKeyboard extends RenderInterfaceElement{

    private NativeKeyboardElement nativeKeyboardElement;
    private float alpha;
    private TextureRegion tfBackground;


    @Override
    public void loadTextures() {
        tfBackground = GraphicsYio.loadTextureRegion("pixels/white.png", false);
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        nativeKeyboardElement = (NativeKeyboardElement) element;
        alpha = nativeKeyboardElement.getFactor().get();

        renderBlackout();
        renderFrame();

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderFrame() {
        if (nativeKeyboardElement.tfFactor.get() < 0.1) return;

        GraphicsYio.setBatchAlpha(batch, 1);
        GraphicsYio.drawByRectangle(batch, tfBackground, nativeKeyboardElement.tfFrame);
        GraphicsYio.renderBorder(batch, blackPixel, nativeKeyboardElement.tfFrame, GraphicsYio.borderThickness);
    }


    private void renderBlackout() {
        GraphicsYio.setBatchAlpha(batch, 0.15 * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, nativeKeyboardElement.blackoutPosition);
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }

}
