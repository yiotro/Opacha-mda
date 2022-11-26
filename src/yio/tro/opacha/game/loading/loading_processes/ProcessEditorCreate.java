package yio.tro.opacha.game.loading.loading_processes;

import yio.tro.opacha.game.Difficulty;
import yio.tro.opacha.game.GameMode;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.loading.LoadingManager;

public class ProcessEditorCreate extends AbstractLoadingProcess{

    public ProcessEditorCreate(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.editor);
        initLevelSize(getLevelSize(getMapSizeFromParameters()));
    }


    @Override
    public void applyGameRules() {
        GameRules.aiOnlyMode = false;
        GameRules.difficulty = Difficulty.normal;
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
