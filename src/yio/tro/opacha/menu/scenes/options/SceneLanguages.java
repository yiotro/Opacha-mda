package yio.tro.opacha.menu.scenes.options;

import yio.tro.opacha.menu.CustomLanguageLoader;
import yio.tro.opacha.menu.LanguageChooseItem;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.opacha.menu.elements.customizable_list.ScrollListItem;
import yio.tro.opacha.menu.elements.customizable_list.SliReaction;
import yio.tro.opacha.menu.elements.ground.GpSrNone;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.elements.scrollable_list.ScrBehavior;
import yio.tro.opacha.menu.elements.scrollable_list.ScrItem;
import yio.tro.opacha.menu.elements.scrollable_list.ScrollableListElement;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.SceneYio;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.GraphicsYio;

import java.util.ArrayList;

public class SceneLanguages extends SceneYio {


    private CustomizableListYio customizableListYio;


    @Override
    public void initialize() {
        setBackground(GroundIndex.GREEN);
        getGround().setSpawnRule(new GpSrNone());

        spawnBackButton(getBackReaction());
        createList();
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.85, 0.85)
                .centerHorizontal()
                .alignBottom(0.025);
        loadValues();
    }


    private void loadValues() {
        customizableListYio.clearItems();

        SliReaction clickReaction = new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                applyLanguage(item.key);
            }
        };

        ArrayList<LanguageChooseItem> chooseListItems = LanguagesManager.getInstance().getChooseListItems();
        for (LanguageChooseItem chooseListItem : chooseListItems) {
            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setTitle(chooseListItem.title);
            scrollListItem.setKey(chooseListItem.name);
            scrollListItem.setClickReaction(clickReaction);
            scrollListItem.setHeight(0.08f * GraphicsYio.height);
            scrollListItem.setDarken(customizableListYio.items.size() % 2 == 0);
            customizableListYio.addItem(scrollListItem);
        }
    }


    private void applyLanguage(String key) {
        CustomLanguageLoader.setAndSaveLanguage(key);
        menuControllerYio.clear();
        MenuControllerYio.createInitialScene();
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.settingsMenu.create();
            }
        };
    }

}
