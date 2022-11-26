package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.CircleButtonYio;
import yio.tro.opacha.stuff.GraphicsYio;

public class RenderCircleButton extends RenderInterfaceElement{

    private CircleButtonYio circleButton;
    private TextureRegion selectEffectTexture;
    private float f;


    @Override
    public void loadTextures() {
        selectEffectTexture = GraphicsYio.loadTextureRegion("menu/selection.png", true);
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        circleButton = (CircleButtonYio) element;
        f = circleButton.getAlpha();

        if (f < 1) {
            batch.setColor(c.r, c.g, c.b, f);
        } else {
            batch.setColor(c.r, c.g, c.b, 1);
        }

        GraphicsYio.drawByRectangle(batch, circleButton.textureRegion, circleButton.getViewPosition());

        if (circleButton.isSelected()) {
            renderSelectEffect();
        }
    }


    private void renderSelectEffect() {
        if (f < 0.01) return;

        batch.setColor(c.r, c.g, c.b, f);

        GraphicsYio.drawFromCenter(batch, selectEffectTexture, circleButton.getEffectX(), circleButton.getEffectY(), circleButton.getEffectRadius());
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
