package yio.tro.opacha.stuff;

import yio.tro.opacha.stuff.object_pool.ReusableYio;

public class LineYio implements ReusableYio{

    public PointYio start;
    public PointYio finish;
    public float thickness;
    public float length;
    public float angle;
    public float renderAngle;


    public LineYio() {
        start = new PointYio();
        finish = new PointYio();
        reset();
    }


    @Override
    public void reset() {
        start.reset();
        finish.reset();
        thickness = GraphicsYio.borderThickness;
        length = 0;
        angle = 0;
        renderAngle = 0;
    }


    public LineYio setThickness(float thickness) {
        this.thickness = thickness;
        return this;
    }


    public LineYio setStart(double x, double y) {
        start.set(x, y);
        updateMetrics();
        return this;
    }


    public LineYio setStart(PointYio pointYio) {
        return setStart(pointYio.x, pointYio.y);
    }


    public LineYio setFinish(double x, double y) {
        finish.set(x, y);
        updateMetrics();
        return this;
    }


    public LineYio setFinish(PointYio pointYio) {
        return setFinish(pointYio.x, pointYio.y);
    }


    public void updateMetrics() {
        length = start.distanceTo(finish);
        angle = (float) start.angleTo(finish);
        renderAngle = (float) (180 / Math.PI * angle);
    }
}
