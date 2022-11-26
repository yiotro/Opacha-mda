package yio.tro.opacha.menu.scenes.editor;

import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.elements.ground.GpSrDown;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.SceneYio;
import yio.tro.opacha.menu.scenes.Scenes;

public class SceneEditorPauseMenu extends SceneYio{

    public ButtonYio resumeButton;
    public ButtonYio saveButton;
    public ButtonYio mainMenuButton;
    private ButtonYio launchButton;


    @Override
    protected void initialize() {
        setBackground(GroundIndex.CYAN);
        getGround().setSpawnRule(new GpSrDown());

        resumeButton = uiFactory.getButton()
                .setSize(0.8, 0.08)
                .setPosition(0.1, 0.6)
                .applyText("resume")
                .setReaction(getResumeReaction())
                .setAnimation(AnimationYio.none);

        launchButton = uiFactory.getButton()
                .setSize(0.8, 0.08)
                .centerHorizontal()
                .alignBottom(previousElement, 0.02)
                .applyText("launch")
                .setReaction(getLaunchReaction())
                .setAnimation(AnimationYio.none);

        saveButton = uiFactory.getButton()
                .setSize(0.8, 0.08)
                .centerHorizontal()
                .alignBottom(previousElement, 0.02)
                .applyText("save")
                .setReaction(getSaveReaction())
                .setAnimation(AnimationYio.none);

        mainMenuButton = uiFactory.getButton()
                .setSize(0.8, 0.08)
                .centerHorizontal()
                .alignBottom(previousElement, 0.02)
                .applyText("main_menu")
                .setReaction(getMainMenuReaction())
                .setAnimation(AnimationYio.none)
                .tagAsBackButton();
    }


    private Reaction getLaunchReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                onLaunchButtonPressed();
            }
        };
    }


    void onLaunchButtonPressed() {
        ObjectsLayer objectsLayer = yioGdxGame.gameController.objectsLayer;
        objectsLayer.editorWorker.onLaunchButtonPressed();
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


    private Reaction getSaveReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.editorSave.create();
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
