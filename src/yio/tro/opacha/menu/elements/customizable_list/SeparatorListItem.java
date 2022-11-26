package yio.tro.opacha.menu.elements.customizable_list;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.LineYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.RenderableTextYio;

public class SeparatorListItem extends AbstractCustomListItem{

    public LineYio left, right;
    float offset = 0.1f * GraphicsYio.width;
    public RenderableTextYio title;


    @Override
    protected void initialize() {
        left = new LineYio();
        right = new LineYio();
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
    }


    @Override
    protected void move() {
        updateLeftLine();
        updateRightLine();
        moveRenderableTextByDefault(title);
    }


    public void setTitle(String string) {
        title.setString(string);
        title.updateMetrics();
    }


    private void updateRightLine() {
        right.start.x = viewPosition.x + viewPosition.width / 2 + offset;
        right.start.y = viewPosition.y + viewPosition.height / 2;
        right.finish.x = viewPosition.x + viewPosition.width - offset;
        right.finish.y = viewPosition.y + viewPosition.height / 2;
    }


    private void updateLeftLine() {
        left.start.x = viewPosition.x + offset;
        left.start.y = viewPosition.y + viewPosition.height / 2;
        left.finish.x = viewPosition.x + viewPosition.width / 2 - offset;
        left.finish.y = viewPosition.y + viewPosition.height / 2;
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return 0.05f * GraphicsYio.height;
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
        return MenuRenders.renderSeparatorListItem;
    }
}
