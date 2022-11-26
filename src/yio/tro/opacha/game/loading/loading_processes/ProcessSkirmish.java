package yio.tro.opacha.game.loading.loading_processes;

import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.Difficulty;
import yio.tro.opacha.game.GameMode;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.loading.LoadingManager;

public class ProcessSkirmish extends AbstractLoadingProcess {

    public ProcessSkirmish(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.skirmish);
        initLevelSize(getLevelSize(getMapSizeFromParameters()));
    }


    @Override
    public void applyGameRules() {
        GameRules.aiOnlyMode = (boolean) loadingParameters.getParameter("ai_only");
        int difficultyIndex = (int) loadingParameters.getParameter("difficulty");
        GameRules.difficulty = Difficulty.values()[difficultyIndex];
    }


    @Override
    public void loadSavedData(int step) {

    }


    @Override
    public void generateLevel() {
        int seed = (int) loadingParameters.getParameter("seed");
        YioGdxGame.predictableRandom.setSeed(seed);
        int colors = (int) loadingParameters.getParameter("colors");
        getObjectsLayer().mapGenerator.generateLinkedMap(getMapSizeFromParameters(), colors);
    }


    @Override
    public void onEndCreation() {

    }
}
