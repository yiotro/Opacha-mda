package yio.tro.opacha.game.tutorial;

import yio.tro.opacha.menu.scenes.Scenes;

public abstract class ForefingerScriptYio extends ScriptYio{

    @Override
    public boolean isActive() {
        return Scenes.forefinger.isCurrentlyVisible();
    }
}
