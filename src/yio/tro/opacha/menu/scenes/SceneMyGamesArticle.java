package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ground.GroundIndex;

public class SceneMyGamesArticle extends ModalSceneYio{

    @Override
    protected void initialize() {
        createCloseButton();
        uiFactory.getButton()
                .setSize(0.85, 0.4)
                .centerHorizontal()
                .alignBottom(0.25)
                .setGroundIndex(GroundIndex.MAIN_MENU)
                .setAnimation(AnimationYio.down)
                .setFont(Fonts.miniFont)
                .applyText(convertStringToArray(languagesManager.getString("my_games_article")))
                .setTouchable(false);
    }
}
