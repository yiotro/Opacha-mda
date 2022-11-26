package yio.tro.opacha.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import yio.tro.opacha.SettingsManager;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.GameController;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.LevelSize;
import yio.tro.opacha.game.debug.DebugFlags;
import yio.tro.opacha.game.loading.LoadingListener;
import yio.tro.opacha.game.touch_modes.TouchMode;
import yio.tro.opacha.game.view.game_renders.GameRender;
import yio.tro.opacha.game.view.game_renders.GameRendersList;
import yio.tro.opacha.stuff.*;
import yio.tro.opacha.stuff.factor_yio.FactorYio;

public class GameView implements LoadingListener{

    YioGdxGame yioGdxGame;
    public GameController gameController;

    public int w, h;

    public FactorYio appearFactor;
    FrameBuffer frameBuffer;
    public SpriteBatch batchMovable, batchSolid;
    public OrthographicCamera orthoCam;

    TextureRegion transitionFrameTexture;
    public TextureRegion blackPixel, backgroundPixel;
    public AtlasLoader atlasLoader;
    double zoomLevelOne, zoomLevelTwo;
    public int currentZoomQuality;
    public RectangleYio transitionFramePosition;
    private RectangleYio screenPosition;
    public TextureRegion darkPixel;


    public GameView(YioGdxGame yioGdxGame) { //must be called after creation of GameController and MenuView
        this.yioGdxGame = yioGdxGame;
        gameController = yioGdxGame.gameController;
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        appearFactor = new FactorYio();
        frameBuffer = FrameBufferYio.getInstance(Pixmap.Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Texture texture = frameBuffer.getColorBufferTexture();
        transitionFrameTexture = new TextureRegion(texture);
        transitionFrameTexture.flip(false, true);
        batchMovable = new SpriteBatch();
        batchSolid = yioGdxGame.batch;
        createOrthoCam();
        loadTextures();
        createAtlas();
        GameRendersList.getInstance().updateGameRenders(this);
        transitionFramePosition = new RectangleYio();
        screenPosition = new RectangleYio(0, 0, GraphicsYio.width, GraphicsYio.height);
    }


    private void createAtlas() {
        String path = "game/atlas/";
        if (SettingsManager.getInstance().colorblindMode) {
            path = "game/colorblind_atlas/";
        }
        atlasLoader = new AtlasLoader(path + "atlas_texture.png", path + "atlas_structure.txt", true);
    }


    public void createOrthoCam() {
        orthoCam = new OrthographicCamera(yioGdxGame.w, yioGdxGame.h);
        orthoCam.position.set(orthoCam.viewportWidth / 2f, orthoCam.viewportHeight / 2f, 0);
        updateCam();
    }


    void loadTextures() {
        blackPixel = GraphicsYio.loadTextureRegion("pixels/black.png", false);
        backgroundPixel = GraphicsYio.loadTextureRegion(getBackgroundPath(), false);
        darkPixel = GraphicsYio.loadTextureRegion("pixels/dark.png", false);
    }


    private String getBackgroundPath() {
        if (SettingsManager.getInstance().colorblindMode) {
            return "game/background/colorblind.png";
        }
        return "game/background/main.png";
    }


    public void updateCam() {
        orthoCam.update();
        batchMovable.setProjectionMatrix(orthoCam.combined);
    }


    public void defaultValues() {
        currentZoomQuality = GraphicsYio.QUALITY_NORMAL;
    }


    public void appear() {
        appearFactor.setValues(0.02, 0);
        appearFactor.appear(6, 1.7); // 3, 1
        updateAnimationTexture();
    }


    public void forceAppear() {
        appearFactor.setValues(1, 0);
        appearFactor.stop();
    }


    public void destroy() {
        if (appearFactor.get() == 0) return;

        appearFactor.destroy(1, 5);

        updateAnimationTexture();
    }


    public void updateAnimationTexture() {
        frameBuffer.begin();
        batchSolid.begin();
        batchSolid.draw(blackPixel, 0, 0, w, h);
        batchSolid.end();
        renderInternals();
        frameBuffer.end();
    }


    public void onPause() {
        atlasLoader.atlasRegion.getTexture().dispose();
        GameRender.disposeAllTextures();
    }


    public void onResume() {
        createAtlas();
        GameRendersList.getInstance().updateGameRenders(this);
        GameRender.updateAllTextures();
    }


    public void renderInternals() {
        GameRendersList instance = GameRendersList.getInstance();

        renderBackground();
        batchMovable.begin();

        instance.renderStars.render();
        instance.renderParticles.render();
        instance.renderLinks.renderShadows();
        instance.renderPlanets.renderShadows();
        instance.renderLinks.render();
        instance.renderUnits.render();
        instance.renderPlanets.render();
        instance.renderFillers.render();

        renderDyingTouchModes();
        renderCurrentTouchMode();
        batchMovable.end();
    }


    private void renderDyingTouchModes() {
        for (TouchMode dyingTm : gameController.dyingTms) {
            GameRender render = dyingTm.getRender();
            if (render == null) return;

            render.render();
        }
    }


    private void renderCurrentTouchMode() {
        GameRender render = gameController.touchMode.getRender();
        if (render == null) return;

        render.render();
    }


    private void renderBackground() {
        batchMovable.begin();
        TextureRegion backgroundPixel = this.backgroundPixel;
        batchMovable.draw(backgroundPixel, 0, 0, gameController.boundWidth, gameController.boundHeight);
        batchMovable.end();
    }


    public void render() {
        if (appearFactor.get() < 0.01) {
            return;
        } else if (appearFactor.get() < 1) {
            renderTransitionFrame();
        } else {
            checkForSolidBlackBackground();
            renderInternals();
        }
    }


    private void checkForSolidBlackBackground() {
        if (!gameController.backgroundVisible) return;

        batchSolid.begin();
        batchSolid.draw(blackPixel, 0, 0, w, h);
        batchSolid.end();
    }


    void renderTransitionFrame() {
        transitionFramePosition.set(0, 0, GraphicsYio.width, GraphicsYio.height);
        transitionFramePosition.x += (1 - appearFactor.get()) * GraphicsYio.height;

        batchSolid.begin();

        if (appearFactor.isInAppearState()) {
            Color c = batchSolid.getColor();
            float a = c.a;
            batchSolid.setColor(c.r, c.g, c.b, Math.min(appearFactor.get() * 2, 1));
            GraphicsYio.drawByRectangle(batchSolid, darkPixel, screenPosition);
            batchSolid.setColor(c.r, c.g, c.b, a);
            GraphicsYio.drawByRectangle(batchSolid, transitionFrameTexture, transitionFramePosition);
        } else {
            Color c = batchSolid.getColor();
            float a = c.a;
            batchSolid.setColor(c.r, c.g, c.b, appearFactor.get());
            GraphicsYio.drawByRectangle(batchSolid, transitionFrameTexture, transitionFramePosition);
            batchSolid.setColor(c.r, c.g, c.b, a);
        }

        batchSolid.end();
    }


    public void moveFactors() {
        appearFactor.move();
    }


    public boolean coversAllScreen() {
        return appearFactor.get() == 1;
    }


    public boolean isInMotion() {
        return appearFactor.get() > 0 && appearFactor.get() < 1;
    }


    public void moveInsideStuff() {
        if (gameController.getTrackerZoom() < zoomLevelOne) {
            currentZoomQuality = GraphicsYio.QUALITY_HIGH;
        } else if (gameController.getTrackerZoom() < zoomLevelTwo) {
            currentZoomQuality = GraphicsYio.QUALITY_NORMAL;
        } else {
            currentZoomQuality = GraphicsYio.QUALITY_LOW;
        }
    }


    public void setZoomLevels(double zoomValues[][], LevelSize levelsSize) {
        int lsIndex = levelsSize.ordinal();

        if (lsIndex >= zoomValues.length) {
            lsIndex = zoomValues.length - 1;
        }

        zoomLevelOne = zoomValues[lsIndex][0];
        zoomLevelTwo = zoomValues[lsIndex][1];
    }


    @Override
    public void onLevelCreationEnd() {
        defaultValues();

        setZoomLevels(gameController.cameraController.getZoomValues(),
                gameController.initialLevelSize);
    }


    @Override
    public void makeExpensiveLoadingStep(int step) {
        if (step == 0) {
            appear();
        }
    }
}
