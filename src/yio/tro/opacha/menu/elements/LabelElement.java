package yio.tro.opacha.menu.elements;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.opacha.Fonts;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.stuff.RenderableTextYio;

public class LabelElement extends InterfaceElement<LabelElement> {


    public RenderableTextYio title;


    public LabelElement(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);
        title = new RenderableTextYio();
        title.setFont(Fonts.buttonFont);
    }


    public LabelElement setString(String string) {
        return setTitle(string);
    }


    public LabelElement setTitle(String string) {
        title.setString(string);
        title.updateMetrics();
        return this;
    }


    public LabelElement setFont(BitmapFont font) {
        title.setFont(font);
        title.updateMetrics();
        return this;
    }


    @Override
    protected LabelElement getThis() {
        return this;
    }


    @Override
    public void move() {
        updateViewPosition();
    }


    @Override
    protected void onApplyParent() {
        super.onApplyParent();
        title.centerHorizontal(viewPosition);
        title.centerVertical(viewPosition);
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
        return MenuRenders.renderLabelElement;
    }

}
