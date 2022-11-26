package yio.tro.opacha.game.loading.loading_processes;

import yio.tro.opacha.game.Difficulty;
import yio.tro.opacha.game.GameMode;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.LevelSize;
import yio.tro.opacha.game.loading.LoadingManager;

public class ProcessEditorLoad extends AbstractLoadingProcess{

    public ProcessEditorLoad(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.editor);

        String levelCode = getLevelCodeFromParameters();
        LevelSize levelSize = gameController.importManager.getLevelSizeFromLevelCode(levelCode);
        initLevelSize(levelSize);
    }


    @Override
    public void applyGameRules() {
        GameRules.difficulty = Difficulty.normal;
    }


    @Override
    public void loadSavedData(int step) {

    }


    @Override
    public void generateLevel() {
        String levelCode = getLevelCodeFromParameters();
        gameController.importManager.perform(levelCode);
    }


    @Override
    public void onEndCreation() {

    }
}
