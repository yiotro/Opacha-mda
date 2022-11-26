package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.game.view.GameView;
import yio.tro.opacha.menu.MenuViewYio;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RectangleYio;

public abstract class RenderInterfaceElement {

    protected MenuViewYio menuViewYio;
    protected SpriteBatch batch;
    protected Color c;
    public int w, h;
    protected TextureRegion blackPixel;


    public RenderInterfaceElement() {
        MenuRenders.list.listIterator().add(this);
    }


    void update(MenuViewYio menuViewYio) {
        this.menuViewYio = menuViewYio;
        batch = menuViewYio.batch;
        c = batch.getColor();
        w = menuViewYio.w;
        h = menuViewYio.h;
        blackPixel = GraphicsYio.loadTextureRegion("pixels/black.png", false);
        loadTextures();
    }


    public abstract void loadTextures();


    public abstract void renderFirstLayer(InterfaceElement element);


    public abstract void renderSecondLayer(InterfaceElement element);


    public abstract void renderThirdLayer(InterfaceElement element);


    protected void renderShadow(RectangleYio rectangle) {
        MenuRenders.renderShadow.renderShadow(rectangle);
    }


    protected void renderDebug(InterfaceElement interfaceElement) {
        GraphicsYio.renderBorder(batch, blackPixel, interfaceElement.getViewPosition(), 0.01f * GraphicsYio.width);
    }


    public GameView getGameView() {
        return menuViewYio.yioGdxGame.gameView;
    }
}
