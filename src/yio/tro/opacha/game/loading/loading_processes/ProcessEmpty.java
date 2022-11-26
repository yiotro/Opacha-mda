package yio.tro.opacha.game.loading.loading_processes;

import yio.tro.opacha.game.GameMode;
import yio.tro.opacha.game.LevelSize;
import yio.tro.opacha.game.loading.LoadingManager;

public class ProcessEmpty extends AbstractLoadingProcess{

    public ProcessEmpty(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.custom);
        initLevelSize(LevelSize.normal);
    }


    @Override
    public void applyGameRules() {

    }


    @Override
    public void loadSavedData(int step) {

    }


    @Override
    public void generateLevel() {

    }


    @Override
    public void onEndCreation() {

    }
}
