package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.NotificationElement;
import yio.tro.opacha.stuff.GraphicsYio;

public class RenderNotification extends RenderInterfaceElement {

    private TextureRegion backgroundTexture;
    private NotificationElement notificationElement;
    private BitmapFont font;


    @Override
    public void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("menu/round_shape/bg0.png", false);
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        notificationElement = (NotificationElement) element;
        font = notificationElement.font;

        if (notificationElement.getFactor().get() < 0.01) return;

        renderShadow(notificationElement.shadowPosition);
        renderBackground();
        renderMessage();
    }


    private void renderMessage() {
        Color color = font.getColor();
        font.setColor(Color.BLACK);
        GraphicsYio.setFontAlpha(font, notificationElement.getFactor().get());

        GraphicsYio.renderText(batch, font, notificationElement.message, notificationElement.textPosition);

        GraphicsYio.setFontAlpha(font, 1);
        font.setColor(color);
    }


    private void renderBackground() {

        GraphicsYio.drawByRectangle(
                batch,
                backgroundTexture,
                notificationElement.getViewPosition()
        );
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
