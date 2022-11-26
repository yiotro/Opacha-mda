package yio.tro.opacha.game.view.game_renders.tm_renders;

import yio.tro.opacha.game.touch_modes.TmDefault;
import yio.tro.opacha.game.touch_modes.TouchMode;
import yio.tro.opacha.game.view.game_renders.GameRender;
import yio.tro.opacha.stuff.CircleYio;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.Storage3xTexture;

public class RenderTmDefault extends GameRender{


    private TmDefault tm;
    CircleYio circleYio;


    public RenderTmDefault() {
        circleYio = new CircleYio();
    }


    @Override
    protected void loadTextures() {

    }


    @Override
    public void render() {
        tm = TouchMode.tmDefault;

        //
    }


    @Override
    protected void disposeTextures() {

    }
}
