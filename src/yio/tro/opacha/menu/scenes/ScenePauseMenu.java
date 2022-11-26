package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.menu.elements.LabelElement;
import yio.tro.opacha.menu.elements.ground.GpSrDown;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ButtonYio;

public class ScenePauseMenu extends SceneYio {


    public ButtonYio resumeButton;
    public ButtonYio restartButton;
    public ButtonYio mainMenuButton;
    public LabelElement labelElement;


    @Override
    public void initialize() {
        setBackground(GroundIndex.CYAN);
        getGround().setSpawnRule(new GpSrDown());

        resumeButton = uiFactory.getButton()
                .setSize(0.8, 0.08)
                .setPosition(0.1, 0.55)
                .applyText("resume")
                .setReaction(getResumeReaction())
                .setAnimation(AnimationYio.none);

        restartButton = uiFactory.getButton()
                .setSize(0.8, 0.08)
                .centerHorizontal()
                .alignBottom(previousElement, 0.02)
                .applyText("restart")
                .setReaction(getRestartReaction())
                .setAnimation(AnimationYio.none);

        mainMenuButton = uiFactory.getButton()
                .setSize(0.8, 0.08)
                .centerHorizontal()
                .alignBottom(previousElement, 0.02)
                .applyText("main_menu")
                .setReaction(getMainMenuReaction())
                .setAnimation(AnimationYio.none)
                .tagAsBackButton();

        labelElement = uiFactory.getLabelElement()
                .setSize(0.5, 0.035)
                .alignTop(0.01)
                .centerHorizontal()
                .setFont(Fonts.smallFont)
                .setString("-");
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        updateLabel();
    }


    private void updateLabel() {
        if (GameRules.levelIndex == -1) {
            labelElement.setString("");
            return;
        }

        labelElement.setString(languagesManager.getString("level") + ": " + GameRules.levelIndex);
    }


    private Reaction getMainMenuReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                yioGdxGame.setGamePaused(true);
                Scenes.mainMenu.create();
            }
        };
    }


    private Reaction getRestartReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.confirmRestart.create();
            }
        };
    }


    private Reaction getResumeReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                yioGdxGame.gameView.appear();
                gameController.createMenuOverlay();
                yioGdxGame.setGamePaused(false);
            }
        };
    }
}
