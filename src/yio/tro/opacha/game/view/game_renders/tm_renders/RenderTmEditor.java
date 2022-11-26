package yio.tro.opacha.game.view.game_renders.tm_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.game.touch_modes.TmEditor;
import yio.tro.opacha.game.touch_modes.TouchMode;
import yio.tro.opacha.game.view.game_renders.GameRender;
import yio.tro.opacha.stuff.GraphicsYio;

public class RenderTmEditor extends GameRender{

    private TmEditor tm;
    private TextureRegion selectionTexture;


    @Override
    protected void loadTextures() {
        selectionTexture = GraphicsYio.loadTextureRegion("menu/selection.png", true);
    }


    @Override
    public void render() {
        tm = TouchMode.tmEditor;

        renderSelection();
    }


    private void renderSelection() {
        if (tm.selectedPlanet == null) return;

        GraphicsYio.drawByCircle(batchMovable, selectionTexture, tm.selectionPosition);
    }


    @Override
    protected void disposeTextures() {
        selectionTexture.getTexture().dispose();
    }
}
