package yio.tro.opacha.menu.scenes.tutorial;

import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.Scenes;

public class SceneTutorialHalvedSelection extends AbstractTutorialScene{

    @Override
    protected String getHintKey() {
        return "tutorial_halved_selection";
    }


    @Override
    protected int getGroundIndex() {
        return GroundIndex.YELLOW;
    }


    @Override
    protected Reaction getNextSceneReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.tutorialEnd.create();
            }
        };
    }
}
