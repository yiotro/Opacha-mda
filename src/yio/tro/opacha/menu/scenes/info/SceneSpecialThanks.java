package yio.tro.opacha.menu.scenes.info;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.menu.LanguageChooseItem;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.scenes.Scenes;

import java.util.ArrayList;

public class SceneSpecialThanks extends AbstractInfoScene {


    private ButtonYio infoPanel;


    @Override
    public void initialize() {
        setBackground(GroundIndex.MAGENTA);

        spawnBackButton(new Reaction() {
            @Override
            protected void reaction() {
                Scenes.settingsMenu.create();
            }
        });

        String translatorsString = getTranslatorsString();
        String topic = languagesManager.getString("special_thanks_begin") +
//                languagesManager.getString("special_thanks_people_eng") +
                translatorsString + "";

        infoPanel = uiFactory.getButton()
                .setPosition(0.05, 0.1, 0.9, 0.7)
                .setReaction(Reaction.rbNothing)
                .setFont(Fonts.miniFont)
                .clearText()
                .applyFixedAmountOfLines(topic, 16);
    }


    private String getTranslatorsString() {
        ArrayList<LanguageChooseItem> chooseListItems = LanguagesManager.getInstance().getChooseListItems();

        StringBuilder builder = new StringBuilder();

        for (LanguageChooseItem chooseListItem : chooseListItems) {
            if (chooseListItem.author.equals("yiotro")) continue;

            builder.append("#").append(chooseListItem.name).append(": ").append(chooseListItem.author);
        }

        return builder.toString();
    }
}
