package yio.tro.opacha.game.gameplay.model;

import yio.tro.opacha.SoundManager;
import yio.tro.opacha.Yio;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.export_import.SavableYio;
import yio.tro.opacha.game.gameplay.IGameplayManager;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.game.gameplay.particles.PaViewType;
import yio.tro.opacha.game.gameplay.particles.ParticlesManager;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.*;

import java.util.ArrayList;

public class PlanetsManager implements IGameplayManager, SavableYio {

    public static final int ECONOMIC_UPGRADE_PRICE = 10;
    public static final int DEFENSIVE_UPGRADE_PRICE = 10;
    ObjectsLayer objectsLayer;
    public ArrayList<Planet> planets;
    RepeatYio<PlanetsManager> repeatApplyCollisions;
    public LinkedStatusChecker linkedStatusChecker;
    public PathFinder pathFinder;
    public Planet lastCapturedPlanet;
    PointYio tempPoint;
    RepeatYio<PlanetsManager> repeatHideButtons;


    public PlanetsManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        planets = new ArrayList<>();
        linkedStatusChecker = new LinkedStatusChecker(this);
        pathFinder = new PathFinder(this);
        tempPoint = new PointYio();
        initRepeats();
    }


    private void initRepeats() {
        repeatApplyCollisions = new RepeatYio<PlanetsManager>(this, 5) {
            @Override
            public void performAction() {
                parent.objectsLayer.mapGenerator.applyStep();
            }
        };
        repeatHideButtons = new RepeatYio<PlanetsManager>(this, 30) {
            @Override
            public void performAction() {
                parent.checkToHidePlanetButtons();
            }
        };
    }


    @Override
    public void defaultValues() {
        clear();
        lastCapturedPlanet = null;
    }


    public void clear() {
        planets.clear();
    }


    @Override
    public void onEndCreation() {
        for (Planet planet : planets) {
            planet.onEndCreation();
        }
    }


    @Override
    public void moveActually() {
        movePlanetsActually();
    }


    public Planet spawnPlanet(CircleYio pos, FractionType fractionType) {
        Planet freshPlanet = new Planet(this);
        planets.add(freshPlanet);
        freshPlanet.setFraction(fractionType);
        freshPlanet.setPosition(pos);
        freshPlanet.onBuilt();
        return freshPlanet;
    }


    public void removePlanet(Planet planet) {
        planets.remove(planet);
    }


    public Planet getRandomPlanet() {
        int size = planets.size();
        int index = YioGdxGame.random.nextInt(size);
        return planets.get(index);
    }


    public Planet getPlanetNearFrameCenter() {
        RectangleYio frame = objectsLayer.gameController.cameraController.getFrame();
        tempPoint.set(frame.x + frame.width / 2, frame.y + frame.height / 2);
        return getClosestPlanet(tempPoint);
    }


    public Planet getClosestPlanet(PointYio pointYio) {
        Planet bestPlanet = null;
        double minDistance = 0;
        for (Planet planet : planets) {
            double currentDistance = pointYio.distanceTo(planet.viewPosition.center);
            if (bestPlanet == null || currentDistance < minDistance) {
                bestPlanet = planet;
                minDistance = currentDistance;
            }
        }
        return bestPlanet;
    }


    public float getPlanetRadius() {
        return 0.05f * GraphicsYio.width;
    }


    public void onClick(PointYio touchPoint) {
        Planet touchedPlanet = findTouchedPlanet(touchPoint);
        if (touchedPlanet == null) {
            deselect();
            return;
        }

        onClickedOnPlanet(touchedPlanet);
    }


    private void onClickedOnPlanet(Planet touchedPlanet) {
        if (checkToSendUnits(touchedPlanet)) return;
        if (checkToSelectPlanet(touchedPlanet)) return;
        if (checkToCancelAutoTarget(touchedPlanet)) return;

        deselect();
    }


    private boolean checkToCancelAutoTarget(Planet touchedPlanet) {
        Planet currentlySelectedPlanet = getCurrentlySelectedPlanet();
        if (currentlySelectedPlanet == null) return false;
        if (touchedPlanet != currentlySelectedPlanet) return false;
        if (!touchedPlanet.isControlledByPlayer()) return false;
        if (!touchedPlanet.hasAutoTarget()) return false;

        touchedPlanet.setAutoTarget(null);
        deselect();
        return true;
    }


    public FractionType getAliveFraction(FractionType ignoredFraction) {
        for (Planet planet : planets) {
            if (planet.isNeutral()) continue;
            if (ignoredFraction != null && planet.fraction == ignoredFraction) continue;
            return planet.fraction;
        }

        return null;
    }


    public boolean areThereAtLeastTwoAliveFractions() {
        FractionType firstFraction = getAliveFraction(null);
        if (firstFraction == null) return false;

        FractionType secondFraction = getAliveFraction(firstFraction);
        if (secondFraction == null) return false;

        return true;
    }


    public void releaseHighlights() {
        for (Planet planet : planets) {
            planet.setHoldHighlight(false);
        }
    }


    private boolean checkToSelectPlanet(Planet touchedPlanet) {
        if (getCurrentlySelectedPlanet() != null) return false;
        if (!touchedPlanet.isControlledByPlayer()) return false;

        deselect();
        touchedPlanet.select();
        Scenes.selectionOverlay.setSelectedPlanet(touchedPlanet);
        Scenes.selectionOverlay.create();
        return true;
    }


    public void onLongTappedPlanet(Planet planet) {
        if (!planet.isControlledByPlayer()) return;

        deselect();
        planet.select();
        planet.setHalvedSelectionMode(true);
        Scenes.selectionOverlay.setSelectedPlanet(planet);
        Scenes.selectionOverlay.create();
    }


    private boolean checkToSendUnits(Planet touchedPlanet) {
        Planet currentlySelectedPlanet = getCurrentlySelectedPlanet();
        if (currentlySelectedPlanet == null) return false;
        if (!currentlySelectedPlanet.isLinkedTo(touchedPlanet)) return false;
        if ((int) currentlySelectedPlanet.unitsInside == 0) return false;
        if (currentlySelectedPlanet == touchedPlanet) return false;

        sendUnits(currentlySelectedPlanet, touchedPlanet);
        deselect();

        return true;
    }


    public boolean isFractionAlive(FractionType fractionType) {
        for (Planet planet : planets) {
            if (planet.fraction == fractionType) return true;
        }
        return false;
    }


    public void sendUnits(Planet start, Planet target) {
        int unitsToLaunch = getUnitsToLaunch(start);
        if (unitsToLaunch == 0) return;
        objectsLayer.unitManager.launchUnit(start, target, unitsToLaunch);
        start.decreaseUnitsInside(unitsToLaunch);
        start.applyPopEffect();
    }


    public int calculateThreat(Planet planet) {
        int threat = 0;
        for (Link adjoinedLink : planet.adjoinedLinks) {
            ArrayList<Unit> walkersToTarget = adjoinedLink.getWalkersToTarget(planet);
            for (Unit unit : walkersToTarget) {
                if (unit.fraction == planet.fraction) continue;
                threat += unit.value;
            }
        }
        return threat;
    }


    private int getUnitsToLaunch(Planet start) {
        int value = (int) start.unitsInside;

        if (start.halvedSelectionMode) {
            return value / 2;
        }

        return value;
    }


    public Planet getCurrentlySelectedPlanet() {
        for (Planet planet : planets) {
            if (!planet.selectionEngineYio.isSelected()) continue;
            if (!planet.isControlledByPlayer()) return null;
            return planet;
        }
        return null;
    }


    public void applyUpgrade(Planet planet, PlanetType planetType) {
        planet.setType(planetType);
        SoundManager.playSound(SoundManager.score);

        ParticlesManager particlesManager = objectsLayer.particlesManager;
        PaViewType paViewType = particlesManager.convertFractionIntoPaViewType(planet.fraction);
        particlesManager.spawnExplosion(planet.viewPosition.center, 2, paViewType);
        planet.applyPopEffect();

        switch (planetType) {
            case defensive:
                planet.decreaseUnitsInside(DEFENSIVE_UPGRADE_PRICE);
                planet.setCurrentDefense(planet.currentDefense + DEFENSIVE_UPGRADE_PRICE);
                break;
            case economic:
                planet.decreaseUnitsInside(ECONOMIC_UPGRADE_PRICE);
                break;
        }
    }


    public void onEconomicUpgradeButtonClicked() {
        Planet currentlySelectedPlanet = getCurrentlySelectedPlanet();
        if (currentlySelectedPlanet == null) return;
        if (!canBeUpgradedToEconomic(currentlySelectedPlanet)) return;

        applyUpgrade(currentlySelectedPlanet, PlanetType.economic);
        Scenes.selectionOverlay.destroy();
        deselect();
        currentlySelectedPlanet.forceDeselection();
    }


    public void onDefensiveUpgradeButtonClicked() {
        Planet currentlySelectedPlanet = getCurrentlySelectedPlanet();
        if (currentlySelectedPlanet == null) return;
        if (!canBeUpgradedToDefensive(currentlySelectedPlanet)) return;

        applyUpgrade(currentlySelectedPlanet, PlanetType.defensive);
        Scenes.selectionOverlay.destroy();
        deselect();
        currentlySelectedPlanet.forceDeselection();
    }


    public boolean canBeUpgradedToEconomic(Planet planet) {
        return !planet.isEconomic() && (int) planet.unitsInside >= ECONOMIC_UPGRADE_PRICE;
    }


    public boolean canBeUpgradedToDefensive(Planet planet) {
        return !planet.isDefensive() && (int) planet.unitsInside >= DEFENSIVE_UPGRADE_PRICE;
    }


    public void deselect() {
        for (Planet planet : planets) {
            planet.deselect();
        }
        Scenes.selectionOverlay.destroy();
    }


    public Planet findTouchedPlanet(PointYio touchPoint) {
        Planet closestPlanet = findClosestPlanet(touchPoint, null);
        if (closestPlanet == null) return null;
        if (!closestPlanet.isTouchedBy(touchPoint, 1)) return null;
        return closestPlanet;
    }


    public Planet findAdjoinedPlanetClosestToAngle(Planet start, double targetAngle) {
        Planet result = null;
        double minAngle = 0;
        double currentAngle;
        for (Link adjoinedLink : start.adjoinedLinks) {
            Planet oppositePlanet = adjoinedLink.getOppositePlanet(start);
            double a = start.viewPosition.center.angleTo(oppositePlanet.viewPosition.center);
            currentAngle = Yio.distanceBetweenAngles(a, targetAngle);
            if (result == null || currentAngle < minAngle) {
                result = oppositePlanet;
                minAngle = currentAngle;
            }
        }
        return result;
    }


    public Planet findClosestPlanet(PointYio pointYio, Planet ignoredPlanet) {
        Planet result = null;
        double minDistance = 0;
        double currentDistance;
        for (Planet planet : planets) {
            if (ignoredPlanet != null && planet == ignoredPlanet) continue;
            currentDistance = planet.position.center.distanceTo(pointYio);
            if (result == null || currentDistance < minDistance) {
                result = planet;
                minDistance = currentDistance;
            }
        }
        return result;
    }


    public Planet findPlanetFurthestFromColoredOnes() {
        if (!isThereAtLeastOneColoredPlanet()) {
            return findPlanetWithLeastAmountOnLinks();
        }

        Planet result = null;
        double maxDistance = 0;
        double currentDistance;
        for (Planet planet : planets) {
            currentDistance = getDistanceToClosestColoredPlanet(planet);
            if (result == null || currentDistance > maxDistance) {
                result = planet;
                maxDistance = currentDistance;
            }
        }
        return result;
    }


    public double getDistanceToClosestColoredPlanet(Planet planet) {
        Planet closestColoredPlanet = findClosestColoredPlanet(planet.position.center);
        if (closestColoredPlanet == null) return 0;
        return planet.position.center.distanceTo(closestColoredPlanet.position.center);
    }


    public boolean isThereAtLeastOneColoredPlanet() {
        for (Planet planet : planets) {
            if (planet.isNeutral()) continue;
            return true;
        }
        return false;
    }


    public Planet findPlanetWithLeastAmountOnLinks() {
        Planet result = null;
        int minValue = -1;
        int currentValue;
        for (Planet planet : planets) {
            currentValue = planet.adjoinedLinks.size();
            if (result == null || currentValue < minValue) {
                result = planet;
                minValue = currentValue;
            }
        }
        return result;
    }


    public Planet findClosestColoredPlanet(PointYio pointYio) {
        Planet result = null;
        double minDistance = 0;
        double currentDistance;
        for (Planet planet : planets) {
            if (planet.isNeutral()) continue;
            currentDistance = planet.position.center.distanceTo(pointYio);
            if (result == null || currentDistance < minDistance) {
                result = planet;
                minDistance = currentDistance;
            }
        }
        return result;
    }


    public int numberOfAdjoinedEnemyPlanets(Planet planet) {
        int c = 0;
        for (Link adjoinedLink : planet.adjoinedLinks) {
            Planet oppositePlanet = adjoinedLink.getOppositePlanet(planet);
            if (oppositePlanet.isNeutral()) continue;
            if (oppositePlanet.fraction == planet.fraction) continue;
            c++;
        }
        return c;
    }


    public int getNumberOfEconomicPlanets(FractionType fractionType) {
        int c = 0;
        for (Planet planet : planets) {
            if (planet.fraction != fractionType) continue;
            if (!planet.isEconomic()) continue;
            c++;
        }
        return c;
    }


    public int calculatePotentialThreat(Planet planet) {
        int sum = 0;
        for (Link adjoinedLink : planet.adjoinedLinks) {
            Planet oppositePlanet = adjoinedLink.getOppositePlanet(planet);
            if (oppositePlanet.isNeutral()) continue;
            if (oppositePlanet.fraction == planet.fraction) continue;
            sum += oppositePlanet.unitsInside;
        }
        return sum;
    }


    @Override
    public String saveToString() {
        StringBuilder builder = new StringBuilder();
        for (Planet planet : planets) {
            builder.append(planet.saveToString()).append(",");
        }
        return builder.toString();
    }


    private void movePlanetsActually() {
        for (Planet planet : planets) {
            planet.moveActually();
        }
    }


    void checkToHidePlanetButtons() {
        if (!Scenes.selectionOverlay.isCurrentlyVisible()) return;
        if (getCurrentlySelectedPlanet() != null) return;
        Scenes.selectionOverlay.destroy();
    }


    @Override
    public void moveVisually() {
        movePlanetsVisually();
        repeatHideButtons.move();
    }


    private void movePlanetsVisually() {
        for (Planet planet : planets) {
            planet.moveVisually();
        }
    }
}
