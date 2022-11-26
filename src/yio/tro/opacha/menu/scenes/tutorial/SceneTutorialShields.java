package yio.tro.opacha.menu.scenes.tutorial;

import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.Scenes;

public class SceneTutorialShields extends AbstractTutorialScene{

    @Override
    protected String getHintKey() {
        return "tutorial_shields";
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
                Scenes.tutorialUpgradesOne.create();
            }
        };
    }
}
