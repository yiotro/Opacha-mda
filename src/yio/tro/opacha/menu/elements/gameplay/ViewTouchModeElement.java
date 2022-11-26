package yio.tro.opacha.menu.elements.gameplay;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.game.touch_modes.TouchMode;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RectangleYio;
import yio.tro.opacha.stuff.RenderableTextYio;
import yio.tro.opacha.stuff.factor_yio.FactorYio;

public class ViewTouchModeElement extends InterfaceElement<ViewTouchModeElement> {

    public RenderableTextYio title;
    public boolean hasText;
    FactorYio textAlphaFactor;
    public RectangleYio backgroundPosition;


    public ViewTouchModeElement(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);

        title = new RenderableTextYio();
        title.setFont(Fonts.smallFont);
        hasText = false;
        textAlphaFactor = new FactorYio();
        backgroundPosition = new RectangleYio();
    }


    @Override
    protected ViewTouchModeElement getThis() {
        return this;
    }


    @Override
    public void move() {
        updateViewPosition();
    }


    @Override
    protected void onApplyParent() {
        updateTextPosition();
        moveTextAlpha();
        title.updateBounds();
        updateBackgroundPosition();
    }


    private void updateBackgroundPosition() {
        backgroundPosition.setBy(title.bounds);
        backgroundPosition.increase(0.02f * GraphicsYio.width);
    }


    private void moveTextAlpha() {
        if (!textAlphaFactor.move()) return;

        hasText = (textAlphaFactor.get() > 0 || textAlphaFactor.isInAppearState());
    }


    private void updateTextPosition() {
        if (!hasText) return;

        title.centerHorizontal(viewPosition);
        title.centerVertical(viewPosition);
    }


    public void onTouchModeSet(TouchMode touchMode) {
        if (touchMode == null) {
            textAlphaFactor.destroy(1, 1);
            return;
        }

        String nameKey = touchMode.getNameKey();

        if (nameKey == null) {
            textAlphaFactor.destroy(1, 1);
            return;
        }

        hasText = true;
        title.setString(LanguagesManager.getInstance().getString(nameKey));
        title.updateMetrics();
        updateTextPosition();

        textAlphaFactor.appear(3, 0.5);
    }


    public double getTextAlpha() {
        return getAlpha() * textAlphaFactor.get();
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {

    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        return false;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        return false;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderViewTouchMode;
    }
}
