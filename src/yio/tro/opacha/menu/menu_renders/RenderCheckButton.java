package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.Fonts;
import yio.tro.opacha.menu.elements.CheckButtonYio;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RenderableTextYio;

public class RenderCheckButton extends RenderInterfaceElement {

    TextureRegion activeTexture;
    private BitmapFont font;
    private CheckButtonYio checkButton;
    private float f;


    @Override
    public void loadTextures() {
        activeTexture = GraphicsYio.loadTextureRegion("menu/check_button/chk_active.png", true);
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        checkButton = (CheckButtonYio) element;
        font = Fonts.smallFont;
        f = checkButton.getAlpha();

        if (f < 0.4) return;

        renderSelection();
        renderActiveSquare();
        renderText();

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderText() {
        if (f < RenderableTextYio.OPTIMIZATION_CUT_OUT) {
            GraphicsYio.setBatchAlpha(batch, 0.15 * f);
            GraphicsYio.drawByRectangle(batch, blackPixel, checkButton.renderableText.bounds);
            GraphicsYio.setBatchAlpha(batch, 1);
            return;
        }

        if (f < 1) {
            GraphicsYio.setFontAlpha(font, f);
            GraphicsYio.renderText(batch, checkButton.renderableText);
            GraphicsYio.setFontAlpha(font, 1);
            return;
        }

        GraphicsYio.renderText(batch, checkButton.renderableText);
    }


    private void renderActiveSquare() {
        if (f < 1) {
            GraphicsYio.setBatchAlpha(batch, f);
        }
        GraphicsYio.renderBorder(batch, blackPixel, checkButton.getActiveSquare());

        if (checkButton.activeFactor.get() > 0) {
            GraphicsYio.setBatchAlpha(batch, Math.min(checkButton.activeFactor.get(), f));
            GraphicsYio.drawByRectangle(batch, activeTexture, checkButton.getActiveSquare());
        }
    }


    private void renderSelection() {
        if (checkButton.selectionFactor.get() > 0) {
            GraphicsYio.setBatchAlpha(batch, 0.2 * checkButton.selectionFactor.get());
            GraphicsYio.drawByRectangle(batch, blackPixel, checkButton.getViewPosition());
            GraphicsYio.setBatchAlpha(batch, 1);
        }
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
