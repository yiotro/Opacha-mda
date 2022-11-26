package yio.tro.opacha.menu.elements.customizable_list;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.opacha.Fonts;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.opacha.stuff.GraphicsYio;

public class ScrollListItem extends AbstractSingleLineItem{

    SliReaction clickReaction;
    SliReaction longTapReaction;
    public String key;
    private float height;
    public boolean centered;
    public boolean highlighted;


    public ScrollListItem() {
        clickReaction = null;
        longTapReaction = null;
        key = null;
        height = 0.1f * GraphicsYio.height;
        centered = false;
        highlighted = false;
    }


    @Override
    protected BitmapFont getTitleFont() {
        return Fonts.smallFont;
    }


    public void setFont(BitmapFont font) {
        title.setFont(font);
        title.updateMetrics();
    }


    @Override
    protected double getHeight() {
        return height;
    }


    @Override
    protected void onClicked() {
        if (clickReaction != null) {
            clickReaction.apply(this);
        }
    }


    @Override
    protected void onLongTapped() {
        super.onLongTapped();
        if (longTapReaction != null) {
            longTapReaction.apply(this);
        }
    }


    public void setClickReaction(SliReaction clickReaction) {
        this.clickReaction = clickReaction;
    }


    public void setLongTapReaction(SliReaction longTapReaction) {
        this.longTapReaction = longTapReaction;
    }


    public void setKey(String key) {
        this.key = key;
    }


    public void setHeight(float height) {
        this.height = height;
    }


    public void setCentered(boolean centered) {
        this.centered = centered;
    }


    @Override
    protected void onPositionChanged() {
        super.onPositionChanged();
        if (centered) {
            title.delta.x = viewPosition.width / 2 - title.width / 2;
        }
    }


    public String getKey() {
        return key;
    }


    public boolean isHighlighted() {
        return highlighted;
    }


    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderScrollListItem;
    }
}
