package yio.tro.opacha.game.loading;

import yio.tro.opacha.Yio;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.GameController;
import yio.tro.opacha.game.GameMode;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.LevelSize;
import yio.tro.opacha.game.loading.loading_processes.*;
import yio.tro.opacha.game.view.GameView;
import yio.tro.opacha.menu.elements.LoadingScreenView;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.scenes.SceneYio;
import yio.tro.opacha.menu.scenes.Scenes;

import java.util.ArrayList;
import java.util.Random;

public class LoadingManager {

    public YioGdxGame yioGdxGame;
    GameController gameController;
    GameView gameView;
    int w, h;
    public boolean working;
    boolean needToForceGameView;
    int currentStep;
    Random random;
    ArrayList<LoadingListener> listeners;
    AbstractLoadingProcess currentProcess;


    public LoadingManager(GameController gameController) {
        this.gameController = gameController;
        yioGdxGame = gameController.yioGdxGame;
        gameView = yioGdxGame.gameView;
        w = gameController.w;
        h = gameController.h;
        working = false;
        needToForceGameView = false;
        currentProcess = null;
        currentStep = -1;
        createListeners();
    }


    public void createListeners() {
        listeners = new ArrayList<>();

        for (SceneYio sceneYio : SceneYio.sceneList) {
            addListener(sceneYio);
        }

        addListener(yioGdxGame.gameView);
    }


    public void applyLoadingScreen(LoadingType type, LoadingParameters loadingParameters) {
        Scenes.loadingScreen.create();

        Scenes.loadingScreen.loadingScreenView.setLoadingType(type);
        Scenes.loadingScreen.loadingScreenView.setLoadingParameters(loadingParameters);
        needToForceGameView = true;
    }


    public void startLoading(LoadingType type, LoadingParameters loadingParameters) {
        Yio.safeSay("Loading level...");
        working = true;
        currentStep = 0;
        currentProcess = null;
        GameRules.initialLoadingType = type;

        switch (type) {
            default:
                currentProcess = new ProcessEmpty(this);
                break;
            case skirmish_create:
                currentProcess = new ProcessSkirmish(this);
                break;
            case campaign_create:
                currentProcess = new ProcessCampaign(this);
                break;
            case editor_create:
                currentProcess = new ProcessEditorCreate(this);
                break;
            case editor_load:
                currentProcess = new ProcessEditorLoad(this);
                break;
            case editor_play:
                currentProcess = new ProcessEditorPlay(this);
                break;
            case tutorial:
                currentProcess = new ProcessTutorial(this);
                break;
            case calendar:
                currentProcess = new ProcessCalendar(this);
                break;
        }

        currentProcess.setLoadingParameters(loadingParameters);
    }


    public void move() {
        switch (currentStep) {
            case 0:
                currentProcess.loadSavedData(currentStep);
                break;
            case 1:
                currentProcess.loadSavedData(currentStep);
                break;
            case 2:
                currentProcess.loadSavedData(currentStep);
                break;
            case 3:
                gameController.defaultValues();
                currentProcess.prepare();
                currentProcess.applyGameRules();
                break;
            case 4:
                beginCreation();
                break;
            case 5:
                currentProcess.generateLevel();
                break;
            case 6:
                gameController.onEndCreation();
                prepareMenu();
                break;
            case 7:
                for (LoadingListener listener : listeners) {
                    listener.onLevelCreationEnd();
                }
                notifyListenersAboutExpensiveStep(currentStep - 7);
                break;
            case 8:
                notifyListenersAboutExpensiveStep(currentStep - 7);
                break;
            case 9:
                notifyListenersAboutExpensiveStep(currentStep - 7);
                break;
            case 10:
                notifyListenersAboutExpensiveStep(currentStep - 7);
                break;
            case 11:
                notifyListenersAboutExpensiveStep(currentStep - 7);
                break;
            case 12:
                endCreation();
                break;
        }

        updateProgress((double) currentStep / 12d);
        currentStep++;
    }


    private void endCreation() {
        yioGdxGame.setGamePaused(false);
        checkToForceGameView();
        yioGdxGame.beginBackgroundChange(GroundIndex.BLACK);
        currentProcess.onEndCreation();
        gameView.updateAnimationTexture();
        working = false;
        debuggingStuff();
    }


    public void startInstantly(LoadingType type, LoadingParameters loadingParameters) {
        startLoading(type, loadingParameters);

        while (working) {
            move();
        }
    }


    private void notifyListenersAboutExpensiveStep(int step) {
        for (LoadingListener listener : listeners) {
            listener.makeExpensiveLoadingStep(step);
        }
    }


    private void checkToForceGameView() {
        if (!needToForceGameView) return;
        needToForceGameView = false;

        gameView.forceAppear();
    }


    public void initGameMode(GameMode gameMode) {
        gameController.setGameMode(gameMode);
    }


    public void initLevelSize(LevelSize levelSize) {
        gameController.initLevelSize(levelSize);
    }


    private void prepareMenu() {
        gameController.createMenuOverlay();
    }


    private void createAndDestroy(SceneYio sceneYio) {
        sceneYio.create();
        sceneYio.destroy();
    }


    private void debuggingStuff() {
        //
    }


    private void beginCreation() {
        gameController.createCamera();
        gameController.createGameObjects();
    }


    public void addListener(LoadingListener listener) {
        if (listeners.contains(listener)) return;

        Yio.addToEndByIterator(listeners, listener);
    }


    public void removeListener(LoadingListener listener) {
        Yio.removeByIterator(listeners, listener);
    }


    public void updateProgress(double progress) {
        LoadingScreenView loadingScreenView = Scenes.loadingScreen.loadingScreenView;
        if (loadingScreenView == null) return;

        loadingScreenView.updateProgress(progress);
    }
}
