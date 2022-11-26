package yio.tro.opacha.game.tutorial;

import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.GameController;
import yio.tro.opacha.menu.MenuControllerYio;

public abstract class ScriptYio {

    YioGdxGame yioGdxGame;
    GameController gameController;
    MenuControllerYio menuControllerYio;
    boolean applied;


    public ScriptYio() {
        applied = false;
    }


    public abstract void apply();


    public abstract boolean isActive();


    public boolean isReady() {
        return true;
    }


    public void setYioGdxGame(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        gameController = yioGdxGame.gameController;
        menuControllerYio = yioGdxGame.menuControllerYio;
    }
}
