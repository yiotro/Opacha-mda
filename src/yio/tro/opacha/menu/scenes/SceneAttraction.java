package yio.tro.opacha.menu.scenes;

import com.badlogic.gdx.Gdx;
import yio.tro.opacha.Fonts;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.stuff.StoreLinksYio;

public class SceneAttraction extends SceneYio{

    private ButtonYio label;


    @Override
    protected void initialize() {
        spawnBackButton(getBackReaction());

        // opacha-mda can be easily rejected in GP if google decides that
        // game promoted here has something to do with 'gambling', for example
        // So I have to be extremely careful with what I'm promoting here

        label = uiFactory.getButton()
                .setSize(0.85, 0.4)
                .centerHorizontal()
                .alignBottom(0.25)
                .setFont(Fonts.miniFont)
                .applyText(convertStringToArray(languagesManager.getString("kladom_release")))
                .setTouchable(false);

        uiFactory.getButton()
                .setParent(label)
                .setSize(0.4, 0.05)
                .setTouchOffset(0.05)
                .centerHorizontal()
                .alignBottom(0.01)
                .applyText("load")
                .setReaction(getOkReaction());
    }


    private Reaction getOkReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Gdx.net.openURI(StoreLinksYio.getInstance().getLink("kladom"));
            }
        };
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.mainMenu.create();
            }
        };
    }
}
