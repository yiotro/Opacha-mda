package yio.tro.opacha.game.view.game_renders.tm_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.game.touch_modes.TmDeletion;
import yio.tro.opacha.game.touch_modes.TouchMode;
import yio.tro.opacha.game.view.game_renders.GameRender;
import yio.tro.opacha.stuff.GraphicsYio;

public class RenderTmDeletion extends GameRender{

    private TmDeletion tm;
    private TextureRegion selectionTexture;


    @Override
    protected void loadTextures() {
        selectionTexture = GraphicsYio.loadTextureRegion("menu/selection.png", true);
    }


    @Override
    public void render() {
        tm = TouchMode.tmDeletion;

        renderTouchDownSelection();
        renderTouchUpSelection();
        renderLine();
    }


    private void renderTouchUpSelection() {
        if (tm.touchUpPlanet == null) return;

        GraphicsYio.drawByCircle(batchMovable, selectionTexture, tm.touchUpSelectionPosition);
    }


    private void renderLine() {
        if (tm.touchDownPlanet == null) return;

        GraphicsYio.drawLine(batchMovable, getBlackPixel(), tm.touchDownPlanet.position.center, tm.currentTouchPoint, 4 * GraphicsYio.borderThickness);
    }


    private void renderTouchDownSelection() {
        if (tm.touchDownPlanet == null) return;

        GraphicsYio.drawByCircle(batchMovable, selectionTexture, tm.touchDownSelectionPosition);
    }


    @Override
    protected void disposeTextures() {
        selectionTexture.getTexture().dispose();
    }
}
