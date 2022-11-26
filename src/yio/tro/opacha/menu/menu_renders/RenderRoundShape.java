package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.stuff.*;

public class RenderRoundShape extends RenderInterfaceElement{


    public static final int COLORS_QUANTITY = 6;
    private TextureRegion grayBackground;
    private TextureRegion grayCorner;
    TextureRegion currentBackground;
    TextureRegion currentCorner;
    float cr;
    RectangleYio pos1, pos2, pos3;
    private RectangleYio position;
    CircleYio corners[];
    private TextureRegion blackBackground;
    private TextureRegion blackCorner;
    SpriteBatch currentBatch;
    private TextureRegion bgCorners[];
    private TextureRegion bgMains[];


    public RenderRoundShape() {
        pos1 = new RectangleYio();
        pos2 = new RectangleYio();
        pos3 = new RectangleYio();
        currentBackground = null;

        corners = new CircleYio[4];
        for (int i = 0; i < corners.length; i++) {
            corners[i] = new CircleYio();
        }
    }


    @Override
    public void loadTextures() {
        bgMains = new TextureRegion[COLORS_QUANTITY];
        bgCorners = new TextureRegion[COLORS_QUANTITY];

        for (int i = 0; i < bgMains.length; i++) {
            bgMains[i] = loadMainBackground("bg" + i);
        }
        for (int i = 0; i < bgMains.length; i++) {
            bgCorners[i] = loadMainBackground("bg" + i + "_corner");
        }

        grayBackground = loadMainBackground("gray");
        grayCorner = loadCornerTexture("gray");
        blackBackground = loadMainBackground("black");
        blackCorner = loadCornerTexture("black");
    }


    TextureRegion getCornerTexture(int groundIndex) {
        if (groundIndex < 0 || groundIndex >= bgCorners.length) {
            return blackCorner;
        }

        return bgCorners[groundIndex];
    }


    public TextureRegion getBackgroundTexture(int groundIndex) {
        if (groundIndex < 0 || groundIndex >= bgCorners.length) {
            return blackBackground;
        }

        return bgMains[groundIndex];
    }


    private TextureRegion loadMainBackground(String name) {
        return GraphicsYio.loadTextureRegion("menu/round_shape/" + name + ".png", false);
    }


    private TextureRegion loadCornerTexture(String name) {
        return GraphicsYio.loadTextureRegion("menu/round_shape/" + name + "_corner.png", true);
    }


    public void renderRoundShape(SpriteBatch argBatch, RectangleYio position, int groundIndex, float cornerRadius) {
        currentBatch = argBatch;
        currentBackground = getBackgroundTexture(groundIndex);
        currentCorner = getCornerTexture(groundIndex);
        cr = cornerRadius / 2;
        this.position = position;

        updatePos1();
        updatePos2();
        updatePos3();
        updateCorners();

        GraphicsYio.drawByRectangle(currentBatch, currentBackground, pos1);
        GraphicsYio.drawByRectangle(currentBatch, currentBackground, pos2);
        GraphicsYio.drawByRectangle(currentBatch, currentBackground, pos3);
        renderCorners();
    }


    public void renderRoundShape(RectangleYio position, int groundIndex, float cornerRadius) {
        renderRoundShape(batch, position, groundIndex, cornerRadius);
    }


    private void renderCorners() {
        for (int i = 0; i < corners.length; i++) {
            GraphicsYio.drawByCircle(
                    currentBatch,
                    currentCorner,
                    corners[i]
            );
        }
    }


    private void updateCorners() {
        corners[0]
                .setRadius(cr)
                .setAngle(0)
                .center.set(position.x + cr, position.y + cr);

        corners[1]
                .setRadius(cr)
                .setAngle(-0.5 * Math.PI)
                .center.set(position.x + cr, position.y + position.height - cr);

        corners[2]
                .setRadius(cr)
                .setAngle(-Math.PI)
                .center.set(position.x + position.width - cr, position.y + position.height - cr);

        corners[3]
                .setRadius(cr)
                .setAngle(-1.5 * Math.PI)
                .center.set(position.x + position.width - cr, position.y + cr);

    }


    private void updatePos3() {
        pos3.x = position.x + 2 * cr;
        pos3.y = position.y + position.height - 2 * cr;
        pos3.width = position.width - 4 * cr;
        pos3.height = 2 * cr;
    }


    private void updatePos2() {
        pos2.x = position.x;
        pos2.y = position.y + 2 * cr;
        pos2.width = position.width;
        pos2.height = position.height - 4 * cr;
    }


    private void updatePos1() {
        pos1.x = position.x + 2 * cr;
        pos1.y = position.y;
        pos1.width = position.width - 4 * cr;
        pos1.height = 2 * cr;
    }


    public void setCurrentBatch(SpriteBatch currentBatch) {
        this.currentBatch = currentBatch;
    }


    public void renderRoundShape(RectangleYio position, int groundIndex) {
        renderRoundShape(position, groundIndex, 0.1f * GraphicsYio.width);
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
