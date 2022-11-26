package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.egg.AeItem;
import yio.tro.opacha.menu.elements.egg.AnimationEggElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.Masking;

public class RenderAnimationEgg extends RenderInterfaceElement{


    private AnimationEggElement egg;
    private TextureRegion background;
    private TextureRegion def;
    private ShapeRenderer shapeRenderer;
    TextureRegion colors[];


    @Override
    public void loadTextures() {
        background = loadEggTexture("bck");
        def = loadEggTexture("def");

        colors = new TextureRegion[5];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = loadEggTexture("c" + (i + 1));
        }
    }


    private TextureRegion loadEggTexture(String name) {
        return GraphicsYio.loadTextureRegion("game/egg/" + name + ".png", false);
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {

    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {
        egg = (AnimationEggElement) element;

        renderBackground();
        beginMask();
        renderColors();
        endMask();
    }


    private void renderColors() {
        GraphicsYio.drawByRectangle(
                batch,
                def,
                egg.getViewPosition()
        );

        for (AeItem color : egg.colors) {
            GraphicsYio.drawByCircle(
                    batch,
                    colors[color.viewIndex],
                    color.viewPosition
            );
        }
    }


    private void endMask() {
        Masking.end(batch);
    }


    private void beginMask() {
        batch.end();

        Masking.begin();
        shapeRenderer = menuViewYio.shapeRenderer;
        shapeRenderer.setProjectionMatrix(menuViewYio.orthoCam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawMasks();
        shapeRenderer.end();

        batch.begin();
        Masking.continueAfterBatchBegin();
    }


    private void drawMasks() {
        for (AeItem mask : egg.masks) {
            shapeRenderer.circle(
                    mask.viewPosition.center.x,
                    mask.viewPosition.center.y,
                    mask.viewPosition.radius
            );
        }
    }


    private void renderBackground() {
        GraphicsYio.drawByRectangle(
                batch,
                background,
                egg.getViewPosition()
        );
    }
}
