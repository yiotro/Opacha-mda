package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.tutorial.ForefingerElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RectangleYio;

public class RenderForefinger extends RenderInterfaceElement{

    private TextureRegion main;
    private ForefingerElement forefinger;
    private float alpha;
    private RectangleYio viewPosition;
    private TextureRegion selection;
    private TextureRegion debugPixel;
    private TextureRegion effectTexture;
    private float ef;
    private TextureRegion blackout;


    @Override
    public void loadTextures() {
        main = GraphicsYio.loadTextureRegion("menu/forefinger/forefinger.png", true);
        selection = GraphicsYio.loadTextureRegion("menu/forefinger/selection.png", true);
        debugPixel = GraphicsYio.loadTextureRegion("pixels/red.png", false);
        effectTexture = GraphicsYio.loadTextureRegion("menu/forefinger/effect.png", false);
        blackout = GraphicsYio.loadTextureRegion("menu/forefinger/forefinger_blackout.png", false);
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        forefinger = (ForefingerElement) element;
        alpha = forefinger.getAlpha();
        viewPosition = forefinger.getViewPosition();

        renderEffect();
        renderMain();
        renderSelection();
        renderBlackout();

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderBlackout() {
        if (forefinger.blackoutFactor.get() == 0) return;

        GraphicsYio.setBatchAlpha(batch, 0.25f * forefinger.blackoutFactor.get() * alpha);

        GraphicsYio.drawByRectangle(
                batch,
                blackout,
                forefinger.blackoutPosition
        );

        for (RectangleYio blackoutBorder : forefinger.blackoutBorders) {
            GraphicsYio.drawByRectangle(
                    batch,
                    blackPixel,
                    blackoutBorder
            );
        }
    }


    private void renderEffect() {
        ef = forefinger.effectFactor.get();
        if (ef == 0) return;

        GraphicsYio.setBatchAlpha(batch, 0.45f * (1 - ef));

        GraphicsYio.drawFromCenter(
                batch,
                effectTexture,
                forefinger.hook.x,
                forefinger.hook.y,
                forefinger.effectRadius
        );
    }


    private void renderSelection() {
        if (!forefinger.isSelected()) return;

        GraphicsYio.setBatchAlpha(batch, forefinger.selectionFactor.get() * alpha);

        GraphicsYio.drawFromCenterRotated(
                batch,
                selection,
                forefinger.viewPoint.x,
                forefinger.viewPoint.y,
                forefinger.viewRadius,
                forefinger.viewAngle - Math.PI / 2
        );

        GraphicsYio.setBatchAlpha(batch, alpha);
    }


    private void renderHook() {
        GraphicsYio.setBatchAlpha(batch, 0.4 * alpha);

        GraphicsYio.drawFromCenter(
                batch,
                debugPixel,
                forefinger.hook.x,
                forefinger.hook.y,
                0.005f * GraphicsYio.width
        );

        GraphicsYio.setBatchAlpha(batch, alpha);
    }


    private void renderMain() {
        GraphicsYio.setBatchAlpha(batch, alpha);

        GraphicsYio.drawFromCenterRotated(
                batch,
                main,
                forefinger.viewPoint.x,
                forefinger.viewPoint.y,
                forefinger.viewRadius,
                forefinger.viewAngle - Math.PI / 2
        );
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
