package yio.tro.opacha.menu.scenes.gameplay;

import com.badlogic.gdx.Gdx;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.debug.DebugFlags;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.elements.CheckButtonYio;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.ModalSceneYio;
import yio.tro.opacha.menu.scenes.Scenes;

public class SceneDebugPanel extends ModalSceneYio {


    private ButtonYio label = null;
    private CheckButtonYio chkDebugEnabled;
    private CheckButtonYio chkEditCampaignLayout;
    private CheckButtonYio chkShowGraph;
    private CheckButtonYio chkFogEnabled;
    private CheckButtonYio chkLighting;


    @Override
    protected void initialize() {
        createCloseButton();
        createLabel();
        createInternals();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
        yioGdxGame.gameController.resetTouchMode();
    }


    private void loadValues() {

    }


    private void createInternals() {
        createCheckButtons();
        createButtons();
    }


    private void createButtons() {
        uiFactory.getButton()
                .setParent(label)
                .setSize(0.8, 0.06)
                .centerHorizontal()
                .alignBottom(0.02)
                .applyText("Export")
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        System.out.println("SceneDebugPanel.reaction");
                        Scenes.notification.show("Exported");
                        destroy();
                    }
                });

        uiFactory.getButton()
                .clone(previousElement)
                .centerHorizontal()
                .alignTop(previousElement, 0.01)
                .applyText("Test explosions")
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
//                        gameController.setTouchMode(TouchMode.tmSpawnParticles);
//                        GameRules.fogEnabled = false;
                        Gdx.input.setOnscreenKeyboardVisible(true);
                        destroy();
                    }
                });
    }


    private void createCheckButtons() {
        chkDebugEnabled = uiFactory.getCheckButton()
                .setParent(label)
                .setName("Debug enabled")
                .alignTop(0.02)
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        System.out.println("SceneDebugPanel.reaction");
                    }
                });

        chkEditCampaignLayout = uiFactory.getCheckButton()
                .clone(previousElement)
                .alignBottom(previousElement)
                .setName("Edit campaign layout")
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        System.out.println("SceneDebugPanel.reaction");
                    }
                });

        chkShowGraph = uiFactory.getCheckButton()
                .clone(previousElement)
                .alignBottom(previousElement)
                .setName("Show graph")
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        System.out.println("SceneDebugPanel.reaction");
                    }
                });

        chkFogEnabled = uiFactory.getCheckButton()
                .clone(previousElement)
                .alignBottom(previousElement)
                .setName("Fog")
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        System.out.println("SceneDebugPanel.reaction");
                    }
                });

        chkLighting = uiFactory.getCheckButton()
                .clone(previousElement)
                .alignBottom(previousElement)
                .setName("Lighting")
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        System.out.println("SceneDebugPanel.reaction");
                    }
                });
    }


    private void createLabel() {
        label = uiFactory.getButton()
                .setSize(0.9, 0.6)
                .alignBottom(0.05)
                .centerHorizontal()
                .setAnimation(AnimationYio.down)
                .setGroundIndex(GroundIndex.BUTTON_WHITE)
                .setTransparencyEnabled(false)
                .setCornerRadius(0.07)
                .setFakeDyingStatusEnabled(true)
                .setSilentReactionMode(true)
                .setShadow(true);
    }


    public boolean isCurrentlyVisible() {
        if (label == null) return false;

        return label.getFactor().get() > 0;
    }
}
