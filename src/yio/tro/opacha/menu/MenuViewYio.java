package yio.tro.opacha.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import yio.tro.opacha.*;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.stuff.GraphicsYio;

public class MenuViewYio {

    public YioGdxGame yioGdxGame;
    MenuControllerYio menuControllerYio;
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public OrthographicCamera orthoCam;
    public int w, h;


    public MenuViewYio(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        menuControllerYio = yioGdxGame.menuControllerYio;
        shapeRenderer = new ShapeRenderer();
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        createOrthoCam();
        update();
    }


    private void createOrthoCam() {
        orthoCam = new OrthographicCamera(yioGdxGame.w, yioGdxGame.h);
        orthoCam.position.set(orthoCam.viewportWidth / 2f, orthoCam.viewportHeight / 2f, 0);
        orthoCam.update();
    }


    public void update() {
        batch = yioGdxGame.batch;
        MenuRenders.updateRenderSystems(this);
    }


    public void renderAll(boolean onTopOfGameView) {
        batch.begin();

        renderLayers(onTopOfGameView, true);
        renderLayers(onTopOfGameView, false);

        renderDebug();

        GraphicsYio.setBatchAlpha(batch, 1);
        batch.end();
    }


    private void renderLayers(boolean onTopOfGameView, boolean dyingStatus) {
        // first layer
        for (InterfaceElement element : menuControllerYio.visibleElements) {
            if (!element.isVisible()) continue;
            if (!element.compareGvStatus(onTopOfGameView)) continue;
            if (element.getDyingStatus() != dyingStatus) continue;
            if (element.isViewPositionNotUpdatedYet()) continue;
            element.getRenderSystem().renderFirstLayer(element);
        }

        // second layer
        for (InterfaceElement element : menuControllerYio.visibleElements) {
            if (!element.isVisible()) continue;
            if (!element.compareGvStatus(onTopOfGameView)) continue;
            if (element.getDyingStatus() != dyingStatus) continue;
            if (element.isViewPositionNotUpdatedYet()) continue;
            element.getRenderSystem().renderSecondLayer(element);
        }

        // third layer
        for (InterfaceElement element : menuControllerYio.visibleElements) {
            if (!element.isVisible()) continue;
            if (!element.compareGvStatus(onTopOfGameView)) continue;
            if (element.getDyingStatus() != dyingStatus) continue;
            if (element.isViewPositionNotUpdatedYet()) continue;
            element.getRenderSystem().renderThirdLayer(element);
        }
    }


    private void renderDebug() {

    }
}
