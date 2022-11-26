package yio.tro.opacha.menu.elements.customizable_list;

import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.campaign.CampaignManager;
import yio.tro.opacha.game.debug.DebugFlags;
import yio.tro.opacha.game.loading.LoadingManager;
import yio.tro.opacha.game.loading.LoadingParameters;
import yio.tro.opacha.game.loading.LoadingType;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;

import java.util.ArrayList;

public class CampaignCustomItem extends AbstractCustomListItem{

    public static final int ROW = 6;
    public ArrayList<CciInnerItem> items;
    public float iconSize = 0.11f * GraphicsYio.width;
    public float delta = 0.04f * GraphicsYio.width;
    private CciInnerItem touchedItem;


    @Override
    protected void initialize() {
        items = new ArrayList<>();
    }


    public void set(int startIndex, int endIndex) {
        for (int index = startIndex; index <= endIndex; index++) {
            CciInnerItem innerItem = new CciInnerItem(this);
            innerItem.setIndex(index);
            innerItem.setType(CampaignManager.getInstance().getLevelType(index));
            items.add(innerItem);
        }
    }


    @Override
    protected void move() {
        for (CciInnerItem item : items) {
            item.move();
            if (!customizableListYio.touched) {
                item.selectionEngineYio.move();
            }
        }
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return iconSize + delta;
    }


    @Override
    protected void onPositionChanged() {
        updateItemMetrics();
    }


    private void updateItemMetrics() {
        float lineWidth = ROW * iconSize + (ROW - 1) * delta;
        float x = (float) (getWidth() / 2 - lineWidth / 2);
        float y = (float) (getHeight() / 2 - iconSize / 2);
        for (CciInnerItem item : items) {
            item.delta.set(x, y);
            item.position.width = iconSize;
            item.position.height = iconSize;
            x += iconSize + delta;
        }
    }


    @Override
    public void onTouchDown(PointYio touchPoint) {
        touchedItem = findTouchedItem(touchPoint);
        if (touchedItem == null) return;

        touchedItem.selectionEngineYio.select();
    }


    public boolean containsLevelIndex(int index) {
        for (CciInnerItem item : items) {
            if (item.index == index) return true;
        }
        return false;
    }


    private CciInnerItem findTouchedItem(PointYio touchPoint) {
        for (CciInnerItem item : items) {
            if (!item.selectionPosition.isPointInside(touchPoint)) continue;
            return item;
        }
        return null;
    }


    @Override
    protected void onClicked() {
        if (touchedItem == null) return;

        int index = touchedItem.index;
        CciType type = touchedItem.type;
        if (type == CciType.unknown && !DebugFlags.unlockLevels) return;
        YioGdxGame yioGdxGame = getGameController().yioGdxGame;

        if (index == 0) {
            yioGdxGame.loadingManager.startLoading(LoadingType.tutorial, new LoadingParameters());
            return;
        }

        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.addParameter("index", index);
        yioGdxGame.loadingManager.startInstantly(LoadingType.campaign_create, loadingParameters);
    }


    @Override
    protected void onLongTapped() {

    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderCampaignCustomItem;
    }
}
