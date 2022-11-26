package yio.tro.opacha;

import com.badlogic.gdx.Input;
import yio.tro.opacha.game.GameController;
import yio.tro.opacha.game.editor.EditorSavesManager;
import yio.tro.opacha.game.editor.EsmItem;
import yio.tro.opacha.game.loading.LoadingParameters;
import yio.tro.opacha.game.loading.LoadingType;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.elements.CircleButtonYio;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.keyboard.NativeKeyboardElement;
import yio.tro.opacha.menu.scenes.Scenes;

public class OnKeyReactions {

    YioGdxGame yioGdxGame;
    MenuControllerYio menuControllerYio;
    GameController gameController;


    public OnKeyReactions(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        menuControllerYio = yioGdxGame.menuControllerYio;
        gameController = yioGdxGame.gameController;
    }


    public void keyDown(int keycode) {
        if (checkForKeyboard(keycode)) return;
        if (checkForInterfaceElements(keycode)) return; // key press was stolen by ui element

        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            onBackPressed();
            return;
        }

        if (keycode == Input.Keys.ENTER) {
            onEnterPressed();
            return;
        }

        if (keycode == Input.Keys.SPACE) {
            onSpacePressed();
            return;
        }

        checkOtherStuff(keycode);

        return;
    }


    private boolean checkForInterfaceElements(int keycode) {
        for (InterfaceElement element : menuControllerYio.getInterfaceElements()) {
            if (!element.isVisible()) continue;

            if (element.onKeyDown(keycode)) {
                return true;
            }
        }

        return false;
    }


    private boolean checkForKeyboard(int keycode) {
        NativeKeyboardElement keyboardElement = Scenes.keyboard.nativeKeyboardElement;
        if (keyboardElement == null) return false;
        if (keyboardElement.getFactor().get() < 0.2) return false;

        keyboardElement.onPcKeyPressed(keycode);
        return true;
    }


    private void onSpacePressed() {
        gameController.speedManager.onPlayPauseButtonPressed();
    }


    private void checkOtherStuff(int keycode) {
        switch (keycode) {
            case Input.Keys.D:
                gameController.debugActions(); // debug
                break;
            case Input.Keys.L:
                doLaunchQuickGame();
                break;
            case Input.Keys.K:
                loadLastEditorSlot();
                break;
            case Input.Keys.C:
                yioGdxGame.setGamePaused(true);
                yioGdxGame.gameView.destroy();
                Scenes.secretScreen.create();
                break;
            case Input.Keys.S:
                yioGdxGame.slowMo = !yioGdxGame.slowMo;
                yioGdxGame.render();
                yioGdxGame.render();
                yioGdxGame.render();
                break;
            case Input.Keys.A:
                if (!Scenes.debugPanel.isCurrentlyVisible()) {
                    Scenes.debugPanel.create();
                } else {
                    Scenes.debugPanel.destroy();
                }
                break;
            case Input.Keys.Z:
                gameController.cameraController.setTargetZoomLevel(gameController.cameraController.comfortableZoomLevel);
                break;
            case Input.Keys.X:
                yioGdxGame.setGamePaused(true);
                yioGdxGame.gameView.destroy();
                yioGdxGame.gameController.onEscapedToPauseMenu();
                Scenes.campaign.create();
                break;
            case Input.Keys.U:
                if (!gameController.yioGdxGame.gamePaused) {
                    gameController.cameraController.changeZoomLevel(0.1);
                } else {
                    System.out.println("OnKeyReactions.checkOtherStuff");
                }
                break;
            case Input.Keys.I:
                if (!gameController.yioGdxGame.gamePaused) {
                    gameController.cameraController.changeZoomLevel(-0.1);
                } else {
                    Scenes.editorLobby.performImportFromClipboard();
                }
                break;
            case Input.Keys.P:
                if (gameController.objectsLayer == null) break;
                if (gameController.objectsLayer.impossibruDetector == null) break;
                boolean situationImpossibru = gameController.objectsLayer.impossibruDetector.isSituationImpossibru();
                System.out.println("situationImpossibru = " + situationImpossibru);
                break;
            case Input.Keys.Q:
                if (Scenes.editorOverlay.isCurrentlyVisible()) {
                    pressElementIfVisible(Scenes.editorOverlay.optionsButton);
                }
                break;
            case Input.Keys.NUM_1:
                pressElementIfVisible(Scenes.selectionOverlay.bEconomic);
                pressElementIfVisible(Scenes.editorOverlay.addButton);
                break;
            case Input.Keys.NUM_2:
                pressElementIfVisible(Scenes.selectionOverlay.bDefensive);
                pressElementIfVisible(Scenes.editorOverlay.removeButton);
                break;
        }
    }


    private void loadLastEditorSlot() {
        yioGdxGame.setGamePaused(true);
        yioGdxGame.gameView.destroy();

        EditorSavesManager editorSavesManager = yioGdxGame.gameController.editorSavesManager;
        EsmItem lastItem = editorSavesManager.getLastItem();
        if (lastItem == null) return;

        String levelCode = lastItem.levelCode;
        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.addParameter("level_code", levelCode);
        loadingParameters.addParameter("slot_key", lastItem.index);
        yioGdxGame.loadingManager.startInstantly(LoadingType.editor_load, loadingParameters);
    }


    private void doLaunchQuickGame() {
        yioGdxGame.setGamePaused(true);
        yioGdxGame.gameView.destroy();
        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.addParameter("map_size", 2);
        loadingParameters.addParameter("colors", 3);
        loadingParameters.addParameter("ai_only", false);
        loadingParameters.addParameter("difficulty", 2);
        loadingParameters.addParameter("seed", YioGdxGame.random.nextInt());
        yioGdxGame.loadingManager.startInstantly(LoadingType.skirmish_create, loadingParameters);
    }


    private void onEnterPressed() {
        pressElementIfVisible(Scenes.mainMenu.playButton);
        pressElementIfVisible(Scenes.chooseGameMode.skirmishButton);
        pressElementIfVisible(Scenes.pauseMenu.resumeButton);
        pressElementIfVisible(Scenes.editorPauseMenu.resumeButton);
        pressElementIfVisible(Scenes.exceptionReport.okButton);
        pressElementIfVisible(Scenes.editorLobby.creationButton);
        pressElementIfVisible(Scenes.skirmishMenu.startButton);
    }


    private void onBackPressed() {
        for (int i = menuControllerYio.getInterfaceElements().size() - 1; i >= 0; i--) {
            InterfaceElement interfaceElement = menuControllerYio.getInterfaceElements().get(i);
            if (!interfaceElement.isVisible()) continue;
            if (interfaceElement.getFactor().get() < 0.95) continue;

            if ((interfaceElement instanceof ButtonYio)) {
                ButtonYio buttonYio = (ButtonYio) interfaceElement;
                if (!buttonYio.isReturningBackButton()) continue;

                buttonYio.pressArtificially();
                break;
            }

            if ((interfaceElement instanceof CircleButtonYio)) {
                CircleButtonYio buttonYio = (CircleButtonYio) interfaceElement;
                if (!buttonYio.isReturningBackButton()) continue;

                buttonYio.pressArtificially();
                break;
            }
        }
    }


    boolean pressElementIfVisible(InterfaceElement element) {
        if (element == null) return false;
        if (!element.isVisible()) return false;
        if (element.getFactor().get() < 0.95) return false;
        element.pressArtificially();
        return true;
    }
}
