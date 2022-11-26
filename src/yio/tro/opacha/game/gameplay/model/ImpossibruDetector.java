package yio.tro.opacha.game.gameplay.model;

import yio.tro.opacha.game.gameplay.ObjectsLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImpossibruDetector {

    ObjectsLayer objectsLayer;
    ArrayList<Planet> propagationList;
    ArrayList<Planet> tempList;
    private final PlanetsManager planetsManager;
    HashMap<FractionType, Integer> quantitiesMap;
    HashMap<FractionType, Integer> conflictsMap;
    ArrayList<Link> tempConflictList;


    public ImpossibruDetector(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        planetsManager = objectsLayer.planetsManager;
        propagationList = new ArrayList<>();
        quantitiesMap = new HashMap<>();
        tempList = new ArrayList<>();
        conflictsMap = new HashMap<>();
        tempConflictList = new ArrayList<>();
    }


    public boolean isSituationImpossibru() {
        if (getPlanets().size() == 0) return true;
        if (checkForOneVsOneBadStart()) return true;

        prepare();
        while (propagationList.size() > 0) {
            performIteration();
        }

        updateQuantitiesMap();
        updateTempConflictList();
        updateConflictsMap();

        return isQuantityBad() || isConflictSituationBad();
    }


    private boolean isConflictSituationBad() {
        int greenConflictValue = conflictsMap.get(FractionType.green);
        int maxConflictValueWithoutGreen = getMaxConflictValueWithoutGreen();
        int fractionsWithOneConflict = countFractionsWithOneConflict();
        if (greenConflictValue == maxConflictValueWithoutGreen && fractionsWithOneConflict >= 2) return true;
        return greenConflictValue > maxConflictValueWithoutGreen;
    }


    private int countFractionsWithOneConflict() {
        int c = 0;
        for (Map.Entry<FractionType, Integer> entry : conflictsMap.entrySet()) {
            if (entry.getKey() == FractionType.green) continue;
            if (entry.getValue() != 1) continue;
            c++;
        }
        return c;
    }


    private int getMaxConflictValueWithoutGreen() {
        int maxValue = -1;
        for (Map.Entry<FractionType, Integer> entry : conflictsMap.entrySet()) {
            if (entry.getKey() == FractionType.green) continue;
            int value = entry.getValue();
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }


    private void updateConflictsMap() {
        conflictsMap.clear();
        for (FractionType fractionType : FractionType.values()) {
            if (fractionType == FractionType.neutral) continue;
            conflictsMap.put(fractionType, 0);
        }
        for (Link link : tempConflictList) {
            increaseValueInConflictMap(link.one.algoFraction);
            increaseValueInConflictMap(link.two.algoFraction);
        }
    }


    private void increaseValueInConflictMap(FractionType fractionType) {
        int value = conflictsMap.get(fractionType);
        conflictsMap.put(fractionType, value + 1);
    }


    private void updateTempConflictList() {
        tempConflictList.clear();
        for (Link link : objectsLayer.linksManager.links) {
            if (link.one.algoFraction == null) continue;
            if (link.two.algoFraction == null) continue;
            if (link.one.algoFraction == link.two.algoFraction) continue;
            onConflictDetected(link);
        }
    }


    private void onConflictDetected(Link link) {
        if (isSimilarConflictAlreadyDetected(link)) return;
        tempConflictList.add(link);
    }


    private boolean isSimilarConflictAlreadyDetected(Link link) {
        for (Link conflict : tempConflictList) {
            if (areConflictsSimilar(conflict, link)) return true;
        }
        return false;
    }


    private boolean areConflictsSimilar(Link a, Link b) {
        FractionType f1 = a.one.algoFraction;
        FractionType f2 = a.two.algoFraction;
        if (b.one.algoFraction == f1 && b.two.algoFraction == f2) return true;
        if (b.one.algoFraction == f2 && b.two.algoFraction == f1) return true;
        return false;
    }


    public void doShowResultsInConsole() {
        System.out.println();
        System.out.println("ImpossibruDetector.doShowResultsInConsole");
        System.out.println("isSituationImpossibru() = " + isSituationImpossibru());
        showQuantitiesMapInConsole();
        showConflictsMapInConsole();
    }


    private boolean isQuantityBad() {
        int greenQuantity = getQuantity(FractionType.green);
        if (greenQuantity == getMinQuantity()) return true;
        return (greenQuantity < (getMaxQuantity() + 1) / 2);
    }


    private boolean checkForOneVsOneBadStart() {
        for (Planet planet : planetsManager.planets) {
            if (planet.isNeutral()) continue;
            if (planet.fraction.ordinal() > 2) return false;
            int linksQuantity = planet.adjoinedLinks.size();
            if (planet.fraction == FractionType.green && linksQuantity > 1) return false;
            if (planet.fraction == FractionType.red && linksQuantity < 2) return false;
        }
        return true;
    }


    private int getQuantity(FractionType fractionType) {
        if (!quantitiesMap.containsKey(fractionType)) return 0;
        return quantitiesMap.get(fractionType);
    }


    private int getMinQuantity() {
        int min = -1;
        for (Map.Entry<FractionType, Integer> entry : quantitiesMap.entrySet()) {
            if (entry.getKey() == null) continue;
            int value = entry.getValue();
            if (min == -1 || value < min) {
                min = value;
            }
        }
        return min;
    }


    private int getMaxQuantity() {
        int max = -1;
        for (int value : quantitiesMap.values()) {
            if (max == -1 || value > max) {
                max = value;
            }
        }
        return max;
    }


    private void showConflictsMapInConsole() {
        System.out.println("Conflicts:");
        for (Map.Entry<FractionType, Integer> entry : conflictsMap.entrySet()) {
            System.out.println("    " + entry.getKey() + ": " + entry.getValue());
        }
    }


    private void showQuantitiesMapInConsole() {
        System.out.println("Quantities:");
        for (Map.Entry<FractionType, Integer> entry : quantitiesMap.entrySet()) {
            System.out.println("    " + entry.getKey() + ": " + entry.getValue());
        }
    }


    private void updateQuantitiesMap() {
        quantitiesMap.clear();
        for (Planet planet : getPlanets()) {
            FractionType algoFraction = planet.algoFraction;
            if (quantitiesMap.containsKey(algoFraction)) {
                quantitiesMap.put(algoFraction, quantitiesMap.get(algoFraction) + 1);
            } else {
                quantitiesMap.put(algoFraction, 1);
            }
        }
    }


    private ArrayList<Planet> getPlanets() {
        return planetsManager.planets;
    }


    private void performIteration() {
        Planet planet = propagationList.get(0);
        propagationList.remove(0);
        if (!canPlanetPropagate(planet)) return;

        for (Link adjoinedLink : planet.adjoinedLinks) {
            Planet oppositePlanet = adjoinedLink.getOppositePlanet(planet);
            if (oppositePlanet.algoFraction != null) continue;
            addPlanetToPropagationList(oppositePlanet, planet);
        }
    }


    private boolean canPlanetPropagate(Planet planet) {
        for (Link adjoinedLink : planet.adjoinedLinks) {
            Planet oppositePlanet = adjoinedLink.getOppositePlanet(planet);
            if (oppositePlanet.algoFraction == null) continue;
            if (oppositePlanet.algoFraction == planet.algoFraction) continue;
            return false;
        }
        return true;
    }


    private void addPlanetToPropagationList(Planet planet, Planet parent) {
        planet.algoFraction = parent.algoFraction;
        propagationList.add(planet);
    }


    private void prepare() {
        propagationList.clear();
        for (Planet planet : getPlanets()) {
            planet.algoFraction = null;
        }

        for (Planet planet : getPlanets()) {
            if (planet.isNeutral()) continue;
            planet.algoFraction = planet.fraction;
            addPlanetToPropagationList(planet, planet);
        }

        shiftPlayerPlanetsToEndOfPropagationList();
    }


    private void shiftPlayerPlanetsToEndOfPropagationList() {
        tempList.clear();
        for (int i = propagationList.size() - 1; i >= 0; i--) {
            Planet planet = propagationList.get(i);
            if (!planet.isControlledByPlayer()) continue;
            tempList.add(planet);
            propagationList.remove(planet);
        }
        for (Planet planet : tempList) {
            addPlanetToPropagationList(planet, planet);
        }
    }
}
