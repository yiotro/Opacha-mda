package yio.tro.opacha.menu.scenes.editor;

import yio.tro.opacha.game.GameController;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.export_import.ExportManager;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.game.loading.LoadingParameters;
import yio.tro.opacha.game.loading.LoadingType;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.scenes.AbstractLoadingSceneYio;

public class SceneEditorPrepareToLaunch extends AbstractLoadingSceneYio {


    private String levelCode;


    @Override
    protected int getBackgroundIndex() {
        return GroundIndex.MAGENTA;
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        GameController gameController = yioGdxGame.gameController;
        ObjectsLayer objectsLayer = gameController.objectsLayer;
        ExportManager exportManager = objectsLayer.exportManager;
        levelCode = exportManager.perform();
    }


    @Override
    protected void applyAction() {
        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.addParameter("level_code", levelCode);
        yioGdxGame.loadingManager.startLoading(LoadingType.editor_play, loadingParameters);
    }
}
