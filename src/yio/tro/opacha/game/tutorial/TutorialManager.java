package yio.tro.opacha.game.tutorial;

import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.CameraController;
import yio.tro.opacha.game.gameplay.model.Planet;
import yio.tro.opacha.menu.elements.tutorial.ForefingerElement;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.GraphicsYio;

public class TutorialManager {

    YioGdxGame yioGdxGame;


    public TutorialManager(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
    }


    public String getLevelCode() {
        return "opacha_level_code#map_name:none#game_rules:easy false#general:tiny#planets:neutral empty 3 0.507 0.754,green empty 25 0.526 0.477,green empty 50 0.725 0.632,green empty 2 0.317 0.607,neutral empty 3 0.372 0.931,neutral empty 3 0.542 1.069,neutral empty 3 0.72 0.894,neutral empty 3 0.258 1.146,neutral empty 3 0.762 1.245,neutral defensive 50 0.464 1.309,red empty 1 0.253 1.421,neutral empty 3 0.659 1.458,#links:neutral 0.507 0.754 0.526 0.477,neutral 0.507 0.754 0.725 0.632,neutral 0.507 0.754 0.317 0.607,neutral 0.372 0.931 0.507 0.754,neutral 0.372 0.931 0.542 1.069,neutral 0.72 0.894 0.542 1.069,neutral 0.258 1.146 0.372 0.931,neutral 0.464 1.309 0.542 1.069,neutral 0.542 1.069 0.762 1.245,neutral 0.253 1.421 0.464 1.309,neutral 0.659 1.458 0.762 1.245,neutral 0.659 1.458 0.464 1.309,#";
    }


    public ScriptYio[] getScriptsArray() {
        return new ScriptYio[]{
                new QuickScriptYio() {
                    @Override
                    public void apply() {
                        setSpeed(0);
                    }
                },
                createMessageScript("tutorial_begin"),
                createMessageScript("tutorial_your_planets"),
                createMessageScript("tutorial_basic_controls"),
                createTapOnPlanetScript(3),
                new QuickScriptYio() {
                    @Override
                    public void apply() {
                        setSpeed(1);
                    }
                },
                createTapOnPlanetScript(0),
                createMessageScript("tutorial_shields"),
                createTapOnPlanetScript(1),
                createTapOnPlanetScript(0),
                createMessageScript("tutorial_upgrades_one"),
                createTapOnPlanetScript(2),
                new ForefingerScriptYio() {
                    @Override
                    public void apply() {
                        doShowForefinger().setTarget(Scenes.selectionOverlay.bEconomic);
                    }
                },
                createMessageScript("tutorial_upgrades_two"),
                createMessageScript("tutorial_auto_target"),
                new ForefingerScriptYio() {
                    @Override
                    public void apply() {
                        doShowForefinger().setTarget(getPlanetByIndex(2), 0.8 * Math.PI);
                    }
                },
                createMessageScript("tutorial_deselect"),
                createTapOnPlanetScript(2),
                createDelayScript(100),
                createTapOnPlanetScript(2),
                createMessageScript("tutorial_halved_selection"),
                new ForefingerScriptYio() {
                    @Override
                    public void apply() {
                        doShowForefinger().setTarget(getPlanetByIndex(0), true);
                    }
                },
                createTapOnPlanetScript(3),
                createDelayScript(500),
                new QuickScriptYio() {
                    @Override
                    public void apply() {
                        CameraController cameraController = yioGdxGame.gameController.cameraController;
                        cameraController.setLocked(false);
                        cameraController.focusOnPoint(0.5 * GraphicsYio.width, 0.99 * GraphicsYio.width);
                        cameraController.setTargetZoomLevel(0.7f);
                    }
                },
                createMessageScript("tutorial_end"),
        };
    }


    private ScriptYio createTapOnPlanetScript(final int index) {
        return new ForefingerScriptYio() {
            @Override
            public void apply() {
                doShowForefinger().setTarget(getPlanetByIndex(index), false);
            }
        };
    }


    private ScriptYio createDelayScript(long delay) {
        return new DelayScriptYio(delay);
    }


    private ScriptYio createMessageScript(final String key) {
        return new ScriptYio() {
            @Override
            public void apply() {
                showMessage(key);
            }


            @Override
            public boolean isActive() {
                return isMessageVisible();
            }
        };
    }


    private void showMessage(String key) {
        Scenes.globalMessage.create();
        Scenes.globalMessage.globalMessageElement.setText(key);
    }


    private boolean isMessageVisible() {
        return Scenes.globalMessage.isCurrentlyVisible();
    }


    private void setSpeed(int speedValue) {
        yioGdxGame.gameController.speedManager.setSpeed(speedValue);
    }


    private ForefingerElement doShowForefinger() {
        Scenes.forefinger.create();
        return Scenes.forefinger.forefingerElement;
    }


    private Planet getPlanetByIndex(int index) {
        return yioGdxGame.gameController.objectsLayer.planetsManager.planets.get(index);
    }
}
