package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.stuff.*;

public class RenderRoundBorder extends RenderInterfaceElement{


    private TextureRegion cornerTexture;
    private TextureRegion sideTexture;
    float cr;
    private RectangleYio position;
    PointYio corners[];
    LineYio sides[];


    public RenderRoundBorder() {
        corners = new PointYio[4];
        for (int i = 0; i < corners.length; i++) {
            corners[i] = new PointYio();
        }

        sides = new LineYio[4];
        for (int i = 0; i < sides.length; i++) {
            sides[i] = new LineYio();
        }
    }


    @Override
    public void loadTextures() {
        cornerTexture = GraphicsYio.loadTextureRegion("menu/round_border/border_corner.png", true);
        sideTexture = GraphicsYio.loadTextureRegion("menu/round_border/border_side.png", true);
    }


    public void renderRoundBorder(RectangleYio position, float cornerRadius) {
        this.cr = cornerRadius / 2;
        this.position = position;

        updateCorners();
        renderCorners();

        updateSides();
        renderSides();
    }


    private void renderSides() {
        for (LineYio side : sides) {
            GraphicsYio.drawLine(
                    batch,
                    sideTexture,
                    side
            );
        }
    }


    private void updateSides() {
        sides[0]
                .setStart(position.x + 2 * cr, position.y + cr)
                .setFinish(position.x + position.width - 2 * cr, position.y + cr)
                .setThickness(2 * cr);

        sides[1]
                .setStart(position.x + cr, position.y + position.height - 2 * cr)
                .setFinish(position.x + cr, position.y + 2 * cr)
                .setThickness(2 * cr);

        sides[2]
                .setStart(position.x + position.width - 2 * cr, position.y + position.height - cr)
                .setFinish(position.x + 2 * cr, position.y + position.height - cr)
                .setThickness(2 * cr);

        sides[3]
                .setStart(position.x + position.width - cr, position.y + 2 * cr)
                .setFinish(position.x + position.width - cr, position.y + position.height - 2 * cr)
                .setThickness(2 * cr);
    }


    private void renderCorners() {
        for (int i = 0; i < corners.length; i++) {
            GraphicsYio.drawFromCenterRotated(
                    batch,
                    cornerTexture,
                    corners[i].x,
                    corners[i].y,
                    cr,
                    - (Math.PI / 2) * i
            );
        }
    }


    private void updateCorners() {
        corners[0].set(position.x + cr, position.y + cr);
        corners[1].set(position.x + cr, position.y + position.height - cr);
        corners[2].set(position.x + position.width - cr, position.y + position.height - cr);
        corners[3].set(position.x + position.width - cr, position.y + cr);
    }


    public void renderRoundBorder(RectangleYio position) {
        renderRoundBorder(position, 0.01f * GraphicsYio.width);
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
