package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.game.campaign.CampaignManager;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.elements.ScrollHelperElement;
import yio.tro.opacha.menu.elements.customizable_list.*;
import yio.tro.opacha.menu.elements.ground.GpSrNone;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.stuff.TimeMeasureYio;

public class SceneCampaign extends SceneYio{


    public CustomizableListYio customizableListYio;
    private ScrollHelperElement scrollHelperElement;


    @Override
    protected void initialize() {
        setBackground(GroundIndex.YELLOW);
        getGround().setSpawnRule(new GpSrNone());

        createCampaignView();
        createScrollHelper();
        createBackButton();
    }


    private void createScrollHelper() {
        scrollHelperElement = uiFactory.getScrollHelperElement()
                .setSize(1, 1)
                .setInverted(true)
                .setScrollEngineYio(customizableListYio.getScrollEngineYio());
    }


    private void createCampaignView() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(1, 1)
                .alignLeft()
                .alignBottom();

        loadValues();
    }


    private void loadValues() {
        customizableListYio.clearItems();
        SampleListItem sampleListItem = new SampleListItem();
        sampleListItem.setTitle(LanguagesManager.getInstance().getString("campaign"));
        customizableListYio.addItem(sampleListItem);

        int n = 1 + CampaignManager.LAST_LEVEL_INDEX / CampaignCustomItem.ROW;
        for (int i = 0; i < n; i++) {
            CampaignCustomItem campaignCustomItem = new CampaignCustomItem();
            campaignCustomItem.set(i * CampaignCustomItem.ROW, Math.min((i + 1) * CampaignCustomItem.ROW - 1, CampaignManager.LAST_LEVEL_INDEX));
            customizableListYio.addItem(campaignCustomItem);

            if (i > 2 && (i + 1) % 5 == 0) {
                SeparatorListItem separatorListItem = new SeparatorListItem();
                separatorListItem.setTitle("" + (i + 1) * CampaignCustomItem.ROW);
                customizableListYio.addItem(separatorListItem);
            }
        }
    }


    @Override
    protected void onEndCreation() {
        super.onEndCreation();
        scrollToHighestUnlockedLevel();
    }


    private void scrollToHighestUnlockedLevel() {
        customizableListYio.resetScroll();
        int index = CampaignManager.getInstance().getIndexOfHighestUnlockedLevel();
        CampaignCustomItem itemWithIndex = findItemWithIndex(index);
        if (itemWithIndex == null) return;

        customizableListYio.scrollToItem(itemWithIndex);
    }


    private CampaignCustomItem findItemWithIndex(int index) {
        for (AbstractCustomListItem item : customizableListYio.items) {
            if (!(item instanceof CampaignCustomItem)) continue;
            if (!((CampaignCustomItem) item).containsLevelIndex(index)) continue;
            return (CampaignCustomItem) item;
        }
        return null;
    }


    public void onLevelMarkedAsCompleted() {
        if (customizableListYio == null) return;

        loadValues();
    }


    private void createBackButton() {
        spawnBackButton(new Reaction() {
            @Override
            protected void reaction() {
                Scenes.chooseGameMode.create();
            }
        });
    }
}
