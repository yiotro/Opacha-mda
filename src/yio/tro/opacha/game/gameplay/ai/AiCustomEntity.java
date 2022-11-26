package yio.tro.opacha.game.gameplay.ai;

import yio.tro.opacha.game.Difficulty;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.game.gameplay.model.FractionType;
import yio.tro.opacha.game.gameplay.model.Link;
import yio.tro.opacha.game.gameplay.model.Planet;
import yio.tro.opacha.game.gameplay.model.PlanetType;
import yio.tro.opacha.stuff.RepeatYio;

import java.util.ArrayList;

public class AiCustomEntity extends AbstractAiEntity {

    RepeatYio<AiCustomEntity> repeatUpdateSafeAutoTargets;
    RepeatYio<AiCustomEntity> repeatCheckForCoordinatedAttack;


    public AiCustomEntity(ObjectsLayer objectsLayer) {
        super(objectsLayer);
        initRepeats();
    }


    private void initRepeats() {
        repeatUpdateSafeAutoTargets = new RepeatYio<AiCustomEntity>(this, 10) {
            @Override
            public void performAction() {
                parent.updateSafeAutoTargets();
            }
        };
        repeatCheckForCoordinatedAttack = new RepeatYio<AiCustomEntity>(this, 10) {
            @Override
            public void performAction() {
                parent.checkForCoordinatedAttack();
            }
        };
    }


    private void updatePlanetsData() {
        updateSafetyStatuses();
        updateRequestPriorities();
        updateThreats();
    }


    private void updateThreats() {
        for (Planet planet : getPlanetsList()) {
            if (isBeyondCommand(planet)) continue;
            if (planet.aiData.safe) {
                planet.aiData.threat = 0;
                continue;
            }
            planet.aiData.threat = getPlanetsManager().calculateThreat(planet);
        }
    }


    private void updateRequestPriorities() {
        for (Planet planet : getPlanetsList()) {
            if (isBeyondCommand(planet)) continue;
            planet.aiData.requestPriority = 0;
            if (planet.type == PlanetType.empty && !isEconomicGrowthStopped()) {
                planet.aiData.requestPriority++;
            }
            planet.aiData.requestPriority += planet.countAdjoinedPlanetsByFraction(FractionType.neutral);
        }
    }


    private void updateSafeAutoTargets() {
        if (difficulty == Difficulty.easy) return;
        if (dynamicPower < 0.25) return;

        Planet planetInDanger = findAnyPlanetInDanger();
        if (planetInDanger == null) return;

        for (Planet planet : getPlanetsList()) {
            if (isBeyondCommand(planet)) continue;
            if (planet.isDefensive()) continue;
            if (planet.aiData.requestPriority > 0) continue;
            Planet adjoinedPlanet = findAdjoinedPlanetWithImmediateRequest(planet);
            if (adjoinedPlanet != null) continue;
            ArrayList<Planet> way = getPathFinder().findWay(planet, planetInDanger);
            if (way == null) continue;
            if (way.size() == 0) continue;
            if (planet.hasAutoTarget() && planet.autoTarget == way.get(0)) continue;
            planet.setAutoTarget(way.get(0));
            onActionPerformed();
        }
    }


    private void checkForCoordinatedAttack() {
        if (difficulty != Difficulty.hard) return;

        Planet target = pickTargetForCoordinatedAttack();
        if (target == null) return;

        for (Link adjoinedLink : target.adjoinedLinks) {
            Planet oppositePlanet = adjoinedLink.getOppositePlanet(target);
            if (isBeyondCommand(oppositePlanet)) continue;
            if (isThreatTooBig(oppositePlanet)) continue;
            launchUnits(oppositePlanet, target);
            onActionPerformed();
        }
    }


    private Planet pickTargetForCoordinatedAttack() {
        for (Planet planet : getPlanetsList()) {
            if (!isBeyondCommand(planet)) continue;
            if (calculateAttackPotential(planet) < planet.getFullHp() + 8) continue;
            return planet;
        }
        return null;
    }


    private int calculateAttackPotential(Planet target) {
        int value = 0;
        for (Link adjoinedLink : target.adjoinedLinks) {
            Planet oppositePlanet = adjoinedLink.getOppositePlanet(target);
            if (isBeyondCommand(oppositePlanet)) continue;
            if (isThreatTooBig(oppositePlanet)) continue;
            value += (int) oppositePlanet.unitsInside;
        }
        return value;
    }


    private Planet findAnyPlanetInDanger() {
        for (Planet planet : getPlanetsList()) {
            if (isBeyondCommand(planet)) continue;
            if (planet.aiData.safe) continue;
            return planet;
        }
        return null;
    }


    private void updateSafetyStatuses() {
        for (Planet planet : getPlanetsList()) {
            if (isBeyondCommand(planet)) continue;
            planet.aiData.numberOfAdjoinedEnemies = planet.getNumberOfAdjoinedEnemies();
            planet.aiData.safe = (planet.aiData.numberOfAdjoinedEnemies == 0);
        }
    }


    @Override
    protected void perform() {
        updatePlanetsData();
        repeatUpdateSafeAutoTargets.move();
        repeatCheckForCoordinatedAttack.move();
        checkToUpgradePlanets();
        checkToCapturePlanets();
        checkForImmediateAutoTargets();
    }


    private void checkForImmediateAutoTargets() {
        for (Planet planet : getPlanetsList()) {
            if (isBeyondCommand(planet)) continue;
            if (planet.aiData.requestPriority > 0) continue;
            if (!planet.aiData.safe) continue;
            if (planet.isDefensive()) continue;
            assignImmediateAutoTarget(planet);
        }
    }


    private void assignImmediateAutoTarget(Planet planet) {
        Planet adjoinedPlanet = findAdjoinedPlanetWithImmediateRequest(planet);
        if (adjoinedPlanet == null) return;
        if (adjoinedPlanet.hasAutoTarget() && adjoinedPlanet.autoTarget == planet) return;
        planet.setAutoTarget(adjoinedPlanet);
        onActionPerformed();
    }


    private Planet findAdjoinedPlanetWithImmediateRequest(Planet planet) {
        Planet resultPlanet = null;
        for (Link adjoinedLink : planet.adjoinedLinks) {
            Planet oppositePlanet = adjoinedLink.getOppositePlanet(planet);
            if (isBeyondCommand(oppositePlanet)) continue;
            if (oppositePlanet.aiData.requestPriority == 0) continue;
            if (resultPlanet == null || oppositePlanet.aiData.requestPriority > resultPlanet.aiData.requestPriority) {
                resultPlanet = oppositePlanet;
            }
        }
        return resultPlanet;
    }


    void checkToUpgradePlanets() {
        if (difficulty == Difficulty.easy) return;
        checkForEconomicUpgrades();
        checkForDefensiveUpgrades();
    }


    private void checkForDefensiveUpgrades() {
        if (difficulty != Difficulty.hard) return;
        for (Planet planet : getPlanetsList()) {
            if (isBeyondCommand(planet)) continue;
            if (!getPlanetsManager().canBeUpgradedToDefensive(planet)) continue;
            if (planet.aiData.safe) continue;
            if (getPlanetsManager().numberOfAdjoinedEnemyPlanets(planet) < 2) continue;
            if (getPlanetsManager().calculatePotentialThreat(planet) + planet.aiData.threat < 0.7 * planet.getFullHp())
                continue;
            if (!isActionAllowed()) continue;
            getPlanetsManager().applyUpgrade(planet, PlanetType.defensive);
            planet.setAutoTarget(null);
            onActionPerformed();
        }
    }


    private void checkForEconomicUpgrades() {
        if (isEconomicGrowthStopped()) return;
        for (Planet planet : getPlanetsList()) {
            if (isBeyondCommand(planet)) continue;
            if (!getPlanetsManager().canBeUpgradedToEconomic(planet)) continue;
            if (!planet.aiData.safe) continue;
            if (!isActionAllowed()) continue;
            getPlanetsManager().applyUpgrade(planet, PlanetType.economic);
            onActionPerformed();
        }
    }


    private boolean isEconomicGrowthStopped() {
        if (difficulty != Difficulty.normal) return false;
        int numberOfEconomicPlanets = getPlanetsManager().getNumberOfEconomicPlanets(fractionType);
        int growthLimit = Math.min(1 + (int) (2 * dynamicPower), getPlanetsList().size() / 6);
        return numberOfEconomicPlanets > growthLimit;
    }


    void checkToCapturePlanets() {
        for (Planet planet : getPlanetsList()) {
            if (isBeyondCommand(planet)) continue;
            if (!isActionAllowed()) continue;
            checkForAttackFromPlanet(planet);
        }
    }


    void checkForAttackFromPlanet(Planet planet) {
        if (isThreatTooBig(planet)) return;
        for (Link adjoinedLink : planet.adjoinedLinks) {
            Planet oppositePlanet = adjoinedLink.getOppositePlanet(planet);
            checkForAttack(planet, oppositePlanet);
        }
    }


    private boolean isThreatTooBig(Planet planet) {
        return planet.aiData.threat > planet.getFullHp() / 2;
    }


    void checkForAttack(Planet start, Planet target) {
        if (start.fraction == target.fraction) return;
        if ((int) start.unitsInside < target.getFullHp()) return;
        if (!target.isNeutral() && (int) start.unitsInside < target.getFullHp() + 5) return;
        launchUnits(start, target);
        onActionPerformed();
    }
}
