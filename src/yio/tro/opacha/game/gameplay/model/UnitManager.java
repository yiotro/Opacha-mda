package yio.tro.opacha.game.gameplay.model;

import yio.tro.opacha.game.gameplay.IGameplayManager;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.stuff.RepeatYio;
import yio.tro.opacha.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class UnitManager implements IGameplayManager{

    ObjectsLayer objectsLayer;
    public ArrayList<Unit> units;
    ObjectPoolYio<Unit> poolUnits;
    RepeatYio<UnitManager> repeatRemoveUnits;


    public UnitManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        units = new ArrayList<>();
        initPools();
        initRepeats();
    }


    private void initRepeats() {
        repeatRemoveUnits = new RepeatYio<UnitManager>(this, 60) {
            @Override
            public void performAction() {
                parent.checkToRemoveUnits();
            }
        };
    }


    private void initPools() {
        poolUnits = new ObjectPoolYio<Unit>(units) {
            @Override
            public Unit makeNewObject() {
                return new Unit(UnitManager.this);
            }
        };
    }


    @Override
    public void defaultValues() {
        poolUnits.clearExternalList();
    }


    public void launchUnit(Planet start, Planet target, int value) {
        Unit freshObject = poolUnits.getFreshObject();
        freshObject.setValue(value);
        freshObject.setFraction(start.fraction);
        freshObject.launch(start.getLink(target), start);
    }


    private void checkToRemoveUnits() {
        for (int i = units.size() - 1; i >= 0; i--) {
            Unit unit = units.get(i);
            if (!unit.isReadyToBeRemoved()) continue;
            removeUnit(unit);
        }
    }


    public void removeUnit(Unit unit) {
        poolUnits.removeFromExternalList(unit);
    }


    public FractionType getAliveFraction(FractionType ignoredFraction) {
        for (Unit unit : units) {
            if (ignoredFraction != null && unit.fraction == ignoredFraction) continue;
            return unit.fraction;
        }

        return null;
    }


    public boolean areThereAtLeastTwoAliveFractions() {
        FractionType firstFraction = getAliveFraction(null);
        if (firstFraction == null) return false;

        FractionType secondFraction = getAliveFraction(firstFraction);
        if (secondFraction == null) return false;

        return true;
    }


    @Override
    public void onEndCreation() {

    }


    @Override
    public void moveActually() {
        moveUnitsActually();
        repeatRemoveUnits.move();
    }


    private void moveUnitsActually() {
        for (Unit unit : units) {
            unit.moveActually();
        }
    }


    @Override
    public void moveVisually() {
        moveUnitsVisually();
    }


    private void moveUnitsVisually() {
        for (Unit unit : units) {
            unit.moveVisually();
        }
    }
}
