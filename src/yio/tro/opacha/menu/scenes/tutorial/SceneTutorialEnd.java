package yio.tro.opacha.menu.scenes.tutorial;

import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.Scenes;

public class SceneTutorialEnd extends AbstractTutorialScene{

    @Override
    protected String getHintKey() {
        return "tutorial_end";
    }


    @Override
    protected int getGroundIndex() {
        return GroundIndex.MAGENTA;
    }


    @Override
    protected Reaction getNextSceneReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.onCampaignLevelCompleted.create();
                Scenes.onCampaignLevelCompleted.setCurrentLevelIndex(0);
                menuControllerYio.onReturningBackButtonPressed();
            }
        };
    }
}
