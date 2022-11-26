package yio.tro.opacha.game.gameplay.model;

import yio.tro.opacha.SettingsManager;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.GameController;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.stuff.CircleYio;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.RectangleYio;

import java.util.ArrayList;

public class MapGenerator {

    ObjectsLayer objectsLayer;
    private final PlanetsManager planetsManager;
    CircleYio tempCircle;
    float stepDistance;
    float targetDistance;
    public RectangleYio limits;
    private final LinksManager linksManager;
    PointYio center;
    private GameController gameController;
    int quantity;
    int colors;
    ArrayList<FractionType> fractionsOrder;
    RectangleYio levelBounds;
    private PointYio minPoint;
    private PointYio maxPoint;


    public MapGenerator(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        planetsManager = objectsLayer.planetsManager;
        linksManager = objectsLayer.linksManager;
        tempCircle = new CircleYio();
        stepDistance = getPlanetRadius() / 2;
        targetDistance = 5 * getPlanetRadius();
        limits = new RectangleYio();
        center = new PointYio();
        fractionsOrder = new ArrayList<>();
        levelBounds = new RectangleYio();
        minPoint = new PointYio();
        maxPoint = new PointYio();
    }


    public void generateLinkedMap(int mapSize, int colors) {
        boolean soundPreviousState = SettingsManager.getInstance().soundEnabled;
        SettingsManager.getInstance().soundEnabled = false;
        this.colors = colors;
        updateLevelBounds();
        updateQuantity(mapSize);
        updateFractionsOrder();
        int c = 500;
        while (c > 0) {
            c--;
            generateRandomMap();
            if (!planetsManager.linkedStatusChecker.isGraphLinked()) continue;
            if (objectsLayer.impossibruDetector.isSituationImpossibru()) continue;
            break;
        }
        SettingsManager.getInstance().soundEnabled = soundPreviousState;
    }


    private void updateLevelBounds() {
        gameController = objectsLayer.gameController;
        levelBounds.set(0, 0, gameController.boundWidth, gameController.boundHeight);
    }


    private void updateFractionsOrder() {
        fractionsOrder.clear();
        FractionType[] values = FractionType.values();
        while (fractionsOrder.size() < values.length - 2) {
            int i = YioGdxGame.predictableRandom.nextInt(values.length);
            FractionType fraction = values[i];
            if (fraction == FractionType.green) continue;
            if (fraction == FractionType.neutral) continue;
            if (fractionsOrder.contains(fraction)) continue;
            fractionsOrder.add(fraction);
        }
    }


    private void updateQuantity(int mapSize) {
        switch (mapSize) {
            case 0:
                quantity = 7;
                break;
            case 1:
                quantity = 12;
                break;
            case 2:
                quantity = 17;
                break;
            case 3:
                quantity = 25;
                break;
            case 4:
                quantity = 61;
                break;
        }
    }


    public void generateRandomMap() {
        begin();
        spawnRandomNeutralPlanets();
        applyCollisions();
        centerPlanets();
        linkPlanets();
        makeSomePlanetsColored();
    }


    private void centerPlanets() {
        updateCenter();
        updateMinPoint();
        updateMaxPoint();

        double px = (minPoint.x + maxPoint.x) / 2;
        double py = (minPoint.y + maxPoint.y) / 2;

        double dx = center.x - px;
        double dy = center.y - py;

        relocateAllPlanets(dx, dy);
    }


    void relocateAllPlanets(double dx, double dy) {
        for (Planet planet : getPlanets()) {
            planet.position.center.x += dx;
            planet.position.center.y += dy;
            planet.onPositionChanged();
        }
    }


    private void updateMinPoint() {
        minPoint.setBy(center);
        for (Planet planet : getPlanets()) {
            if (planet.position.center.x < minPoint.x) {
                minPoint.x = planet.position.center.x;
            }
            if (planet.position.center.y < minPoint.y) {
                minPoint.y = planet.position.center.y;
            }
        }
    }


    private void updateMaxPoint() {
        maxPoint.setBy(center);
        for (Planet planet : getPlanets()) {
            if (planet.position.center.x > maxPoint.x) {
                maxPoint.x = planet.position.center.x;
            }
            if (planet.position.center.y > maxPoint.y) {
                maxPoint.y = planet.position.center.y;
            }
        }
    }


    private void makeSomePlanetsColored() {
        for (int i = 0; i < colors; i++) {
            Planet planet = planetsManager.findPlanetFurthestFromColoredOnes();
            if (i == 0) {
                planet.setFraction(FractionType.green);
            } else {
                planet.setFraction(fractionsOrder.get(i - 1));
            }
            planet.setUnitsInside(5.9);
        }
    }


    private void spawnRandomNeutralPlanets() {
        for (int i = 0; i < quantity; i++) {
            tempCircle.setRadius(getPlanetRadius());
            tempCircle.center.set(
                    getPlanetRadius() + YioGdxGame.predictableRandom.nextDouble() * (levelBounds.width - 2 * getPlanetRadius()),
                    getPlanetRadius() + YioGdxGame.predictableRandom.nextDouble() * (levelBounds.height - 2 * getPlanetRadius())
            );
            planetsManager.spawnPlanet(tempCircle, FractionType.neutral);
        }
    }


    private void begin() {
        gameController = objectsLayer.gameController;
        clear();
        updateLimits();
        updateCenter();
    }


    private void clear() {
        planetsManager.clear();
        linksManager.clear();
    }


    private void updateCenter() {
        center.set(
                levelBounds.x + levelBounds.width / 2,
                levelBounds.y + levelBounds.height / 2
        );
    }


    private void updateLimits() {
        limits.set(0, 0, gameController.boundWidth, gameController.boundHeight);
        limits.increase(-0.02f * GraphicsYio.width);
    }


    public void linkPlanets() {
        ArrayList<Planet> planets = getPlanets();
        for (int i = 0; i < planets.size(); i++) {
            for (int j = i + 1; j < planets.size(); j++) {
                Planet one = planets.get(i);
                Planet two = planets.get(j);
                float distance = one.position.center.distanceTo(two.position.center);
                if (distance > targetDistance + 2 * stepDistance) continue;
                linksManager.addLink(one, two);
            }
        }
    }


    private ArrayList<Planet> getPlanets() {
        return planetsManager.planets;
    }


    public void applyCollisions() {
        for (int i = 0; i < 100; i++) {
            applyStep();
        }
    }


    public void applyStep() {
        for (Planet planet : getPlanets()) {
            applyStep(planet);
        }
        applyLimits();
    }


    public void applyLimits() {
        for (Planet planet : getPlanets()) {
            CircleYio pos = planet.position;
            if (pos.center.x + pos.radius > limits.x + limits.width) {
                pos.center.x = limits.x + limits.width - pos.radius;
            }
            if (pos.center.x - pos.radius < limits.x) {
                pos.center.x = limits.x + pos.radius;
            }
            if (pos.center.y + pos.radius > limits.y + limits.height) {
                pos.center.y = limits.y + limits.height - pos.radius;
            }
            if (pos.center.y - pos.radius < limits.y) {
                pos.center.y = limits.y + pos.radius;
            }
        }
    }


    private void applyStep(Planet planet) {
        moveSlightlyToCenter(planet);
        relocateRegardingClosestPlanet(planet);
    }


    private void moveSlightlyToCenter(Planet planet) {
        float distance = planet.position.center.distanceTo(center);
        if (distance < planet.position.radius) return;

        double angle = planet.position.center.angleTo(center);
        planet.position.center.relocateRadial(stepDistance / 5, angle);
        planet.onPositionChanged();
    }


    private void relocateRegardingClosestPlanet(Planet planet) {
        Planet closestPlanet = planetsManager.findClosestPlanet(planet.position.center, planet);
        float distance = closestPlanet.position.center.distanceTo(planet.position.center);
        double angle = planet.position.center.angleTo(closestPlanet.position.center);
        float step = stepDistance;
        if (distance < targetDistance) {
            step *= -1;
        }
        planet.position.center.relocateRadial(step, angle);
        planet.onPositionChanged();
    }


    private float getPlanetRadius() {
        return objectsLayer.planetsManager.getPlanetRadius();
    }
}
