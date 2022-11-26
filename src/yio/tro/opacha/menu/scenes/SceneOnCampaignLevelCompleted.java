package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.game.campaign.CampaignManager;
import yio.tro.opacha.game.loading.LoadingParameters;
import yio.tro.opacha.game.loading.LoadingType;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;

public class SceneOnCampaignLevelCompleted extends SceneYio{

    int currentLevelIndex;
    private ButtonYio label;


    @Override
    protected void initialize() {
        setBackground(GroundIndex.CYAN);
        createBackButton();
        createLabel();
        createNextButton();
    }


    private void createNextButton() {
        uiFactory.getButton()
                .setParent(label)
                .setSize(0.4, 0.06)
                .alignBottom(0.01)
                .alignRight(0.03)
                .applyText("next")
                .setReaction(getNextReaction());
    }


    private Reaction getNextReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                onNextLevelButtonPressed();
            }
        };
    }


    private void onNextLevelButtonPressed() {
        if (CampaignManager.getInstance().areAllLevelsCompleted()) {
            Scenes.reward.create();
            return;
        }

        int nextLevelIndex = CampaignManager.getInstance().getNextLevelIndex(currentLevelIndex);

        // last level completed (deprecated)
        if (nextLevelIndex == currentLevelIndex) {
            Scenes.reward.create();
            return;
        }

        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.addParameter("index", nextLevelIndex);
        yioGdxGame.loadingManager.startInstantly(LoadingType.campaign_create, loadingParameters);
    }


    private void createLabel() {
        label = uiFactory.getButton()
                .setSize(0.8, 0.25)
                .centerHorizontal()
                .alignBottom(0.3)
                .setTouchable(false)
                .applyText("level_complete");
    }


    private void createBackButton() {
        spawnBackButton(new Reaction() {
            @Override
            protected void reaction() {
                Scenes.chooseGameMode.create();
            }
        });
    }


    public void setCurrentLevelIndex(int currentLevelIndex) {
        this.currentLevelIndex = currentLevelIndex;
    }
}
