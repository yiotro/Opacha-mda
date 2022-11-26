package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;

public class SceneReward extends SceneYio{

    @Override
    protected void initialize() {
        setBackground(GroundIndex.YELLOW);

        uiFactory.getAnimationEggElement()
                .setSize(1, 1)
                .centerHorizontal()
                .centerVertical()
                .setAnimation(AnimationYio.none);
    }


}
