package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RectangleYio;
import yio.tro.opacha.stuff.RenderableTextYio;

public class RenderButton extends RenderInterfaceElement {

    TextureRegion buttonPixel, blackPixel;
    private ButtonYio buttonYio;
    private RectangleYio viewPos;
    private RectangleYio touchArea;
    private float f;


    public RenderButton() {
        touchArea = new RectangleYio();
    }


    @Override
    public void loadTextures() {
        buttonPixel = GraphicsYio.loadTextureRegion("pixels/button.png", false);
        blackPixel = GraphicsYio.loadTextureRegion("pixels/black.png", false);
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        if (element.getFactor().get() < 0.001) return;

        updateReferences(element);
        if (!buttonYio.isRenderable()) return;

        renderCurrentButton();
    }


    private void updateReferences(InterfaceElement element) {
        buttonYio = (ButtonYio) element;
        f = buttonYio.getAlpha();
        viewPos = buttonYio.getViewPosition();
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }


    public void renderCurrentButton() {
        renderButtonShadow();
        updateBatchAlpha();
        renderDefaultBackground();
        renderCustomBackground();
        renderBorder();

        batch.setColor(c.r, c.g, c.b, 1);

        renderSelection();
        renderText();
    }


    private void renderBorder() {
        if (!buttonYio.isBorderEnabled()) return;

        batch.setColor(c.r, c.g, c.b, 0.25f * f);

        MenuRenders.renderRoundBorder.renderRoundBorder(viewPos, buttonYio.cornerRadius);
    }


    private void renderCustomBackground() {
        if (!buttonYio.hasCustomTexture()) return;

        GraphicsYio.drawByRectangle(batch, buttonYio.getTextureRegion(), viewPos);
    }


    private void renderDefaultBackground() {
        if (buttonYio.hasCustomTexture()) return;
        if (!buttonYio.backgroundEnabled) return;

        MenuRenders.renderRoundShape.renderRoundShape(viewPos, buttonYio.groundIndex, buttonYio.cornerRadius);
        GraphicsYio.setBatchAlpha(batch, 0.15f * f * f);
        MenuRenders.renderRoundShape.renderRoundShape(viewPos, 5, buttonYio.cornerRadius);
    }


    private void renderText() {
        if (f < 0.5) return;

        if (buttonYio.getFactor().get() < RenderableTextYio.OPTIMIZATION_CUT_OUT) {
            GraphicsYio.setBatchAlpha(batch, 0.15 * f);
            for (RenderableTextYio item : buttonYio.items) {
                if (item.string.length() < 2) continue;
                GraphicsYio.drawByRectangle(batch, blackPixel, item.bounds);
            }
            GraphicsYio.setBatchAlpha(batch, 1);
            return;
        }

        GraphicsYio.setFontAlpha(buttonYio.font, (f - 0.5) * 2);

        for (RenderableTextYio item : buttonYio.items) {
            GraphicsYio.renderText(batch, item);
        }

        GraphicsYio.setFontAlpha(buttonYio.font, 1);
    }


    private void renderTouchArea() {
        updateTouchArea();

        GraphicsYio.renderBorder(batch, blackPixel, touchArea);
    }


    private void updateTouchArea() {
        RectangleYio pos = buttonYio.getViewPosition();
        touchArea.x = pos.x - buttonYio.getHorizontalTouchOffset();
        touchArea.width = pos.width + 2 * buttonYio.getHorizontalTouchOffset();
        touchArea.y = pos.y - buttonYio.getVerticalTouchOffset();
        touchArea.height = pos.height + 2 * buttonYio.getVerticalTouchOffset();
    }


    private void updateBatchAlpha() {
        if (f <= 1) {
            batch.setColor(c.r, c.g, c.b, f * f);
        } else {
            batch.setColor(c.r, c.g, c.b, 1);
        }
    }


    public void renderSelection() {
        if (!buttonYio.isSelected()) return;
        if (buttonYio.isInSilentReactionMode()) return;

        batch.setColor(c.r, c.g, c.b, Math.min(0.25f * buttonYio.selectionFactor.get(), f * f));

        if (buttonYio.selectionTexture == null) {
            if (buttonYio.rectangularSelectionEnabled) {
                GraphicsYio.drawByRectangle(batch, blackPixel, viewPos);
            } else {
                MenuRenders.renderRoundShape.renderRoundShape(viewPos, -1, buttonYio.cornerRadius);
            }
        } else {
            GraphicsYio.drawFromCenter(
                    batch,
                    buttonYio.selectionTexture,
                    viewPos.x + viewPos.width / 2,
                    viewPos.y + viewPos.height / 2,
                    viewPos.width
            );
        }

        batch.setColor(c.r, c.g, c.b, 1);
    }


    public void renderButtonShadow() {
        if (!buttonYio.isVisible()) return;
        if (!buttonYio.shadowEnabled) return;

        GraphicsYio.setBatchAlpha(batch, f * f);
        MenuRenders.renderShadow.renderShadow(viewPos);
    }

}
