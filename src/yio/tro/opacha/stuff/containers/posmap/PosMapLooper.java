package yio.tro.opacha.stuff.containers.posmap;

import java.util.ArrayList;

public abstract class PosMapLooper<ProviderType> {

    protected ProviderType parent;
    int verOffset, horOffset;


    public PosMapLooper(ProviderType parent, int offset) {
        this(parent, offset, offset);
    }


    public PosMapLooper(ProviderType parent, int horOffset, int verOffset) {
        this.parent = parent;
        this.horOffset = horOffset;
        this.verOffset = verOffset;
    }


    public abstract void performAction(ArrayList<PosMapObjectYio> posMapObjects);


    public void forNearbySectors(PosMapYio posMapYio, PmSectorIndex sectorIndex) {
        for (int i = sectorIndex.x - horOffset; i <= sectorIndex.x + horOffset; i++) {
            for (int j = sectorIndex.y - verOffset; j <= sectorIndex.y + verOffset; j++) {
                ArrayList<PosMapObjectYio> sector = posMapYio.getSector(i, j);
                if (sector == null) continue;
                performAction(sector);
            }
        }
    }
}
