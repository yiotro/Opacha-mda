package yio.tro.opacha.game.loading.loading_processes;

import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.Difficulty;
import yio.tro.opacha.game.GameMode;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.loading.LoadingManager;

import java.util.Random;

public class ProcessCalendar extends AbstractLoadingProcess{

    private int index;
    private int mapSize;


    public ProcessCalendar(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.calendar);

        updateIndex();
        YioGdxGame.predictableRandom = new Random(index);
        mapSize = YioGdxGame.predictableRandom.nextInt(3);
        initLevelSize(getLevelSize(mapSize));
    }


    private void updateIndex() {
        int year = (int) loadingParameters.getParameter("year");
        int month = (int) loadingParameters.getParameter("month");
        int day = (int) loadingParameters.getParameter("day");
        index = year + month * 1000 + day * 100000;
    }


    @Override
    public void applyGameRules() {
        updateIndex();
        GameRules.difficulty = getDifficultyByIndex(index);
    }


    private Difficulty getDifficultyByIndex(int index) {
        if (index % 15 == 0) return Difficulty.hard;
        return Difficulty.normal;
    }


    @Override
    public void loadSavedData(int step) {

    }


    @Override
    public void generateLevel() {
        updateIndex();
        getObjectsLayer().mapGenerator.generateLinkedMap(mapSize, getColors());
    }


    private int getColors() {
        return YioGdxGame.predictableRandom.nextInt(3) + 2;
    }


    @Override
    public void onEndCreation() {

    }
}
