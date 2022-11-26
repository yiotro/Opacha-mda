package yio.tro.opacha.menu.scenes.tutorial;

import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.Scenes;

public class SceneTutorialBasicControls extends AbstractTutorialScene{

    @Override
    protected String getHintKey() {
        return "tutorial_basic_controls";
    }


    @Override
    protected int getGroundIndex() {
        return GroundIndex.CYAN;
    }


    @Override
    protected Reaction getNextSceneReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.tutorialShields.create();
            }
        };
    }
}
