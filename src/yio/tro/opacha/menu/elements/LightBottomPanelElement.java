package yio.tro.opacha.menu.elements;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RectangleYio;
import yio.tro.opacha.stuff.RenderableTextYio;

public class LightBottomPanelElement extends InterfaceElement<LightBottomPanelElement> {

    public RenderableTextYio title;
    public RectangleYio renderPosition;


    public LightBottomPanelElement(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);
        title = new RenderableTextYio();
        title.setFont(Fonts.buttonFont);
        renderPosition = new RectangleYio();
    }


    @Override
    protected LightBottomPanelElement getThis() {
        return this;
    }


    public LightBottomPanelElement setTitle(String key) {
        title.setString(LanguagesManager.getInstance().getString(key));
        title.updateMetrics();
        return this;
    }


    @Override
    public void move() {
        updateViewPosition();
    }


    @Override
    protected void onApplyParent() {
        super.onApplyParent();
        moveRenderPosition();
        moveTitle();
    }


    private void moveRenderPosition() {
        renderPosition.setBy(viewPosition);
        renderPosition.increase(GraphicsYio.borderThickness);
        renderPosition.height -= GraphicsYio.borderThickness;
    }


    private void moveTitle() {
        title.centerHorizontal(viewPosition);
        title.position.y = viewPosition.y + viewPosition.height - 0.03f * GraphicsYio.height;
        title.updateBounds();
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
        return MenuRenders.renderLightBottomPanel;
    }
}
