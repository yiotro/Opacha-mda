package yio.tro.opacha.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.LockCameraElement;
import yio.tro.opacha.stuff.GraphicsYio;

public class RenderLockCameraElement extends RenderInterfaceElement{


    private TextureRegion lockedTexture;
    private LockCameraElement lockCameraElement;
    private TextureRegion selectionTexture;
    private TextureRegion unlockedTexture;


    @Override
    public void loadTextures() {
        lockedTexture = GraphicsYio.loadTextureRegion("menu/gameplay/lock_camera.png", true);
        unlockedTexture = GraphicsYio.loadTextureRegion("menu/gameplay/unlock_camera.png", true);
        selectionTexture = GraphicsYio.loadTextureRegion("menu/selection.png", true);
    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        lockCameraElement = (LockCameraElement) element;
        if (getGameView().appearFactor.get() < 0.1) return;
        GraphicsYio.drawByCircle(batch, getCurrentTexture(), lockCameraElement.iconPosition);
        renderSelection();
    }


    private TextureRegion getCurrentTexture() {
        if (lockCameraElement.locked) {
            return lockedTexture;
        }
        return unlockedTexture;
    }


    private void renderSelection() {
        if (!lockCameraElement.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, lockCameraElement.selectionEngineYio.getAlpha());
        GraphicsYio.drawByCircle(batch, selectionTexture, lockCameraElement.selectionPosition);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
