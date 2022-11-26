package yio.tro.opacha.menu.scenes.info;

import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.DebugDataContainer;

public class SceneDebugData extends AbstractInfoScene{

    @Override
    protected void initialize() {
        createInfoMenu("Default text", getBackReaction(), 16, 1);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        updateText();
    }


    private void updateText() {
        infoPanel.applyFixedAmountOfLines(DebugDataContainer.text, 16);
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.secretScreen.create();
            }
        };
    }
}
