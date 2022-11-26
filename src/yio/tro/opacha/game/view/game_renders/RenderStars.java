package yio.tro.opacha.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.game.gameplay.stars.Star;
import yio.tro.opacha.stuff.GraphicsYio;

public class RenderStars extends GameRender{


    private TextureRegion starTexture;


    @Override
    protected void loadTextures() {
        starTexture = GraphicsYio.loadTextureRegion("game/background/particle.png", true);
    }


    @Override
    public void render() {
        GraphicsYio.setBatchAlpha(batchMovable, 0.1);
        for (Star star : getObjectsLayer().starsManager.stars) {
            if (!star.isCurrentlyVisible()) continue;
            renderSingleStar(star);
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    private void renderSingleStar(Star star) {
        GraphicsYio.drawByCircle(
                batchMovable,
                starTexture,
                star.viewPosition
        );
    }


    @Override
    protected void disposeTextures() {
        starTexture.getTexture().dispose();
    }
}
