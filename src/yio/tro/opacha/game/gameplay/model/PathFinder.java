package yio.tro.opacha.game.gameplay.model;

import java.util.ArrayList;

public class PathFinder {

    PlanetsManager planetsManager;
    ArrayList<Planet> propagationList;
    ArrayList<Planet> resultList;
    Planet start;
    Planet target;


    public PathFinder(PlanetsManager planetsManager) {
        this.planetsManager = planetsManager;
        propagationList = new ArrayList<>();
        resultList = new ArrayList<>();
    }


    public ArrayList<Planet> findWay(Planet start, Planet target) {
        resultList.clear();
        propagationList.clear();
        if (start == target) return resultList;

        this.start = start;
        this.target = target;

        preparePlanets();
        addToPropagationList(start, null);

        while (propagationList.size() > 0 && target.algoPointer == null) {
            applyStep();
        }
        if (target.algoPointer == null) return null;

        collectWay();

        return resultList;
    }


    private void collectWay() {
        resultList.clear();
        Planet tempPlanet = target;
        while (tempPlanet != start) {
            resultList.add(0, tempPlanet);
            tempPlanet = tempPlanet.algoPointer;
        }
    }


    private void applyStep() {
        Planet planet = propagationList.get(0);
        propagationList.remove(0);
        for (Link adjoinedLink : planet.adjoinedLinks) {
            Planet oppositePlanet = adjoinedLink.getOppositePlanet(planet);
            if (oppositePlanet.flag) continue;
            addToPropagationList(oppositePlanet, planet);
        }
    }


    private void addToPropagationList(Planet planet, Planet parent) {
        propagationList.add(planet);
        planet.flag = true;
        planet.algoPointer = parent;
    }


    private void preparePlanets() {
        for (Planet planet : getPlanets()) {
            planet.flag = false;
            planet.algoPointer = null;
        }
    }


    private ArrayList<Planet> getPlanets() {
        return planetsManager.planets;
    }
}
