package yio.tro.opacha.menu.two_finger_touch;

import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.object_pool.ReusableYio;

public class TfPoint implements ReusableYio {

    public PointYio position;
    public int id;


    public TfPoint() {
        position = new PointYio();
    }


    @Override
    public void reset() {
        position.reset();
        id = -1;
    }
}
