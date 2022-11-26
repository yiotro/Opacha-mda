package yio.tro.opacha.game.loading.level_generators;

import yio.tro.opacha.game.GameController;

public abstract class LevelGenerator {

    protected GameController gameController;


    public LevelGenerator(GameController gameController) {
        this.gameController = gameController;
    }


    public final void generate() {
        begin();
        createInternals();
        end();
    }


    protected void begin() {
        // nothing by default
    }


    protected abstract void createInternals();


    protected void end() {
        // nothing by default
    }
}
