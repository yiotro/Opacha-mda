package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.OneTimeInfo;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.BackgroundYio;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.stuff.GraphicsYio;

public class SceneMyGamesButton extends ModalSceneYio{

    @Override
    protected void initialize() {
        uiFactory.getButton()
                .setSize(GraphicsYio.convertToWidth(0.2), 0.045)
                .alignBottom(0.01)
                .alignRight(GraphicsYio.convertToWidth(0.01))
                .setGroundIndex(GroundIndex.MAIN_MENU)
                .setTouchOffset(0.04)
                .setBorder(false)
                .applyText("my_games")
                .setAnimation(AnimationYio.down)
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        apply();
                    }
                });
    }


    private void apply() {
        destroy();
        OneTimeInfo.getInstance().myGames = true;
        OneTimeInfo.getInstance().save();
        Scenes.myGamesArticle.create();
    }
}
