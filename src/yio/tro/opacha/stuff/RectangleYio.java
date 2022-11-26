package yio.tro.opacha.stuff;

import yio.tro.opacha.Yio;
import yio.tro.opacha.stuff.object_pool.ReusableYio;

public class RectangleYio implements ReusableYio{

    public float x;
    public float y;
    public float width;
    public float height;


    public RectangleYio() {
        reset();
    }


    public RectangleYio(double x, double y, double width, double height) {
        set(x, y, width, height);
    }


    public RectangleYio(RectangleYio src) {
        setBy(src);
    }


    public void set(double x, double y, double width, double height) {
        this.x = (float) x;
        this.y = (float) y;
        this.width = (float) width;
        this.height = (float) height;
    }


    public void setBy(RectangleYio src) {
        set(src.x, src.y, src.width, src.height);
    }


    public void increase(double delta) {
        x -= delta;
        y -= delta;
        width += 2 * delta;
        height += 2 * delta;
    }


    public double distanceTo(PointYio pointYio) {
        if (isPointInside(pointYio)) return 0;

        if (pointYio.x < x) {
            if (pointYio.y < y) {
                return Yio.distance(pointYio.x, pointYio.y, x, y);
            } else if (pointYio.y < y + height) {
                return x - pointYio.x;
            } else {
                return Yio.distance(pointYio.x, pointYio.y, x, y + height);
            }
        } if (pointYio.x < x + width) {
            if (pointYio.y < y) {
                return y - pointYio.y;
            } else if (pointYio.y < y + height) {
                return 0;
            } else {
                return pointYio.y - (y + height);
            }
        } else {
            if (pointYio.y < y) {
                return Yio.distance(pointYio.x, pointYio.y, x + width, y);
            } else if (pointYio.y < y + height) {
                return pointYio.x - (x + width);
            } else {
                return Yio.distance(pointYio.x, pointYio.y, x + width, y + height);
            }
        }
    }


    public double angleFrom(PointYio pointYio) {
        if (pointYio.x < x) {
            if (pointYio.y < y) {
                return Yio.angle(pointYio.x, pointYio.y, x, y);
            } else if (pointYio.y < y + height) {
                return 0;
            } else {
                return Yio.angle(pointYio.x, pointYio.y, x, y + height);
            }
        } if (pointYio.x < x + width) {
            if (pointYio.y < y) {
                return Math.PI / 2;
            } else if (pointYio.y < y + height) {
                return 0;
            } else {
                return 1.5 * Math.PI;
            }
        } else {
            if (pointYio.y < y) {
                return Yio.angle(pointYio.x, pointYio.y, x + width, y);
            } else if (pointYio.y < y + height) {
                return Math.PI;
            } else {
                return Yio.angle(pointYio.x, pointYio.y, x + width, y + height);
            }
        }
    }


    public boolean isPointInside(PointYio pointYio) {
        if (pointYio.x < x) return false;
        if (pointYio.y < y) return false;
        if (pointYio.x > x + width) return false;
        if (pointYio.y > y + height) return false;
        return true;
    }


    @Override
    public void reset() {
        set(0, 0, 0, 0);
    }


    @Override
    public String toString() {
        return "(" + Yio.roundUp(x, 2) + ", " + Yio.roundUp(y, 2) + ", " + Yio.roundUp(width, 2) + ", " + Yio.roundUp(height, 2) + ")";
    }
}
