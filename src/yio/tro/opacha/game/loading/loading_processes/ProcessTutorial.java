package yio.tro.opacha.game.loading.loading_processes;

import yio.tro.opacha.game.*;
import yio.tro.opacha.game.loading.LoadingManager;

public class ProcessTutorial extends AbstractLoadingProcess{

    public ProcessTutorial(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.tutorial);

        String levelCode = yioGdxGame.tutorialManager.getLevelCode();
        LevelSize levelSize = gameController.importManager.getLevelSizeFromLevelCode(levelCode);
        initLevelSize(levelSize);
    }


    @Override
    public void applyGameRules() {
        GameRules.difficulty = Difficulty.normal; // will be rewritten on import
    }


    @Override
    public void loadSavedData(int step) {

    }


    @Override
    public void generateLevel() {
        String levelCode = yioGdxGame.tutorialManager.getLevelCode();
        gameController.importManager.perform(levelCode);
    }


    @Override
    public void onEndCreation() {
        CameraController cameraController = gameController.cameraController;
        cameraController.loadFromString("0.52 0.7 0.55");
        cameraController.setLocked(true);
    }
}
