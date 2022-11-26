package yio.tro.opacha.menu.reactions;

import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.game.GameController;
import yio.tro.opacha.YioGdxGame;

public abstract class Reaction {

    protected GameController gameController;
    protected MenuControllerYio menuControllerYio;
    protected YioGdxGame yioGdxGame;


    protected abstract void reaction();


    public void performReactActions(MenuControllerYio menuControllerYio) {
        if (menuControllerYio != null) {
            this.menuControllerYio = menuControllerYio;
            yioGdxGame = menuControllerYio.yioGdxGame;
            gameController = yioGdxGame.gameController;
        }
        reaction();
    }


    public static RbNothing rbNothing = new RbNothing();

}
