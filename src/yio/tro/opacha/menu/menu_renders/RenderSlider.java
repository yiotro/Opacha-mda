package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.slider.SliderYio;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RectangleYio;
import yio.tro.opacha.stuff.RenderableTextYio;

public class RenderSlider extends RenderInterfaceElement {

    TextureRegion blackCircle, accentPixel, untouchableValue, untouchablePixel;
    float sliderLineHeight, sliderLineHeightHalved;
    private SliderYio sliderYio;
    private RectangleYio viewPosition;
    private float f;


    public RenderSlider() {
        sliderLineHeight = 0.007f * Gdx.graphics.getWidth();
        sliderLineHeightHalved = sliderLineHeight / 2;
    }


    @Override
    public void loadTextures() {
        blackCircle = loadSliderTexture("black_circle");
        accentPixel = GraphicsYio.loadTextureRegion("pixels/slider_accent.png", false);
        untouchableValue = loadSliderTexture("untouchable_slider_value");
        untouchablePixel = GraphicsYio.loadTextureRegion("pixels/blue.png", false);
    }


    private TextureRegion loadSliderTexture(String name) {
        return GraphicsYio.loadTextureRegion("menu/slider/" + name + ".png", true);
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        renderSlider((SliderYio) element);
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }


    void renderSlider(SliderYio slider) {
        sliderYio = slider;
        viewPosition = slider.getViewPosition();
        f = sliderYio.getAlpha();

        checkToChangeBatchAlpha();

        renderBlackLine();
        renderAccent();
        renderSegments();
        renderValueCircle();
        renderText();

        // used only for debug
//        renderBorder();

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderAccent() {
        if (!sliderYio.isAccentVisible()) return;
        batch.draw(getAccentPixel(
                sliderYio),
                viewPosition.x,
                viewPosition.y - sliderLineHeightHalved,
                sliderYio.runnerValue * viewPosition.width,
                sliderLineHeight);
    }


    private TextureRegion getAccentPixel(SliderYio sliderYio) {
        if (sliderYio.isTouchable()) {
            return accentPixel;
        } else {
            return untouchablePixel;
        }
    }


    private void renderBorder() {
        GraphicsYio.setBatchAlpha(batch, 1);
        GraphicsYio.renderBorder(batch, getGameView().blackPixel, sliderYio.getTouchRectangle());
    }


    private void renderText() {
        if (f < 0.5) return;

        if (f < RenderableTextYio.OPTIMIZATION_CUT_OUT) {
            GraphicsYio.setBatchAlpha(batch, 0.15 * f);
            GraphicsYio.drawByRectangle(batch, blackPixel, sliderYio.valueText.bounds);
            GraphicsYio.drawByRectangle(batch, blackPixel, sliderYio.title.bounds);
            GraphicsYio.setBatchAlpha(batch, 1);
            return;
        }

        GraphicsYio.setFontAlpha(sliderYio.valueText.font, f * f);
        GraphicsYio.setFontAlpha(sliderYio.title.font, f * f);

        GraphicsYio.renderText(batch, sliderYio.valueText);
        GraphicsYio.renderText(batch, sliderYio.title);

        GraphicsYio.setFontAlpha(sliderYio.valueText.font, 1);
        GraphicsYio.setFontAlpha(sliderYio.title.font, 1);
    }


    private void renderValueCircle() {
        GraphicsYio.drawFromCenter(
                batch,
                getValueCircle(sliderYio),
                sliderYio.getRunnerValueViewX(),
                viewPosition.y,
                sliderYio.circleSize);
    }


    private TextureRegion getValueCircle(SliderYio sliderYio) {
        if (!sliderYio.isTouchable()) {
            return untouchableValue;
        }

        return blackCircle;
    }


    private void checkToChangeBatchAlpha() {
        if (f == 1) return;
        batch.setColor(c.r, c.g, c.b, f * f);
    }


    private void renderSegments() {
        if (!sliderYio.isSegmentsVisible()) {
            GraphicsYio.drawFromCenter(
                    batch,
                    blackCircle,
                    viewPosition.x,
                    viewPosition.y,
                    sliderYio.getSegmentCircleSize());
            GraphicsYio.drawFromCenter(
                    batch,
                    blackCircle,
                    viewPosition.x + viewPosition.width,
                    viewPosition.y,
                    sliderYio.getSegmentCircleSize());
        } else {
            for (int i = 0; i < sliderYio.numberOfSegments + 1; i++) {
                GraphicsYio.drawFromCenter(
                        batch,
                        blackCircle,
                        sliderYio.getSegmentLeftSidePos(i),
                        viewPosition.y,
                        sliderYio.getSegmentCircleSize());
            }
        }
    }


    private void renderBlackLine() {
        batch.draw(
                blackPixel,
                viewPosition.x,
                viewPosition.y - sliderLineHeightHalved,
                viewPosition.width,
                sliderLineHeight);
    }
}
