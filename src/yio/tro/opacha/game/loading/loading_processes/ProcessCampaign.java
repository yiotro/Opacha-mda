package yio.tro.opacha.game.loading.loading_processes;

import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.Difficulty;
import yio.tro.opacha.game.GameMode;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.loading.LoadingManager;

import java.util.Random;

public class ProcessCampaign extends AbstractLoadingProcess{

    int mapSize;
    private int index;


    public ProcessCampaign(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.campaign);

        loadIndexFromParameters();
        YioGdxGame.predictableRandom = new Random(index);
        mapSize = YioGdxGame.predictableRandom.nextInt(3);
        initLevelSize(getLevelSize(mapSize));
    }


    private void loadIndexFromParameters() {
        index = (int) loadingParameters.getParameter("index");
    }


    @Override
    public void applyGameRules() {
        loadIndexFromParameters();
        GameRules.difficulty = getDifficultyByIndex(index);
        GameRules.levelIndex = index;
    }


    private Difficulty getDifficultyByIndex(int index) {
        if (index < 2) {
            return Difficulty.easy;
        }

        if (index % 7 == 0 && index > 25) return Difficulty.hard;

        return Difficulty.normal;
    }


    @Override
    public void loadSavedData(int step) {

    }


    @Override
    public void generateLevel() {
        loadIndexFromParameters();
        getObjectsLayer().mapGenerator.generateLinkedMap(mapSize, getColors());
    }


    private int getColors() {
        if (index < 4) return 2;
        return YioGdxGame.predictableRandom.nextInt(3) + 2;
    }


    @Override
    public void onEndCreation() {

    }
}
