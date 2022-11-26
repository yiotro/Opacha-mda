package yio.tro.opacha.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.SettingsManager;
import yio.tro.opacha.game.GameController;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.game.view.GameView;
import yio.tro.opacha.stuff.AtlasLoader;
import yio.tro.opacha.stuff.Storage3xTexture;

public abstract class GameRender {

    protected GameView gameView;
    protected GameController gameController;
    protected SpriteBatch batchMovable, batchSolid;
    float w, h;
    protected AtlasLoader atlasLoader;


    public GameRender() {
        GameRendersList.getInstance().gameRenders.listIterator().add(this);
    }


    void update(GameView gameView) {
        this.gameView = gameView;
        gameController = gameView.gameController;
        batchMovable = gameView.batchMovable;
        batchSolid = gameView.batchSolid;
        w = gameView.w;
        h = gameView.h;
        atlasLoader = gameView.atlasLoader;

        loadTextures();
    }


    protected Storage3xTexture load3xTexture(String name) {
        return new Storage3xTexture(atlasLoader, name + ".png");
    }


    public static void updateAllTextures() {
        for (GameRender gameRender : GameRendersList.getInstance().gameRenders) {
            gameRender.loadTextures();
        }
    }


    public static void disposeAllTextures() {
        for (GameRender gameRender : GameRendersList.getInstance().gameRenders) {
            gameRender.disposeTextures();
        }
    }


    protected abstract void loadTextures();


    public abstract void render();


    protected abstract void disposeTextures();


    protected int getCurrentZoomQuality() {
        return gameView.currentZoomQuality;
    }


    public ObjectsLayer getObjectsLayer() {
        return gameController.objectsLayer;
    }


    public TextureRegion getBlackPixel() {
        return gameView.blackPixel;
    }
}
