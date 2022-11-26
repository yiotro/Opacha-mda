package yio.tro.opacha.menu.scenes.info.help;

import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.SceneYio;
import yio.tro.opacha.menu.scenes.Scenes;

public class SceneHelpMenu extends SceneYio {


    @Override
    protected void initialize() {
        setBackground(GroundIndex.MAGENTA);

        spawnBackButton(new Reaction() {
            @Override
            protected void reaction() {
                yioGdxGame.setGamePaused(true);
                Scenes.mainMenu.create();
            }
        });
    }
}
