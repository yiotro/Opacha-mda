package yio.tro.opacha.game.export_import;

import yio.tro.opacha.game.*;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.game.gameplay.model.*;
import yio.tro.opacha.stuff.CircleYio;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;

public class ImportManager {

    GameController gameController;
    private String source;
    private ObjectsLayer objectsLayer;
    CircleYio tempCircle;
    PointYio tempPoint;


    public ImportManager(GameController gameController) {
        this.gameController = gameController;
        tempCircle = new CircleYio();
        tempPoint = new PointYio();
    }


    public void perform(String src) {
        source = src;
        objectsLayer = gameController.objectsLayer;

        applyGameRules();
        applyPlanets();
        applyLinks();

        finish();
    }


    private void applyLinks() {
        String linksSection = getSection("links");
        if (linksSection == null) return;

        for (String token : linksSection.split(",")) {
            applySingleLink(token);
        }
    }


    private void applySingleLink(String token) {
        String[] split = token.split(" ");
        FractionType fractionType = FractionType.valueOf(split[0]);
        double rx1 = Double.valueOf(split[1]);
        double ry1 = Double.valueOf(split[2]);
        double rx2 = Double.valueOf(split[3]);
        double ry2 = Double.valueOf(split[4]);

        PlanetsManager planetsManager = objectsLayer.planetsManager;
        tempPoint.set(rx1 * GraphicsYio.width, ry1 * GraphicsYio.width);
        Planet one = planetsManager.findTouchedPlanet(tempPoint);
        tempPoint.set(rx2 * GraphicsYio.width, ry2 * GraphicsYio.width);
        Planet two = planetsManager.findTouchedPlanet(tempPoint);

        Link link = objectsLayer.linksManager.addLink(one, two);
        link.setFractionType(fractionType);
    }


    private void applyPlanets() {
        String planetsSection = getSection("planets");
        if (planetsSection == null) return;

        for (String token : planetsSection.split(",")) {
            applySinglePlanet(token);
        }
    }


    private void applySinglePlanet(String token) {
        String[] split = token.split(" ");
        FractionType fractionType = FractionType.valueOf(split[0]);
        PlanetType planetType = PlanetType.valueOf(split[1]);
        int unitsInside = Integer.valueOf(split[2]);
        double rx = Double.valueOf(split[3]);
        double ry = Double.valueOf(split[4]);

        PlanetsManager planetsManager = objectsLayer.planetsManager;
        tempCircle.setRadius(planetsManager.getPlanetRadius());
        tempCircle.center.set(rx * GraphicsYio.width, ry * GraphicsYio.width);

        Planet planet = planetsManager.spawnPlanet(tempCircle, fractionType);
        planet.setType(planetType);
        planet.setUnitsInside(unitsInside);
        planet.applyMaxShields();
    }


    private void applyGameRules() {
        String gameRulesSection = getSection("game_rules");
        if (gameRulesSection == null) return;

        String[] split = gameRulesSection.split(" ");

        GameRules.difficulty = Difficulty.valueOf(split[0]);
        if (split.length > 1) {
            GameRules.aiOnlyMode = Boolean.valueOf(split[1]);
        }
    }


    private void finish() {

    }


    public Difficulty getDifficultyFromLevelCode(String levelCode) {
        source = levelCode;
        String gameRulesSection = getSection("game_rules");
        if (gameRulesSection == null) return Difficulty.hard;

        String[] split = gameRulesSection.split(" ");

        return Difficulty.valueOf(split[0]);
    }


    public LevelSize getLevelSizeFromLevelCode(String levelCode) {
        source = levelCode;
        String generalSection = getSection("general");
        if (generalSection == null) return null;

        String[] split = generalSection.split(" ");
        return LevelSize.valueOf(split[0]);
    }


    private String getSection(String name) {
        int nameIndex = source.indexOf(name);
        if (nameIndex == -1) return null;
        int colonIndex = source.indexOf(":", nameIndex);
        int hashIndex = source.indexOf("#", colonIndex);
        if (hashIndex - colonIndex < 2) return null;

        return source.substring(colonIndex + 1, hashIndex);
    }
}
