package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RectangleYio;

public class RenderShadow extends RenderInterfaceElement{

    public static final int SRC_SIZE = 400;
    public static final int BLUR_RADIUS = 13;
    public static final int OFFSET = 50 - BLUR_RADIUS;
    public static final int CORNER_SIZE = 2 * BLUR_RADIUS;

    private Texture srcShadow;
    private TextureRegion textureCorner, textureSide;
    float cornerRadius;
    private RectangleYio pos;
    public float incOffset, slideOffset;
    RectangleYio internalFill;
    SpriteBatch currentBatch;
    public float defCornerRadius;


    public RenderShadow() {
        setIncOffset(0.06f * GraphicsYio.width);
        pos = new RectangleYio();
        internalFill = new RectangleYio();
        currentBatch = null;
        defCornerRadius = 0.19f * GraphicsYio.width;
    }


    @Override
    public void loadTextures() {
        srcShadow = new Texture(Gdx.files.internal("menu/shadow.png"));
        srcShadow.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        textureCorner = new TextureRegion(srcShadow, OFFSET, OFFSET, CORNER_SIZE, CORNER_SIZE);
        textureCorner.flip(false, true);
        textureSide = new TextureRegion(srcShadow, SRC_SIZE / 2, OFFSET, 1, CORNER_SIZE);
        textureSide.flip(false, true);
    }


    public void renderShadow(RectangleYio position) {
        renderShadow(position, defCornerRadius);
    }


    public void renderShadow(RectangleYio position, float cornerRadius) {
        renderShadow(batch, position, cornerRadius);
    }


    public void renderShadow(SpriteBatch argBatch, RectangleYio position, float cornerRadius) {
        currentBatch = argBatch;
        this.cornerRadius = cornerRadius;
        updatePos(position);

        renderSides();
        renderCorners();
        renderInternalFill();
    }


    private void renderInternalFill() {
        internalFill.x = pos.x + cornerRadius;
        internalFill.y = pos.y + cornerRadius;
        internalFill.width = pos.width - 2 * cornerRadius;
        internalFill.height = pos.height - 2 * cornerRadius;

        GraphicsYio.drawByRectangle(
                currentBatch,
                blackPixel,
                internalFill
        );
    }


    private void updatePos(RectangleYio position) {
        pos.setBy(position);

        pos.x -= incOffset;
        pos.y -= incOffset;
        pos.width += 2 * incOffset;
        pos.height += 2 * incOffset;

        pos.y -= slideOffset;
    }


    private void renderCorners() {
        GraphicsYio.drawRectangleRotatedSimple(
                currentBatch,
                textureCorner,
                pos.x,
                pos.y,
                cornerRadius,
                cornerRadius,
                0
        );

        GraphicsYio.drawRectangleRotatedSimple(
                currentBatch,
                textureCorner,
                pos.x + pos.width,
                pos.y,
                cornerRadius,
                cornerRadius,
                Math.PI / 2
        );

        GraphicsYio.drawRectangleRotatedSimple(
                currentBatch,
                textureCorner,
                pos.x + pos.width,
                pos.y + pos.height,
                cornerRadius,
                cornerRadius,
                Math.PI
        );

        GraphicsYio.drawRectangleRotatedSimple(
                currentBatch,
                textureCorner,
                pos.x,
                pos.y + pos.height,
                cornerRadius,
                cornerRadius,
                1.5 * Math.PI
        );
    }


    private void renderSides() {
        GraphicsYio.drawRectangleRotatedSimple(
                currentBatch,
                textureSide,
                pos.x + cornerRadius,
                pos.y,
                pos.width - 2 * cornerRadius,
                cornerRadius,
                0);

        GraphicsYio.drawRectangleRotatedSimple(
                currentBatch,
                textureSide,
                pos.x + pos.width,
                pos.y + cornerRadius,
                pos.height - 2 * cornerRadius,
                cornerRadius,
                Math.PI / 2
        );

        GraphicsYio.drawRectangleRotatedSimple(
                currentBatch,
                textureSide,
                pos.x + pos.width - cornerRadius,
                pos.y + pos.height,
                pos.width - 2 * cornerRadius,
                cornerRadius,
                Math.PI
        );

        GraphicsYio.drawRectangleRotatedSimple(
                currentBatch,
                textureSide,
                pos.x,
                pos.y + pos.height - cornerRadius,
                pos.height - 2 * cornerRadius,
                cornerRadius,
                1.5 * Math.PI
        );
    }


    public void setIncOffset(float incOffset) {
        this.incOffset = incOffset;
        slideOffset = incOffset / 2;
    }


    public void setCurrentBatch(SpriteBatch currentBatch) {
        this.currentBatch = currentBatch;
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {

    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
