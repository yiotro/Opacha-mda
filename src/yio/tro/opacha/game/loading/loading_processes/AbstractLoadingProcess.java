package yio.tro.opacha.game.loading.loading_processes;

import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.GameController;
import yio.tro.opacha.game.GameMode;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.LevelSize;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.game.loading.LoadingManager;
import yio.tro.opacha.game.loading.LoadingParameters;

public abstract class AbstractLoadingProcess {


    YioGdxGame yioGdxGame;
    GameController gameController;
    LoadingManager loadingManager;
    LoadingParameters loadingParameters;


    public AbstractLoadingProcess(LoadingManager loadingManager) {
        this.loadingManager = loadingManager;
        yioGdxGame = loadingManager.yioGdxGame;
        gameController = yioGdxGame.gameController;

        loadingParameters = null;
    }


    public abstract void prepare();


    public abstract void applyGameRules();


    protected LevelSize getLevelSizeFromParameters() {
        int levelSizeIndex = Integer.valueOf(loadingParameters.getParameter("level_size").toString());
        return LevelSize.values()[levelSizeIndex];
    }


    protected String getLevelCodeFromParameters() {
        return (String) loadingParameters.getParameter("level_code");
    }


    protected LevelSize getLevelSize(int mapSize) {
        if (mapSize < 2) {
            return LevelSize.values()[0];
        }

        if (mapSize < 4) {
            return LevelSize.values()[1];
        }

        return LevelSize.values()[2];
    }


    protected int getMapSizeFromParameters() {
        return Integer.valueOf(loadingParameters.getParameter("map_size").toString());
    }


    public abstract void loadSavedData(int step);


    public abstract void generateLevel();


    public abstract void onEndCreation();


    public void setLoadingParameters(LoadingParameters loadingParameters) {
        this.loadingParameters = loadingParameters;
        GameRules.initialParameters = loadingParameters;
    }


    public void initGameMode(GameMode gameMode) {
        loadingManager.initGameMode(gameMode);
    }


    public void initLevelSize(LevelSize levelSize) {
        loadingManager.initLevelSize(levelSize);
    }


    protected ObjectsLayer getObjectsLayer() {
        return getGameController().objectsLayer;
    }


    protected GameController getGameController() {
        return loadingManager.yioGdxGame.gameController;
    }
}
