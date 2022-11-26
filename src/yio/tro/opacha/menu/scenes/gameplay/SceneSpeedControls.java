package yio.tro.opacha.menu.scenes.gameplay;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.SettingsManager;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.ModalSceneYio;
import yio.tro.opacha.stuff.GraphicsYio;

public class SceneSpeedControls extends ModalSceneYio {


    public ButtonYio playPauseButton;
    private TextureRegion playIcon;
    private TextureRegion selectionTexture;
    private TextureRegion tacticalPauseIcon;
    private double touchOffset;
    private ButtonYio fastForwardButton;
    private double visualOffset;


    @Override
    protected void initialize() {
        loadTextures();
        touchOffset = 0.04;
        visualOffset = 0.03;
        createSpeedControls();
    }


    private void createSpeedControls() {
        playPauseButton = uiFactory.getButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .alignLeft(touchOffset)
                .alignTop(getTopOffset())
                .setTouchOffset(touchOffset)
                .setIgnoreResumePause(true)
                .setReaction(getPlayPauseReaction())
                .setSelectionTexture(selectionTexture);

        fastForwardButton = uiFactory.getButton()
                .clone(previousElement)
                .alignRight(previousElement, 2 * touchOffset)
                .setReaction(getFastForwardReaction())
                .loadTexture("menu/gameplay/fast_forward_icon.png")
                .setSelectionTexture(selectionTexture);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        onSpeedStateChanged();
    }


    private double getTopOffset() {
        double hOffset = GraphicsYio.convertToHeight(touchOffset);
        if (SettingsManager.getInstance().velocitySliderEnabled) {
            return 0.06 + hOffset;
        }
        return hOffset;
    }


    private Reaction getFastForwardReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                gameController.speedManager.onFastForwardButtonPressed();
            }
        };
    }


    private void loadTextures() {
        selectionTexture = GraphicsYio.loadTextureRegion("menu/selection.png", true);
        playIcon = GraphicsYio.loadTextureRegion("menu/gameplay/play_icon.png", true);
        tacticalPauseIcon = GraphicsYio.loadTextureRegion("menu/gameplay/tactical_pause_icon.png", true);
    }


    public void onSpeedStateChanged() {
        if (playPauseButton == null) return;
        if (yioGdxGame.gameController.speedManager.getSpeed() == 0) {
            playPauseButton.setTextureRegion(playIcon);
        } else {
            playPauseButton.setTextureRegion(tacticalPauseIcon);
        }
    }


    private Reaction getPlayPauseReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                gameController.speedManager.onPlayPauseButtonPressed();
            }
        };
    }
}
