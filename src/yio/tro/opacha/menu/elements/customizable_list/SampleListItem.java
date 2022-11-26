package yio.tro.opacha.menu.elements.customizable_list;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RenderableTextYio;

public class SampleListItem extends AbstractCustomListItem{

    public RenderableTextYio title;


    @Override
    protected void initialize() {
        title = new RenderableTextYio();
        title.setFont(Fonts.buttonFont);
    }


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
    protected double getHeight() {
        return 0.12f * GraphicsYio.height;
    }


    @Override
    protected void onPositionChanged() {
        title.delta.x = (float) (getWidth() / 2 - title.width / 2);
        title.delta.y = (float) (getHeight() / 2 + title.height / 2);
    }


    @Override
    protected void onClicked() {

    }


    @Override
    protected void onLongTapped() {

    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderSampleListItem;
    }
}
