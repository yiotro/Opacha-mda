package yio.tro.opacha.game.gameplay.model;

import yio.tro.opacha.Yio;
import yio.tro.opacha.game.export_import.SavableYio;
import yio.tro.opacha.game.gameplay.AcceleratableYio;
import yio.tro.opacha.game.gameplay.particles.PaViewType;
import yio.tro.opacha.game.gameplay.particles.ParticlesManager;
import yio.tro.opacha.stuff.CircleYio;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.RepeatYio;
import yio.tro.opacha.stuff.object_pool.ObjectPoolYio;
import yio.tro.opacha.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class Link implements ReusableYio, AcceleratableYio, SavableYio{

    LinksManager linksManager;
    public Planet one;
    public Planet two;
    public FractionType fractionType;
    public float thickness;
    public double angle;
    public double distance;
    float defaultLength;
    public float movementMultiplier;
    public ArrayList<CircleYio> nPoints;
    ObjectPoolYio<CircleYio> poolCircles;
    PointYio tempPoint;
    public ArrayList<Unit> upWalkers;
    public ArrayList<Unit> downWalkers;
    RepeatYio<Link> repeatCheckForBattles;


    public Link(LinksManager linksManager) {
        this.linksManager = linksManager;
        thickness = 0.008f * GraphicsYio.width;
        defaultLength = 0.2f * GraphicsYio.width;
        nPoints = new ArrayList<>();
        tempPoint = new PointYio();
        upWalkers = new ArrayList<>();
        downWalkers = new ArrayList<>();
        reset();
        initRepeats();
        initPools();
    }


    private void initPools() {
        poolCircles = new ObjectPoolYio<CircleYio>(nPoints) {
            @Override
            public CircleYio makeNewObject() {
                return new CircleYio();
            }
        };
    }


    private void initRepeats() {
        repeatCheckForBattles = new RepeatYio<Link>(this, 3) {
            @Override
            public void performAction() {
                parent.checkForBattles();
            }
        };
    }


    @Override
    public void reset() {
        one = null;
        two = null;
        fractionType = null;
    }


    public boolean equals(Planet one, Planet two) {
        return contains(one) && contains(two);
    }


    public boolean contains(Planet planet) {
        return one == planet || two == planet;
    }


    public Planet getOppositePlanet(Planet planet) {
        if (planet == one) return two;
        if (planet == two) return one;
        return null;
    }


    public boolean isCurrentlyVisible() {
        return one.isCurrentlyVisible() || two.isCurrentlyVisible();
    }


    @Override
    public void moveActually() {
        repeatCheckForBattles.move();
    }


    @Override
    public void moveVisually() {

    }


    public void setOne(Planet one) {
        this.one = one;
    }


    public void setTwo(Planet two) {
        this.two = two;
    }


    public void updateMetrics() {
        angle = one.position.center.angleTo(two.position.center);
        distance = one.position.center.distanceTo(two.position.center);
        movementMultiplier = 0.009f * (float) (defaultLength / distance);
    }


    public void initNPoints() {
        poolCircles.clearExternalList();
        tempPoint.setBy(one.position.center);
        double r = one.position.radius;
        double step = 0.7 * r;
        int n = (int) (distance / step) + 1;
        for (int i = 0; i < n; i++) {
            CircleYio circleYio = poolCircles.getFreshObject();
            circleYio.center.setBy(tempPoint);
            circleYio.setRadius(0.3 * r);
            circleYio.setAngle(angle);
            tempPoint.relocateRadial(step, angle);
        }
    }


    public void checkForBattles() {
        if (upWalkers.size() == 0) return;
        if (downWalkers.size() == 0) return;

        Unit firstUpWalker = upWalkers.get(0);
        for (Unit downWalker : downWalkers) {
            if (checkForSingleBattle(firstUpWalker, downWalker)) break;
        }
        if (downWalkers.size() == 0) return;

        Unit firstDownWalker = downWalkers.get(0);
        for (Unit upWalker : upWalkers) {
            if (checkForSingleBattle(upWalker, firstDownWalker)) break;
        }
    }


    private boolean checkForSingleBattle(Unit upWalker, Unit downWalker) {
        if (upWalker.fraction == downWalker.fraction) return false;
        if (upWalker.acceleratableFactor.value < downWalker.acceleratableFactor.value) return false;

        applyBattle(upWalker, downWalker);
        return true;
    }


    public ArrayList<Unit> getWalkersToTarget(Planet target) {
        if (target == one) {
            return downWalkers;
        }
        if (target == two) {
            return upWalkers;
        }
        return null;
    }


    private void applyBattle(Unit one, Unit two) {
        if (one.value > two.value) {
            one.setValue(one.value - two.value);
            killUnit(two);
        } else if (one.value < two.value) {
            two.setValue(two.value - one.value);
            killUnit(one);
        } else {
            killUnit(one);
            killUnit(two);
        }
    }


    private void killUnit(Unit unit) {
        unit.setAlive(false);

        ParticlesManager particlesManager = linksManager.objectsLayer.particlesManager;
        PaViewType paViewType = particlesManager.convertFractionIntoPaViewType(unit.fraction);
        particlesManager.spawnExplosion(unit.viewPosition.center, 1, paViewType);
    }


    public void onUnitAdded(Unit unit) {
        getProperWalkersList(unit).add(unit);
        checkForBattles();
    }


    public void onUnitDied(Unit unit) {
        getProperWalkersList(unit).remove(unit);
    }


    private ArrayList<Unit> getProperWalkersList(Unit unit) {
        if (unit.acceleratableFactor.isInAppearState()) {
            return upWalkers;
        }

        return downWalkers;
    }


    @Override
    public String saveToString() {
        double rx1 = Yio.roundUp(one.position.center.x / GraphicsYio.width, 3);
        double ry1 = Yio.roundUp(one.position.center.y / GraphicsYio.width, 3);
        double rx2 = Yio.roundUp(two.position.center.x / GraphicsYio.width, 3);
        double ry2 = Yio.roundUp(two.position.center.y / GraphicsYio.width, 3);
        return fractionType + " " + rx1 + " " + ry1 + " " + rx2 + " " + ry2;
    }


    public void onEndCreation() {
        initNPoints();
    }


    public boolean isNeutral() {
        return fractionType == FractionType.neutral;
    }


    public void setFractionType(FractionType fractionType) {
        this.fractionType = fractionType;
    }
}
