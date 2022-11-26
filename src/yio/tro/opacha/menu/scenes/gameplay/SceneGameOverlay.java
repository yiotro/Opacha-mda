package yio.tro.opacha.menu.scenes.gameplay;

import yio.tro.opacha.game.touch_modes.TouchMode;
import yio.tro.opacha.menu.elements.gameplay.ViewTouchModeElement;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.scenes.ModalSceneYio;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.GraphicsYio;

public class SceneGameOverlay extends ModalSceneYio {

    public ButtonYio pauseMenuButton;
    public ViewTouchModeElement viewTouchModeElement;


    public SceneGameOverlay() {
        viewTouchModeElement = null;
    }


    @Override
    protected void beginCreation() {
        menuControllerYio.setCurrentScene(this);
        destroyAllVisibleElements();
        menuControllerYio.checkToRemoveInvisibleElements();
    }


    @Override
    public void initialize() {
        pauseMenuButton = uiFactory.getButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .alignRight(0.03)
                .alignTop(GraphicsYio.convertToHeight(0.03))
                .setTouchOffset(0.05)
                .loadTexture("menu/pause_icon.png")
                .setReaction(getPauseReaction())
                .setKey("pause_menu")
                .setSelectionTexture(GraphicsYio.loadTextureRegion("menu/selection.png", true))
                .tagAsBackButton();

        viewTouchModeElement = uiFactory.getViewTouchModeElement()
                .setSize(1, 0.06)
                .centerHorizontal()
                .alignTop();
    }


    private Reaction getPauseReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                yioGdxGame.setGamePaused(true);
                gameController.onEscapedToPauseMenu();
                menuControllerYio.destroyGameView();

                gameController.createPauseMenu();
            }
        };
    }


    public void onTouchModeSet(TouchMode touchMode) {
        if (viewTouchModeElement != null) {
            viewTouchModeElement.onTouchModeSet(touchMode);
        }
    }


    @Override
    public void onLevelCreationEnd() {
        super.onLevelCreationEnd();
    }
}
