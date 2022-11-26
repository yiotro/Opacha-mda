package yio.tro.opacha.game.gameplay.model;

import java.util.ArrayList;

public class LinkedStatusChecker {

    PlanetsManager planetsManager;
    ArrayList<Planet> propagationList;


    public LinkedStatusChecker(PlanetsManager planetsManager) {
        this.planetsManager = planetsManager;
        propagationList = new ArrayList<>();
    }


    public boolean isGraphLinked() {
        if (planetsManager.planets.size() < 1) return true;

        prepareFlags();
        preparePropagationList();

        while (propagationList.size() > 0) {
            applyStep();
        }

        return areAllPlanetsFlagged();
    }


    public int countIslands() {
        if (planetsManager.planets.size() < 1) return 0;

        int c = 0;
        prepareFlags();

        while (true) {
            c++;
            preparePropagationList();
            while (propagationList.size() > 0) {
                applyStep();
            }
            if (areAllPlanetsFlagged()) break;
        }

        return c;
    }


    private void applyStep() {
        Planet planet = propagationList.get(0);
        propagationList.remove(0);
        propagate(planet);
    }


    private void propagate(Planet planet) {
        for (Link adjoinedLink : planet.adjoinedLinks) {
            Planet oppositePlanet = adjoinedLink.getOppositePlanet(planet);
            if (oppositePlanet.flag) continue;
            addToPropagationList(oppositePlanet);
        }
    }


    private boolean areAllPlanetsFlagged() {
        for (Planet planet : planetsManager.planets) {
            if (!planet.flag) return false;
        }
        return true;
    }


    private void preparePropagationList() {
        propagationList.clear();
        addToPropagationList(getFirstNotFlaggedPlanet());
    }


    private Planet getFirstNotFlaggedPlanet() {
        for (Planet planet : planetsManager.planets) {
            if (planet.flag) continue;
            return planet;
        }
        return null;
    }


    private void addToPropagationList(Planet planet) {
        propagationList.add(planet);
        planet.flag = true;
    }


    private void prepareFlags() {
        for (Planet planet : planetsManager.planets) {
            planet.flag = false;
        }
    }
}
