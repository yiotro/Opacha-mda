package yio.tro.opacha.stuff.containers.posmap;

import yio.tro.opacha.stuff.PointYio;

public abstract class PosMapObjectYio {

    protected PointYio posMapPosition;
    PmSectorIndex lastIndexPoint, indexPoint;


    public PosMapObjectYio() {
        lastIndexPoint = new PmSectorIndex();
        indexPoint = new PmSectorIndex();
        posMapPosition = new PointYio();
    }


    protected abstract void updatePosMapPosition();
}
