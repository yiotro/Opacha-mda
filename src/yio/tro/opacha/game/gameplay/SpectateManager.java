package yio.tro.opacha.game.gameplay;

import yio.tro.opacha.game.CameraController;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.SpeedManager;
import yio.tro.opacha.game.gameplay.model.*;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.RepeatYio;

import java.util.ArrayList;

public class SpectateManager implements IGameplayManager{

    ObjectsLayer objectsLayer;
    MatchResults matchResults;
    public boolean active;
    Planet targetPlanet;
    int currentStepIndex;
    RepeatYio<SpectateManager> repeatMakeStep;
    public boolean cameraMovementAllowed;


    public SpectateManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        matchResults = null;
        initRepeats();
    }


    private void initRepeats() {
        repeatMakeStep = new RepeatYio<SpectateManager>(this, 60) {
            @Override
            public void performAction() {
                parent.makeStep();
            }
        };
    }


    @Override
    public void defaultValues() {
        active = false;
        targetPlanet = null;
        currentStepIndex = 0;
        cameraMovementAllowed = true;
    }


    void makeStep() {
        switch (currentStepIndex) {
            default:
                doIncreaseSpeed();
                break;
            case 0:
                doUpgradeAllPlanets();
                break;
            case 1:
                doApplyAutoSupply();
                break;
            case 2:
                doFocusCameraOnTarget();
                break;
            case 3:
                doUnleashTheCamera();
                Scenes.endSpectate.create();
                break;
        }

        currentStepIndex++;
    }


    private void doApplyAutoSupply() {
        PathFinder pathFinder = objectsLayer.planetsManager.pathFinder;
        for (Planet planet : objectsLayer.planetsManager.planets) {
            if (planet.isNeutral()) continue;
            ArrayList<Planet> way = pathFinder.findWay(planet, targetPlanet);
            if (way == null) continue;
            if (way.size() == 0) continue;
            if (planet.hasAutoTarget() && planet.autoTarget == way.get(0)) continue;
            planet.setAutoTarget(way.get(0));
        }
    }


    public void doFinish() {
        objectsLayer.fillManager.launch(targetPlanet);
        objectsLayer.fillManager.setMatchResults(matchResults);
        Scenes.endSpectate.destroy();
        active = false;
    }


    private void doIncreaseSpeed() {
        SpeedManager speedManager = objectsLayer.gameController.speedManager;
        if (speedManager.getSpeed() >= 6) return;
        speedManager.setSpeed(speedManager.getSpeed() + 1);
    }


    private void doUnleashTheCamera() {
        cameraMovementAllowed = true;
        CameraController cameraController = objectsLayer.gameController.cameraController;
        cameraController.setBoundsEnabled(true);
    }


    private void doFocusCameraOnTarget() {
        CameraController cameraController = objectsLayer.gameController.cameraController;
        cameraController.focusOnPoint(targetPlanet.viewPosition.center);
        cameraController.setTargetZoomLevel(0.6f);
        cameraController.setBoundsEnabled(false);
    }


    private void doUpgradeAllPlanets() {
        PlanetsManager planetsManager = objectsLayer.planetsManager;
        for (Planet planet : planetsManager.planets) {
            if (planet.isNeutral()) continue;
            if (planet.isEconomic()) continue;
            planet.increaseUnitsInside(PlanetsManager.ECONOMIC_UPGRADE_PRICE);
            planetsManager.applyUpgrade(planet, PlanetType.economic);
        }
    }


    @Override
    public void onEndCreation() {

    }


    @Override
    public void moveActually() {
        if (!active) return;
    }


    @Override
    public void moveVisually() {
        if (!active) return;
        repeatMakeStep.move();
    }


    public void launch(Planet lastCapturedPlanet, MatchResults matchResults) {
        active = true;
        targetPlanet = lastCapturedPlanet;
        currentStepIndex = 0;
        cameraMovementAllowed = false;
        objectsLayer.gameController.speedManager.setSpeed(1);
        repeatMakeStep.setCountDown(30);
        setMatchResults(matchResults);
    }


    public void setMatchResults(MatchResults matchResults) {
        this.matchResults = matchResults;
    }
}
