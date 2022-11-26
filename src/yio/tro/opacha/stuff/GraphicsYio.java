package yio.tro.opacha.stuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import yio.tro.opacha.Yio;


public class GraphicsYio {

    public static final int QUALITY_HIGH = 2;
    public static final int QUALITY_NORMAL = 1;
    public static final int QUALITY_LOW = 0;

    private static GlyphLayout glyphLayout = new GlyphLayout();
    public static float height = (float) Gdx.graphics.getHeight();
    public static float width = (float) Gdx.graphics.getWidth();
    public static float screenRatio = height / width;
    public static final float borderThickness = Math.max(1, 0.002f * width);
    public static final float prepFactor = (float) (180 / Math.PI);


    public static TextureRegion loadTextureRegion(String name, boolean antialias) {
        Texture texture = new Texture(Gdx.files.internal(name));

        if (antialias) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        TextureRegion region = new TextureRegion(texture);
        return region;
    }


    static public float getTextWidth(BitmapFont font, String text) {
        glyphLayout.setText(font, text);
        return glyphLayout.width;
    }


    static public float getTextHeight(BitmapFont font, String text) {
        glyphLayout.setText(font, text);
        return glyphLayout.height;
    }


    public static void drawFromCenter(SpriteBatch batch, TextureRegion textureRegion, double cx, double cy, double r) {
        batch.draw(textureRegion, (float) (cx - r), (float) (cy - r), (float) (2d * r), (float) (2d * r));
    }


    public static void drawFromCenterRotated(Batch batch, TextureRegion textureRegion, double cx, double cy, double r, double rotationAngle) {
        batch.draw(textureRegion, (float) (cx - r), (float) (cy - r), (float) r, (float) r, (float) (2d * r), (float) (2d * r), 1, 1, prepFactor * (float) rotationAngle);
    }


    public static void drawRectangleRotatedSimple(Batch batch, TextureRegion textureRegion, double x, double y, double width, double height, double angle) {
        batch.draw(textureRegion, (float) x, (float) y, 0, 0, (float) width, (float) height, 1, 1, 57.2957795f * (float) angle);
    }


    public static void drawRectangleRotatedByCenter(Batch batch, TextureRegion textureRegion, double cx, double cy, double horSize, double verSize, double rotationAngle) {
        batch.draw(
                textureRegion,
                (float) (cx - horSize),
                (float) (cy - verSize),
                (float) horSize,
                (float) verSize,
                (float) (2d * horSize),
                (float) (2d * verSize),
                1,
                1,
                57.2957795f * (float) rotationAngle);
    }


    public static void renderBorder(SpriteBatch batch, TextureRegion pixel, RectangleYio viewPos) {
        renderBorder(batch, pixel, viewPos, borderThickness);
    }


    public static void renderBorder(SpriteBatch batch, TextureRegion pixel, RectangleYio viewPos, float thickness) {
        drawLine(
                batch,
                pixel,
                viewPos.x,
                viewPos.y - thickness / 2,
                viewPos.x,
                viewPos.y + viewPos.height + thickness / 2,
                thickness
        );

        drawLine(
                batch,

                pixel,

                viewPos.x,
                viewPos.y,
                viewPos.x + viewPos.width,
                viewPos.y,
                thickness
        );

        drawLine(
                batch,

                pixel,

                viewPos.x,
                viewPos.y + viewPos.height,
                viewPos.x + viewPos.width,
                viewPos.y + viewPos.height,
                thickness
        );

        drawLine(
                batch,

                pixel,

                viewPos.x + viewPos.width,
                viewPos.y - thickness / 2,
                viewPos.x + viewPos.width,
                viewPos.y + viewPos.height + thickness / 2,
                thickness
        );
    }


    public static void drawByCircle(Batch batch, TextureRegion textureRegion, CircleYio circleYio) {
        drawFromCenterRotated(batch, textureRegion, circleYio.center.x, circleYio.center.y, circleYio.radius, circleYio.angle);
    }


    public static void drawByRectangle(Batch batch, TextureRegion textureRegion, RectangleYio rectangleYio) {
        batch.draw(textureRegion, rectangleYio.x, rectangleYio.y, rectangleYio.width, rectangleYio.height);
    }


    public static void drawLine(SpriteBatch spriteBatch, TextureRegion texture, LineYio lineYio) {
        drawLine(spriteBatch, texture, lineYio.start, lineYio.finish, lineYio.thickness);
    }


    public static void drawLine(SpriteBatch spriteBatch, TextureRegion texture, PointYio p1, PointYio p2, double thickness) {
        drawLine(spriteBatch, texture, p1.x, p1.y, p2.x, p2.y, thickness);
    }


    public static double convertToHeight(double width) {
        return width / screenRatio;
    }


    public static double convertToWidth(double height) {
        return height * screenRatio;
    }


    public static void setBatchAlpha(SpriteBatch spriteBatch, double alpha) {
        Color color = spriteBatch.getColor();
        spriteBatch.setColor(color.r, color.g, color.b, (float) alpha);
    }


    public static void setFontAlpha(BitmapFont font, double alpha) {
        Color color = font.getColor();
        font.setColor(color.r, color.g, color.b, (float) alpha);
    }


    public static void renderText(SpriteBatch spriteBatch, RenderableTextYio rText) {
        renderText(spriteBatch, rText.font, rText.string, rText.position);
    }


    public static void renderText(SpriteBatch spriteBatch, BitmapFont font, String text, PointYio position) {
        font.draw(spriteBatch, text, position.x, position.y);
    }


    public static void renderTextOptimized(SpriteBatch spriteBatch, TextureRegion pixel, RenderableTextYio renderableTextYio, float alpha) {
        if (alpha < RenderableTextYio.OPTIMIZATION_CUT_OUT) {
            GraphicsYio.setBatchAlpha(spriteBatch, 0.15 * alpha);
            GraphicsYio.drawByRectangle(spriteBatch, pixel, renderableTextYio.bounds);
            GraphicsYio.setBatchAlpha(spriteBatch, 1);
            return;
        }

        GraphicsYio.setFontAlpha(renderableTextYio.font, alpha);
        GraphicsYio.renderText(spriteBatch, renderableTextYio);
        GraphicsYio.setFontAlpha(renderableTextYio.font, 1);
    }


    public static void drawAmplifiedLine(SpriteBatch spriteBatch, TextureRegion texture, LineYio lineYio, double offset) {
        drawAmplifiedLine(spriteBatch, texture, lineYio.start, lineYio.finish, lineYio.thickness, offset);
    }


    public static void drawAmplifiedLine(SpriteBatch spriteBatch, TextureRegion texture, PointYio p1, PointYio p2, double thickness, double offset) {
        drawAmplifiedLine(spriteBatch, texture, p1.x, p1.y, p2.x, p2.y, thickness, offset);
    }


    public static void drawAmplifiedLine(SpriteBatch spriteBatch, TextureRegion texture, double x1, double y1, double x2, double y2, double thickness, double offset) {
        spriteBatch.draw(
                texture,
                (float) (x1 - offset),
                (float) (y1 - thickness * 0.5),
                (float) offset,
                (float) thickness * 0.5f,
                (float) (Yio.distance(x1, y1, x2, y2) + 2 * offset),
                (float) thickness,
                1f,
                1f,
                (float) (180 / Math.PI * Yio.angle(x1, y1, x2, y2))
        );
    }


    public static void drawLine(SpriteBatch spriteBatch, TextureRegion texture, double x1, double y1, double x2, double y2, double thickness) {
        spriteBatch.draw(
                texture,
                (float) x1,
                (float) (y1 - thickness * 0.5),
                0f,
                (float) thickness * 0.5f,
                (float) Yio.distance(x1, y1, x2, y2),
                (float) thickness,
                1f,
                1f,
                (float) (180 / Math.PI * Yio.angle(x1, y1, x2, y2))
        );
    }
}
