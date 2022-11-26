package yio.tro.opacha.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.game.LevelSize;
import yio.tro.opacha.game.gameplay.model.FractionType;
import yio.tro.opacha.game.gameplay.model.Unit;
import yio.tro.opacha.game.gameplay.model.UnitManager;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.Storage3xTexture;

import java.util.HashMap;

public class RenderUnits extends GameRender{

    HashMap<FractionType, Storage3xTexture> mapTextures;
    private UnitManager unitManager;


    @Override
    protected void loadTextures() {
        mapTextures = new HashMap<>();
        for (FractionType fractionType : FractionType.values()) {
            if (fractionType == FractionType.neutral) continue;
            mapTextures.put(fractionType, load3xTexture("unit_" + fractionType));
        }
    }


    @Override
    public void render() {
        unitManager = getObjectsLayer().unitManager;

        for (Unit unit : unitManager.units) {
            if (!unit.isCurrentlyVisible()) continue;
            if (!unit.isAlive()) continue;
            renderSingleUnit(unit);
        }
    }


    private void renderSingleUnit(Unit unit) {
        GraphicsYio.drawByCircle(
                batchMovable,
                getUnitTexture(unit),
                unit.viewPosition
        );

        if (gameController.initialLevelSize != LevelSize.normal) {
            GraphicsYio.renderText(batchMovable, unit.title);
        }
    }


    private TextureRegion getUnitTexture(Unit unit) {
        Storage3xTexture storage3xTexture = mapTextures.get(unit.fraction);
        if (gameController.initialLevelSize == LevelSize.normal) {
            return storage3xTexture.getLowest();
        }

        return storage3xTexture.getTexture(getCurrentZoomQuality());
    }


    @Override
    protected void disposeTextures() {

    }
}
