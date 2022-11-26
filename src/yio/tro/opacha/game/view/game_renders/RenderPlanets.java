package yio.tro.opacha.game.view.game_renders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.game.gameplay.model.*;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.Storage3xTexture;

import java.util.HashMap;

public class RenderPlanets extends GameRender{

    HashMap<FractionType, Storage3xTexture> mapMainTextures;
    private PlanetsManager planetsManager;
    private Storage3xTexture shadowTexture;
    HashMap<PlanetType, Storage3xTexture> mapTypeTextures;
    private Storage3xTexture selectionTexture;
    HashMap<FractionType, Storage3xTexture> mapShieldSmallTextures;
    HashMap<FractionType, Storage3xTexture> mapShieldMoreTextures;
    private Storage3xTexture autoTargetTexture;
    private Storage3xTexture highlightTexture;
    private Storage3xTexture halvedSelectionTexture;


    @Override
    protected void loadTextures() {
        mapMainTextures = new HashMap<>();
        for (FractionType fractionType : FractionType.values()) {
            mapMainTextures.put(fractionType, load3xTexture("planet_" + fractionType));
        }

        mapTypeTextures = new HashMap<>();
        for (PlanetType planetType : PlanetType.values()) {
            mapTypeTextures.put(planetType, load3xTexture("type_" + planetType));
        }

        mapShieldSmallTextures = new HashMap<>();
        for (FractionType fractionType : FractionType.values()) {
            mapShieldSmallTextures.put(fractionType, load3xTexture("shield_" + fractionType + "_small"));
        }

        mapShieldMoreTextures = new HashMap<>();
        for (FractionType fractionType : FractionType.values()) {
            mapShieldMoreTextures.put(fractionType, load3xTexture("shield_" + fractionType + "_more"));
        }

        shadowTexture = load3xTexture("planet_shadow");
        selectionTexture = load3xTexture("selection");
        autoTargetTexture = load3xTexture("auto_target");
        highlightTexture = load3xTexture("highlight");
        halvedSelectionTexture = load3xTexture("halved_selection");
    }


    @Override
    public void render() {
        planetsManager = getObjectsLayer().planetsManager;

        for (Planet planet : planetsManager.planets) {
            if (!planet.isCurrentlyVisible()) continue;
            renderSinglePlanet(planet);
        }

        for (Planet planet : planetsManager.planets) {
            if (!planet.isCurrentlyVisible()) continue;
            renderText(planet);
        }
    }


    public void renderShadows() {
        planetsManager = getObjectsLayer().planetsManager;

        for (Planet planet : planetsManager.planets) {
            if (!planet.isCurrentlyVisible()) continue;
            renderShadow(planet);
        }
    }


    private void renderShadow(Planet planet) {
        GraphicsYio.drawByCircle(
                batchMovable,
                shadowTexture.getTexture(getCurrentZoomQuality()),
                planet.viewPosition
        );
    }


    private void renderSinglePlanet(Planet planet) {
        renderHighlight(planet);
        renderSelection(planet);
        renderShields(planet);
        renderAutoTarget(planet);
        renderMainPart(planet);
        renderViewType(planet);
    }


    private void renderHighlight(Planet planet) {
        if (!planet.highlightEngine.isSelected()) return;

        GraphicsYio.setBatchAlpha(batchMovable, planet.highlightEngine.getAlpha() / 2);
        GraphicsYio.drawByCircle(batchMovable, highlightTexture.getTexture(getCurrentZoomQuality()), planet.highlightViewPosition);
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    private void renderAutoTarget(Planet planet) {
        if (planet.atFactor.get() == 0) return;

        GraphicsYio.drawByCircle(
                batchMovable,
                autoTargetTexture.getTexture(getCurrentZoomQuality()),
                planet.atViewPosition
        );
    }


    private void renderText(Planet planet) {
        GraphicsYio.renderText(batchMovable, planet.text);
    }


    private void renderShields(Planet planet) {
        for (Shield shield : planet.shields) {
            if (!shield.isActive()) continue;
            GraphicsYio.drawByCircle(
                    batchMovable,
                    getShieldTexture(planet),
                    shield.viewPosition
            );
        }
    }


    private TextureRegion getShieldTexture(Planet planet) {
        if (planet.isDefensive()) {
            return mapShieldMoreTextures.get(planet.fraction).getTexture(getCurrentZoomQuality());
        }

        return mapShieldSmallTextures.get(planet.fraction).getTexture(getCurrentZoomQuality());
    }


    private void renderMainPart(Planet planet) {
        GraphicsYio.drawByCircle(
                batchMovable,
                mapMainTextures.get(planet.fraction).getTexture(getCurrentZoomQuality()),
                planet.viewPosition
        );
    }


    private void renderViewType(Planet planet) {
        if (planet.viewTypeFactor.get() < 1) {
            GraphicsYio.setBatchAlpha(batchMovable, planet.viewTypeFactor.get());
        }

        GraphicsYio.drawByCircle(
                batchMovable,
                mapTypeTextures.get(planet.type).getTexture(getCurrentZoomQuality()),
                planet.viewPosition
        );

        if (planet.viewTypeFactor.get() < 1) {
            GraphicsYio.setBatchAlpha(batchMovable, 1);
        }
    }


    private void renderSelection(Planet planet) {
        if (!planet.selectionEngineYio.isSelected()) return;

        GraphicsYio.drawByCircle(
                batchMovable,
                getSelectionTexture(planet),
                planet.selectionPosition
        );
    }


    private TextureRegion getSelectionTexture(Planet planet) {
        if (planet.halvedSelectionMode) {
            return halvedSelectionTexture.getTexture(getCurrentZoomQuality());
        }

        return selectionTexture.getTexture(getCurrentZoomQuality());
    }


    @Override
    protected void disposeTextures() {

    }
}
