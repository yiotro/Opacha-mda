package yio.tro.opacha.game.loading.level_generators;

import yio.tro.opacha.game.GameController;

public class EmptyLevelGenerator extends LevelGenerator{

    public EmptyLevelGenerator(GameController gameController) {
        super(gameController);
    }



    @Override
    protected void createInternals() {
        // nothing
    }

}
