package yio.tro.opacha.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.game.gameplay.fill.FillManager;
import yio.tro.opacha.game.gameplay.model.FractionType;
import yio.tro.opacha.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderFillers extends GameRender{

    HashMap<FractionType, TextureRegion> mapTextures;


    @Override
    protected void loadTextures() {
        mapTextures = new HashMap<>();
        for (FractionType fractionType : FractionType.values()) {
            if (fractionType == FractionType.neutral) continue;
            TextureRegion textureRegion = GraphicsYio.loadTextureRegion("game/fillers/filler_" + fractionType + ".png", true);
            mapTextures.put(fractionType, textureRegion);
        }
    }


    @Override
    public void render() {
        FillManager fillManager = gameController.objectsLayer.fillManager;
        if (!fillManager.isActive()) return;

        GraphicsYio.drawByCircle(batchMovable, mapTextures.get(fillManager.matchResults.winner), fillManager.viewPosition);
    }


    @Override
    protected void disposeTextures() {

    }
}
