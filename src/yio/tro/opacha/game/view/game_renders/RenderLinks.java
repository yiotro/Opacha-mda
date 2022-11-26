package yio.tro.opacha.game.view.game_renders;

import yio.tro.opacha.Yio;
import yio.tro.opacha.game.gameplay.model.FractionType;
import yio.tro.opacha.game.gameplay.model.Link;
import yio.tro.opacha.game.gameplay.model.LinksManager;
import yio.tro.opacha.stuff.CircleYio;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.Storage3xTexture;

import java.util.HashMap;

public class RenderLinks extends GameRender{

    HashMap<FractionType, Storage3xTexture> mapTextures;
    private LinksManager linksManager;
    private Storage3xTexture shadowTexture;


    @Override
    protected void loadTextures() {
        mapTextures = new HashMap<>();
        for (FractionType fractionType : FractionType.values()) {
            mapTextures.put(fractionType, load3xTexture("link_" + fractionType));
        }
        shadowTexture = load3xTexture("link_shadow");
    }


    @Override
    public void render() {
        linksManager = getObjectsLayer().linksManager;

        for (Link link : linksManager.links) {
            if (!link.isCurrentlyVisible()) continue;
            renderSingleLink(link);
        }
    }


    public void renderShadows() {
        linksManager = getObjectsLayer().linksManager;

        for (Link link : linksManager.links) {
            if (!link.isCurrentlyVisible()) continue;
            renderSingleShadow(link);
        }
    }


    private void renderSingleShadow(Link link) {
        if (link.isNeutral()) return;

        batchMovable.draw(
                shadowTexture.getTexture(getCurrentZoomQuality()),
                link.one.viewPosition.center.x,
                (float) (link.one.viewPosition.center.y - 4.3f * link.thickness * 0.5),
                0f,
                4.3f * link.thickness * 0.5f,
                (float) link.distance,
                4.3f * link.thickness,
                1f,
                1f,
                (float) (180 / Math.PI * link.angle)
        );
    }


    private void renderNeutralLink(Link link) {
        for (CircleYio nPoint : link.nPoints) {
            GraphicsYio.drawByCircle(
                    batchMovable,
                    mapTextures.get(FractionType.neutral).getTexture(getCurrentZoomQuality()),
                    nPoint
            );
        }
    }


    private void renderSingleLink(Link link) {
        if (link.isNeutral()) {
            renderNeutralLink(link);
            return;
        }

        batchMovable.draw(
                mapTextures.get(link.fractionType).getTexture(getCurrentZoomQuality()),
                link.one.viewPosition.center.x,
                (float) (link.one.viewPosition.center.y - link.thickness * 0.5),
                0f,
                link.thickness * 0.5f,
                (float) link.distance,
                link.thickness,
                1f,
                1f,
                (float) (180 / Math.PI * link.angle)
        );
    }


    @Override
    protected void disposeTextures() {

    }
}
