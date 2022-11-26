package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.QuickVelocityTutorialElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RectangleYio;
import yio.tro.opacha.stuff.RenderableTextYio;

public class RenderQuickVelocityTutorial extends RenderInterfaceElement{

    private TextureRegion redPixel;
    QuickVelocityTutorialElement qvtElement;
    private float f;
    private RenderableTextYio title;
    private BitmapFont font;
    private TextureRegion whitePixel;
    RectangleYio increasedBounds;


    public RenderQuickVelocityTutorial() {
        increasedBounds = new RectangleYio();
    }


    @Override
    public void loadTextures() {
        redPixel = GraphicsYio.loadTextureRegion("pixels/red.png", false);
        whitePixel = GraphicsYio.loadTextureRegion("pixels/white.png", false);
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        qvtElement = (QuickVelocityTutorialElement) element;
        f = Math.min(element.getFactor().get(), qvtElement.realFactor.get());

        renderBlackouts();
        renderRedBorder();
        renderTitle();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderTitle() {
        title = qvtElement.title;
        updateIncreasedBounds();
        GraphicsYio.setBatchAlpha(batch, 0.8 * f);
        GraphicsYio.drawByRectangle(batch, blackPixel, increasedBounds);
        font = title.font;
        Color fontBackupColor = font.getColor();
        font.setColor(Color.WHITE);
        GraphicsYio.setFontAlpha(title.font, f);
        GraphicsYio.renderText(batch, title);
        font.setColor(fontBackupColor);
    }


    private void updateIncreasedBounds() {
        increasedBounds.setBy(title.bounds);
        increasedBounds.increase(0.015f * GraphicsYio.width);
    }


    private void renderBlackouts() {
        GraphicsYio.setBatchAlpha(batch, 0.33 * f);
        for (RectangleYio blackout : qvtElement.blackouts) {
            GraphicsYio.drawByRectangle(batch, blackPixel, blackout);
        }
    }


    private void renderRedBorder() {
        GraphicsYio.setBatchAlpha(batch, f);
        GraphicsYio.renderBorder(batch, redPixel, qvtElement.focusPosition);
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
