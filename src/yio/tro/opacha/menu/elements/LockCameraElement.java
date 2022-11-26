package yio.tro.opacha.menu.elements;

import yio.tro.opacha.SoundManager;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.menu.ClickDetector;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.stuff.CircleYio;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.SelectionEngineYio;

public class LockCameraElement extends InterfaceElement<LockCameraElement> {

    public boolean locked;
    public CircleYio iconPosition;
    public SelectionEngineYio selectionEngineYio;
    boolean currentlyTouched;
    public CircleYio selectionPosition;


    public LockCameraElement(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);
        locked = false;
        iconPosition = new CircleYio();
        selectionEngineYio = new SelectionEngineYio();
        selectionPosition = new CircleYio();
    }


    @Override
    protected LockCameraElement getThis() {
        return this;
    }


    @Override
    public void move() {
        updateViewPosition();
    }


    @Override
    protected void onApplyParent() {
        super.onApplyParent();
        updateIconPosition();
        moveSelection();
        updateSelectionPosition();
    }


    private void updateSelectionPosition() {
        selectionPosition.setBy(iconPosition);
        selectionPosition.radius *= 2.5f;
    }


    private void moveSelection() {
        if (currentlyTouched) return;
        selectionEngineYio.move();
    }


    private void updateIconPosition() {
        iconPosition.center.set(
                viewPosition.x + viewPosition.width / 2,
                viewPosition.y + viewPosition.height / 2
        );
        iconPosition.radius = viewPosition.width / 2;
    }


    @Override
    public void onDestroy() {

    }


    public boolean isTouchedBy(PointYio touchPoint) {
        return touchPoint.distanceTo(iconPosition.center) < iconPosition.radius + 0.05f * GraphicsYio.width;
    }


    @Override
    public void onAppear() {
        currentlyTouched = false;
        loadValues();
    }


    private void loadValues() {
        locked = getGameController().cameraController.isLocked();
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        if (isTouchedBy(currentTouch)) {
            selectionEngineYio.select();
            currentlyTouched = true;
            return true;
        }
        return false;
    }


    @Override
    public boolean touchDrag() {
        if (!currentlyTouched) return false;
        return true;
    }


    @Override
    public boolean touchUp() {
        if (!currentlyTouched) return false;
        currentlyTouched = false;
        if (isClicked()) {
            onClick();
        }
        return true;
    }


    private void onClick() {
        SoundManager.playSound(SoundManager.button);
        setLocked(!locked);
    }


    public void setLocked(boolean locked) {
        if (this.locked == locked) return;
        this.locked = locked;
        applyValues();
    }


    private void applyValues() {
        getGameController().cameraController.setLocked(locked);
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderLockCameraElement;
    }
}
