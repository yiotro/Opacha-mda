package yio.tro.opacha.game.editor;

import yio.tro.opacha.stuff.object_pool.ReusableYio;

public class EsmItem implements ReusableYio{

    public String index;
    public String name;
    public String levelCode;


    @Override
    public void reset() {
        index = "";
        name = "";
        levelCode = "";
    }

}
