package yio.tro.opacha.menu.elements.customizable_list;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RenderableTextYio;

public abstract class AbstractSingleLineItem extends AbstractCustomListItem{

    public RenderableTextYio title;
    public boolean darken;
    public float darkValue;


    @Override
    protected void initialize() {
        title = new RenderableTextYio();
        title.setFont(getTitleFont());
        darken = false;
        darkValue = 0.04f;
    }


    protected abstract BitmapFont getTitleFont();


    @Override
    protected void move() {
        moveRenderableTextByDefault(title);
    }


    public void setTitle(String string) {
        title.setString(string);
        title.updateMetrics();
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected void onPositionChanged() {
        title.delta.x = 0.04f * GraphicsYio.width;
        title.delta.y = (float) (getHeight() / 2 + title.height / 2);
    }


    @Override
    protected void onLongTapped() {

    }


    public void setDarken(boolean darken) {
        this.darken = darken;
    }


    public void setDarkValue(float darkValue) {
        this.darkValue = darkValue;
    }
}
