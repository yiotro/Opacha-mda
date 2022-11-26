package yio.tro.opacha;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import yio.tro.opacha.game.GameController;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.tutorial.TutorialManager;
import yio.tro.opacha.game.VelocityManager;
import yio.tro.opacha.game.campaign.CampaignManager;
import yio.tro.opacha.game.debug.DebugFlags;
import yio.tro.opacha.game.loading.LoadingManager;
import yio.tro.opacha.game.view.GameView;
import yio.tro.opacha.game.view.game_renders.GameRendersList;
import yio.tro.opacha.menu.TextFitParser;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.MenuViewYio;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.scenes.SceneYio;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.*;
import yio.tro.opacha.stuff.calendar.CalendarManager;
import yio.tro.opacha.stuff.factor_yio.FactorYio;

import java.util.ArrayList;
import java.util.Random;

public class YioGdxGame extends ApplicationAdapter implements InputProcessor {

    public static long appLaunchTime, initialLoadingTime;
    public static PlatformType platformType;

    boolean alreadyShownErrorMessageOnce;
    boolean ignoreDrag, loadedResources;
    public boolean gamePaused, readyToUnPause, splashVisible, startedExitProcess;
    static boolean screenVeryNarrow;

    public int w, h;
    int frameSkipCount, splashCount, currentBackgroundIndex;

    long lastTimeButtonPressed, timeToUnPause;
    public float pressX, pressY, backgroundHeight, splashOffset;

    public GameController gameController;
    public GameView gameView;
    public SpriteBatch batch;
    public MenuControllerYio menuControllerYio;
    public MenuViewYio menuViewYio;
    TextureRegion defBackground;
    TextureRegion splashTexture, currentBackground, lastBackground;
    FrameBuffer frameBuffer;
    FactorYio transitionFactor, splashFactor;
    public static Random random = new Random();
    public static Random predictableRandom = new Random();
    public LoadingManager loadingManager;
    public OnKeyReactions onKeyReactions;
    ArrayList<TouchListenerYio> touchListeners;
    public MovableController movableController;
    public boolean slowMo;
    public Stage stage; // for keyboard input
    public InputMultiplexer inputMultiplexer;
    public TutorialManager tutorialManager;


    @Override
    public void create() {
        loadedResources = false;
        batch = new SpriteBatch();
        splashCount = 0;
        loadTextures();
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        GraphicsYio.width = (float) Gdx.graphics.getWidth();
        GraphicsYio.height = (float) Gdx.graphics.getHeight();
        pressX = 0.5f * w;
        pressY = 0.5f * h;
        frameSkipCount = 50; // >= 2
        movableController = new MovableController();
        frameBuffer = FrameBufferYio.getInstance(Pixmap.Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        touchListeners = new ArrayList<>();
        tutorialManager = new TutorialManager(this);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        slowMo = false;
        stage = new Stage();
    }


    private void loadTextures() {
        splashTexture = GraphicsYio.loadTextureRegion("menu/splash.png", true);
    }


    void generalInitialization() {
        TimeMeasureYio.begin();
        appLaunchTime = System.currentTimeMillis();
        loadedResources = true;
        backgroundHeight = 1.666666f * w;
        screenVeryNarrow = (h > backgroundHeight + 2);
        if (screenVeryNarrow) {
            say("screen is very narrow");
        }
        loadBackgroundTextures();

        transitionFactor = new FactorYio();

        initSplash();
        gamePaused = true;
        alreadyShownErrorMessageOnce = false;
        startedExitProcess = false;

        SceneYio.onGeneralInitialization(); // before menu controller
        initializeSingletons();
        SettingsManager.getInstance().loadValues();
        Fonts.initFonts(); // causes main delay on launch
        menuControllerYio = new MenuControllerYio(this);
        menuViewYio = new MenuViewYio(this);
        GameRules.bootInit();
        gameController = new GameController(this); // must be called after menu controller is created. (maybe not)
        gameView = new GameView(this);
        gameView.appearFactor.destroy(1, 1);
        currentBackgroundIndex = -1;
        currentBackground = gameView.blackPixel; // call this after game view is created
        beginBackgroundChange(GroundIndex.MAIN_MENU);
        loadingManager = new LoadingManager(gameController);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        SoundManager.loadSounds();
        onKeyReactions = new OnKeyReactions(this);

        loadSavedInfo();
        initialLoadingTime = TimeMeasureYio.apply("Initial loading time");
    }


    private void initializeSingletons() {
        TextFitParser.initialize();
        LanguagesManager.initialize();
        OneTimeInfo.initialize();
        SettingsManager.initialize();
        GameRendersList.initialize();
        LogCollectorYio.initialize();
        FastXorRandomYio.initialize();
        CampaignManager.initialize();
        AdaptiveDifficultyManager.initialize();
        VelocityManager.initialize();
        GradualAttractionManager.initialize();
        CalendarManager.initialize();
        CalendarManager.getInstance().loadValues();
        StoreLinksYio.initialize();
    }


    private void loadSavedInfo() {

    }


    private void initSplash() {
        splashVisible = true;
        splashFactor = new FactorYio();
        splashFactor.setValues(0, 0);
        splashFactor.appear(1, 3);
        splashOffset = 0.2f * w;
    }


    private void loadBackgroundTextures() {
        defBackground = GraphicsYio.loadTextureRegion("menu/def_background.png", true);
    }


    public void setGamePaused(boolean gamePaused) {
        if (gamePaused && !this.gamePaused) { // actions when paused
            this.gamePaused = true;
            onPaused();
        } else if (!gamePaused && this.gamePaused) { // actions when unpaused
            unPauseAfterSomeTime();
            onUnPaused();
        }
    }


    private void onUnPaused() {
        beginBackgroundChange(GroundIndex.BLACK);
        Fonts.smallFont.setColor(Color.BLACK);
    }


    private void onPaused() {
        beginBackgroundChange(1);
        Fonts.smallFont.setColor(Color.BLACK);
        menuControllerYio.forceDyingElementsToEnd();
    }


    public void beginBackgroundChange(int index) {
        if (currentBackgroundIndex == index && index == 4) return;

        currentBackgroundIndex = index;
        lastBackground = currentBackground;
        switch (index) {
            default:
                currentBackground = defBackground;
                break;
            case GroundIndex.BLACK:
                currentBackground = gameView.blackPixel;
                break;
        }

        transitionFactor.setValues(0.02, 0.01);
        transitionFactor.appear(3, 1);
    }


    public void move() {
        if (!loadedResources) return;

        checkToMoveSplashScreen();

        if (loadingManager.working) {
            loadingManager.move();
            return;
        }

        checkForSlowMo();
        transitionFactor.move();
        movableController.move();
        RefreshRateDetector.getInstance().move();
        checkToUnPause();

        gameView.moveFactors();

        moveInternalGameStuff();

        menuControllerYio.move();
        if (loadingManager.working) return; // immediately after button press

        stage.act();
    }


    private void moveInternalGameStuff() {
        if (!loadedResources) return;
        if (gamePaused) return;

        gameView.moveInsideStuff();
        gameController.move();
    }


    private void checkToMoveSplashScreen() {
        if (!splashVisible) return;
        moveSplash();
    }


    private void checkForSlowMo() {
        if (!slowMo) return;

        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void checkToUnPause() {
        if (readyToUnPause && System.currentTimeMillis() > timeToUnPause && gameView.coversAllScreen()) {
            gamePaused = false;
            readyToUnPause = false;
            gameController.currentTouchCount = 0;
            frameSkipCount = 10; // >= 2
        }
    }


    private void moveSplash() {
        splashFactor.move();

        if (splashFactor.get() == 1) {
            splashVisible = false;
        }
    }


    private void drawBackground(TextureRegion textureRegion) {
        batch.begin();
        batch.draw(textureRegion, 0, 0, w, h);
//        if (isScreenVeryNarrow()) batch.draw(textureRegion, 0, 0.985f * backgroundHeight, w, backgroundHeight);
        batch.end();
    }


    private void renderMenuLayersWhenNothingIsMoving() { // when transitionFactor.get() == 1
        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, 1);
        drawBackground(currentBackground);
    }


    private void renderMenuLayersWhenUsualAnimation() {
        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, 1);
        drawBackground(lastBackground);

        float f = (0 + transitionFactor.get());
        batch.setColor(c.r, c.g, c.b, f);
        drawBackground(currentBackground);
        batch.setColor(c.r, c.g, c.b, 1);
    }


    private void renderMenuLayers() {
        if (transitionFactor.get() == 1) {
            renderMenuLayersWhenNothingIsMoving();
            return;
        }


        renderMenuLayersWhenUsualAnimation();
    }


    public void renderInternals() {
        if (!loadedResources) return;

        if (!gameView.coversAllScreen()) {
            renderMenuLayers();
        }

        menuViewYio.renderAll(false);
        gameView.render();
        menuViewYio.renderAll(true);

        renderDebug();
    }


    private void renderDebug() {
        if (!DebugFlags.showFps) return;

        batch.begin();

        Fonts.smallFont.draw(
                batch,
                getCurrentFpsString(),
                0.07f * w, Gdx.graphics.getHeight() - 10
        );

        batch.end();
    }


    private String getCurrentFpsString() {
        return Gdx.graphics.getFramesPerSecond() + " [" + RefreshRateDetector.getInstance().refreshRate + "]";
    }


    private void renderSplashScreen() {
        batch.begin();
        int splashHeight = (int) (1.666666 * w);
        int delta = splashHeight - h;
        batch.draw(splashTexture, 0, -delta / 2, w, splashHeight);
        batch.end();
    }


    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!loadedResources) {
            renderSplashScreen();
            if (splashCount == 2) generalInitialization();
            splashCount++;
            return;
        }

        tryToMove();

        if (gamePaused || slowMo) {
            renderInternals();
        } else {
            if (Gdx.graphics.getDeltaTime() < 0.025 || frameSkipCount >= 2) {
                frameSkipCount = 0;
                frameBuffer.begin();
                renderInternals();
                frameBuffer.end();
            } else {
                frameSkipCount++;
            }
            batch.begin();
            batch.draw(frameBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, true);
            batch.end();
        }

        if (splashVisible) {
            renderSplash();
        }

        stage.draw();
    }


    private void renderSplash() {
        batch.begin();
        Color c = batch.getColor();
        float a = c.a;
        batch.setColor(c.r, c.g, c.b, 1 - splashFactor.get());
        float off = splashOffset * splashFactor.get();
        batch.draw(splashTexture, 0, 0, w, h);
        batch.setColor(c.r, c.g, c.b, a);
        batch.end();
    }


    private void tryToMove() {
        try {
            move();
        } catch (Exception exception) {
            if (!alreadyShownErrorMessageOnce) {
                exception.printStackTrace();
                alreadyShownErrorMessageOnce = true;
                onCatchedException(exception);
            }
        }
    }


    public void addTouchListener(TouchListenerYio touchListenerYio) {
        Yio.addByIterator(touchListeners, touchListenerYio);
    }


    void unPauseAfterSomeTime() {
        readyToUnPause = true;
        timeToUnPause = System.currentTimeMillis() + 100;
    }


    public static boolean isScreenNonStandard() {
        float ratio = (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        return ratio < 1.51 || ratio > 1.7;
    }


    public static boolean isScreenVeryNarrow() {
        return screenVeryNarrow;
    }


    public void forceBackgroundChange() {
        transitionFactor.setValues(1, 0);
    }


    public static void say(String text) {
        System.out.println(text);
    }


    @Override
    public void pause() {
        super.pause();

        if (startedExitProcess) return;

        if (gameView != null) {
            gameView.onPause();
        }

        if (gameController != null) {
            gameController.onPause();
        }

        if (menuControllerYio != null) {
            menuControllerYio.onPause();
        }
    }


    @Override
    public void resume() {
        super.resume();

        if (startedExitProcess) return;

        if (gameView != null) {
            gameView.onResume();
        }

        if (gameController != null) {
            gameController.onResume();
        }

        if (menuControllerYio != null) {
            menuControllerYio.onResume();
        }
    }


    @Override
    public boolean keyDown(int keycode) {
        if (splashFactor.get() < 1) return true;

        try {
            onKeyReactions.keyDown(keycode);
        } catch (Exception exception) {
            if (!alreadyShownErrorMessageOnce) {
                exception.printStackTrace();
                alreadyShownErrorMessageOnce = true;
                onCatchedException(exception);
            }
        }

        return true;
    }


    @Override
    public boolean keyUp(int keycode) {

        return false;
    }


    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    public boolean isGamePaused() {
        return gamePaused;
    }


    private void onCatchedException(Exception exception) {
        gameView.destroy();
        setGamePaused(true);
        loadingManager.working = false;

        Scenes.catchedException.setException(exception);
        Scenes.catchedException.create();
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        ignoreDrag = true;
        pressX = screenX;
        pressY = h - screenY;
//        System.out.println("Screen touch: " + Yio.roundUp(pressX / w, 4) + " " + Yio.roundUp(pressY / h, 4));
        try {
            if (touchDownReaction(screenX, screenY, pointer, button)) return false;
        } catch (Exception exception) {
            if (!alreadyShownErrorMessageOnce) {
                exception.printStackTrace();
                alreadyShownErrorMessageOnce = true;
                onCatchedException(exception);
            }
        }
        return false;
    }


    private boolean touchDownReaction(int screenX, int screenY, int pointer, int button) {
        for (TouchListenerYio touchListener : touchListeners) {
            if (touchListener.isActive() && touchListener.touchDown(screenX, h - screenY)) return true;
        }

        if (!gameView.isInMotion() && transitionFactor.get() > 0.99 && menuControllerYio.touchDown(screenX, h - screenY, pointer, button)) {
            lastTimeButtonPressed = System.currentTimeMillis();
            return true;
        } else {
            ignoreDrag = false;
        }

        if (!gamePaused) {
            gameController.touchDown(screenX, Gdx.graphics.getHeight() - screenY, pointer, button);
        }

        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        try {
            touchUpReaction(screenX, screenY, pointer, button);
        } catch (Exception exception) {
            if (!alreadyShownErrorMessageOnce) {
                exception.printStackTrace();
                alreadyShownErrorMessageOnce = true;
                onCatchedException(exception);
            }
        }
        return false;
    }


    private void touchUpReaction(int screenX, int screenY, int pointer, int button) {
        for (TouchListenerYio touchListener : touchListeners) {
            if (touchListener.isActive() && touchListener.touchUp(screenX, h - screenY)) return;
        }

        if (menuControllerYio.touchUp(screenX, h - screenY, pointer, button)) return;

        if (!gamePaused && gameView.coversAllScreen()) {
            gameController.touchUp(screenX, h - screenY, pointer, button);
        }
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        try {
            touchDragReaction(screenX, screenY, pointer);
        } catch (Exception exception) {
            if (!alreadyShownErrorMessageOnce) {
                exception.printStackTrace();
                alreadyShownErrorMessageOnce = true;
                onCatchedException(exception);
            }
        }

        return false;
    }


    private boolean touchDragReaction(int screenX, int screenY, int pointer) {
        for (TouchListenerYio touchListener : touchListeners) {
            if (touchListener.isActive() && touchListener.touchDrag(screenX, h - screenY)) return false;
        }

        menuControllerYio.touchDragged(screenX, h - screenY, pointer);

        if (!ignoreDrag && !gamePaused && gameView.coversAllScreen()) {
            gameController.touchDragged(screenX, h - screenY, pointer);
        }

        return false;
    }


    @Override
    public boolean scrolled(float amountX, float amountY) {
        int amount = (int) amountY;
        if (menuControllerYio.onMouseWheelScrolled(amount)) return true;
        gameController.onMouseWheelScrolled(amount);
        return true;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }


    public void exitApp() {
        startedExitProcess = true;
        TextFitParser instance = TextFitParser.getInstance();
        instance.disposeAllTextures();
        instance.killInstance();

        Gdx.app.exit();
    }


}
