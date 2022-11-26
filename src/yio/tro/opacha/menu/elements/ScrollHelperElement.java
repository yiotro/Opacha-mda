package yio.tro.opacha.menu.elements;

import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RectangleYio;
import yio.tro.opacha.stuff.SelectionEngineYio;
import yio.tro.opacha.stuff.factor_yio.FactorYio;
import yio.tro.opacha.stuff.scroll_engine.ScrollEngineYio;

public class ScrollHelperElement extends InterfaceElement<ScrollHelperElement> {

    public ScrollEngineYio scrollEngineYio;
    public RectangleYio roadPosition;
    public RectangleYio wagonPosition;
    boolean inverted;
    boolean currentlyTouched;
    float touchOffset;
    public SelectionEngineYio selectionEngineYio;
    public RectangleYio touchArea;
    boolean applianceMode;
    double targetValue;
    public FactorYio fadeInFactor;


    public ScrollHelperElement(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);
        roadPosition = new RectangleYio();
        wagonPosition = new RectangleYio();
        inverted = false;
        touchOffset = 0.07f * GraphicsYio.width;
        selectionEngineYio = new SelectionEngineYio();
        touchArea = new RectangleYio();
        fadeInFactor = new FactorYio();
    }


    @Override
    protected ScrollHelperElement getThis() {
        return this;
    }


    @Override
    public void move() {
        updateViewPosition();
    }


    @Override
    protected void onApplyParent() {
        super.onApplyParent();
        updateRoadPosition();
        updateWagonPosition();
        moveApplianceMode();
        updateTouchArea();
        moveSelection();
        moveFadeInFactor();
    }


    private void moveFadeInFactor() {
        fadeInFactor.move();

        if (!fadeInFactor.isInAppearState() && Math.abs(scrollEngineYio.getSpeed()) > 0.01) {
            doFadeIn();
        }

        if (isReadyToFadeOut()) {
            fadeInFactor.destroy(1, 0.5);
        }
    }


    private void doFadeIn() {
        fadeInFactor.appear(3, 2);
    }


    private boolean isReadyToFadeOut() {
        if (fadeInFactor.isInDestroyState()) return false;
        if (Math.abs(scrollEngineYio.getSpeed()) >= 0.01) return false;
        if (applianceMode) return false;
        if (currentlyTouched) return false;
        return true;
    }


    private void moveApplianceMode() {
        if (!applianceMode) return;

        if (scrollEngineYio.applyNormalizedPosition(targetValue)) {
            updateWagonPosition();
        } else {
            applianceMode = false;
        }
    }


    private void updateTouchArea() {
        touchArea.setBy(wagonPosition);
        touchArea.increase(touchOffset);
    }


    private void moveSelection() {
        if (currentlyTouched) return;
        selectionEngineYio.move();
    }


    private void updateWagonPosition() {
        float aPos = (float) (scrollEngineYio.getSlider().a / scrollEngineYio.getLimits().getLength());
        float bPos = (float) (scrollEngineYio.getSlider().b / scrollEngineYio.getLimits().getLength());

        if (inverted) {
            float temp = 1 - aPos;
            aPos = 1 - bPos;
            bPos = temp;
        }

        wagonPosition.set(
                roadPosition.x,
                roadPosition.y + aPos * roadPosition.height,
                roadPosition.width,
                (bPos - aPos) * roadPosition.height
        );
    }


    private void updateRoadPosition() {
        roadPosition.setBy(viewPosition);
        roadPosition.width = 8 * GraphicsYio.borderThickness;
        roadPosition.x = GraphicsYio.width - roadPosition.width;
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        currentlyTouched = false;
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        currentlyTouched = touchArea.isPointInside(currentTouch) && fadeInFactor.get() > 0;
        if (currentlyTouched) {
            selectionEngineYio.select();
            if (!fadeInFactor.isInAppearState()) {
                doFadeIn();
            }
            return true;
        }
        return false;
    }


    @Override
    public boolean touchDrag() {
        if (!currentlyTouched) return false;
        applyCurrentTouchToScrollEngine();
        return true;
    }


    private void applyCurrentTouchToScrollEngine() {
        targetValue = (currentTouch.y - roadPosition.y) / roadPosition.height;
        if (inverted) {
            targetValue = 1 - targetValue;
        }
        applianceMode = true;
        scrollEngineYio.setSpeed(0);
    }


    @Override
    public boolean touchUp() {
        if (!currentlyTouched) return false;
        currentlyTouched = false;

        return false;
    }


    public ScrollHelperElement setScrollEngineYio(ScrollEngineYio scrollEngineYio) {
        this.scrollEngineYio = scrollEngineYio;
        return this;
    }


    public ScrollHelperElement setInverted(boolean inverted) {
        this.inverted = inverted;
        return this;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderScrollHelperElement;
    }
}
