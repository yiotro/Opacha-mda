package yio.tro.opacha.stuff.containers.posmap;

public class PmSectorIndex {

    public int x, y;


    public PmSectorIndex() {
        set(0, 0);
    }


    public void set(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
    }


    void setBy(PmSectorIndex src) {
        x = src.x;
        y = src.y;
    }


    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
