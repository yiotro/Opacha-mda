package yio.tro.opacha.game.gameplay.model;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.SoundManager;
import yio.tro.opacha.Yio;
import yio.tro.opacha.game.*;
import yio.tro.opacha.game.export_import.SavableYio;
import yio.tro.opacha.game.gameplay.AcceleratableYio;
import yio.tro.opacha.game.gameplay.GameObject;
import yio.tro.opacha.game.gameplay.ai.AiData;
import yio.tro.opacha.game.gameplay.particles.ParticlesManager;
import yio.tro.opacha.stuff.*;
import yio.tro.opacha.stuff.factor_yio.FactorYio;
import yio.tro.opacha.stuff.object_pool.ObjectPoolYio;
import yio.tro.opacha.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class Planet extends GameObject implements ReusableYio, AcceleratableYio, SavableYio {

    public static final int MAX_UNITS_CAPACITY = 99999;
    PlanetsManager planetsManager;
    public CircleYio position;
    public CircleYio viewPosition;
    public FractionType fraction;
    public ArrayList<Link> adjoinedLinks;
    public boolean flag;
    public PlanetType type;
    public SelectionEngineYio selectionEngineYio;
    public CircleYio selectionPosition;
    boolean holdSelection;
    public FactorYio viewTypeFactor;
    JumpEngineYio jumpEngineYio;
    public ArrayList<Shield> shields;
    ObjectPoolYio<Shield> poolShields;
    public float currentDefense;
    public int maxDefense;
    public RenderableTextYio text;
    public float unitsInside;
    RepeatYio<Planet> repeatBuildUnits, repeatIncreaseDefense;
    public Planet autoTarget;
    public CircleYio atViewPosition;
    public FactorYio atFactor;
    double atAngle;
    RepeatYio<Planet> repeatAutoTarget;
    public SelectionEngineYio highlightEngine;
    public boolean holdHighlight;
    public CircleYio highlightViewPosition;
    public boolean halvedSelectionMode;
    public AiData aiData;
    public Planet algoPointer;
    public FractionType algoFraction;
    private float atAccumulator; // auto target


    public Planet(PlanetsManager planetsManager) {
        this.planetsManager = planetsManager;
        position = new CircleYio();
        viewPosition = new CircleYio();
        fraction = null;
        adjoinedLinks = new ArrayList<>();
        selectionEngineYio = new SelectionEngineYio();
        selectionPosition = new CircleYio();
        viewTypeFactor = new FactorYio();
        jumpEngineYio = new JumpEngineYio();
        shields = new ArrayList<>();
        text = new RenderableTextYio();
        text.setFont(Fonts.gameFont);
        autoTarget = null;
        atFactor = new FactorYio();
        atViewPosition = new CircleYio();
        atAngle = 0;
        highlightEngine = new SelectionEngineYio();
        holdHighlight = false;
        highlightViewPosition = new CircleYio();
        halvedSelectionMode = false;
        aiData = new AiData();
        algoPointer = null;
        algoFraction = null;
        atAccumulator = 0;

        setUnitsInside(3);
        initPools();
        initRepeats();
        reset();
    }


    private void initRepeats() {
        repeatBuildUnits = new RepeatYio<Planet>(this, 6, 2) {
            @Override
            public void performAction() {
                parent.buildUnit();
            }
        };
        repeatIncreaseDefense = new RepeatYio<Planet>(this, 6, 2) {
            @Override
            public void performAction() {
                parent.checkToIncreaseCurrentDefense();
            }
        };
        repeatAutoTarget = new RepeatYio<Planet>(this, 9) {
            @Override
            public void performAction() {
                parent.checkToApplyAutoTarget();
            }
        };
    }


    private void checkToApplyAutoTarget() {
        if (autoTarget == null) return;

        if (atAccumulator < 1) {
            atAccumulator += 0.1f * VelocityManager.getInstance().value;
            return;
        }

        atAccumulator -= 1;
        planetsManager.sendUnits(this, autoTarget);
    }


    private void checkToIncreaseCurrentDefense() {
        if (currentDefense >= maxDefense) return;
        if (isEconomic()) return;
        float delta = 0.1f * VelocityManager.getInstance().value;
        if (isDefensive()) {
            delta *= 3;
        }
        setCurrentDefense(currentDefense + delta);
    }


    private void initPools() {
        poolShields = new ObjectPoolYio<Shield>(shields) {
            @Override
            public Shield makeNewObject() {
                return new Shield(Planet.this);
            }
        };
    }


    @Override
    public void reset() {
        position.reset();
        viewPosition.reset();
        fraction = null;
        adjoinedLinks.clear();
        flag = false;
        type = PlanetType.empty;
        holdSelection = false;
        jumpEngineYio.reset();
        viewTypeFactor.reset();
        poolShields.clearExternalList();
    }


    public void onAdjoinedLinkAdded(Link link) {
        adjoinedLinks.add(link);
    }


    public void onAdjoinedLinkRemoved(Link link) {
        adjoinedLinks.remove(link);
    }


    public boolean isCurrentlyVisible() {
        GameController gameController = planetsManager.objectsLayer.gameController;
        return gameController.cameraController.isCircleInViewFrame(viewPosition);
    }


    public boolean isTouchedBy(PointYio touchPoint, double zoom) {
        return position.center.distanceTo(touchPoint) < (1.3 * position.radius) / zoom;
    }


    private void buildUnit() {
        if (fraction == FractionType.neutral) return;
        if (planetsManager.objectsLayer.gameController.gameMode == GameMode.editor) return;
        double delta = 0.1 * VelocityManager.getInstance().value;
        if (isEconomic()) {
            delta *= 2;
        }
        increaseUnitsInside(delta);
    }


    @Override
    public void moveActually() {
        repeatBuildUnits.move();
        repeatIncreaseDefense.move();
        repeatAutoTarget.move();
    }


    @Override
    public void moveVisually() {
        applyVisualRotation();
        moveSelection();
        updateSelectionPosition();
        viewTypeFactor.move();
        updateViewRadius();
        updateShields();
        moveText();
        moveAutoTargetVisually();
        moveHighlight();
    }


    private void moveHighlight() {
        if (holdHighlight) return;
        highlightEngine.move();
        highlightViewPosition.setBy(viewPosition);
        highlightViewPosition.radius = 1.5f * position.radius;
    }


    private void moveAutoTargetVisually() {
        if (autoTarget == null && atFactor.get() == 0) return;

        atFactor.move();
        updateAtViewPosition();
    }


    private void updateAtViewPosition() {
        atViewPosition.center.setBy(viewPosition.center);
        atViewPosition.setRadius(1.2 * atFactor.get() * viewPosition.radius);
        atViewPosition.angle += 0.2 * (atAngle - atViewPosition.angle);
    }


    private void moveText() {
        text.position.x = viewPosition.center.x - text.width / 2;
        text.position.y = viewPosition.center.y + text.height / 2;
        text.updateBounds();
    }


    private void updateShields() {
        for (Shield shield : shields) {
            shield.move();
        }
    }


    private void updateViewRadius() {
        if (!jumpEngineYio.hasToMove()) return;
        jumpEngineYio.move();
        viewPosition.radius = (1 + 0.2f * jumpEngineYio.getValue()) * position.radius;
    }


    public void applyPopEffect() {
        jumpEngineYio.reset();
        jumpEngineYio.apply(1, 0.5);
    }


    private void moveSelection() {
        if (holdSelection) return;
        if (!selectionEngineYio.isSelected()) return;

        selectionEngineYio.move();

        if (!selectionEngineYio.isSelected()) {
            halvedSelectionMode = false;
        }
    }


    private void updateSelectionPosition() {
        selectionPosition.setBy(viewPosition);
        selectionPosition.radius = (float) ((0.5 + 0.8 * selectionEngineYio.factorYio.get()) * position.radius);
        selectionPosition.angle = -0.5 * viewPosition.angle;
    }


    private void applyVisualRotation() {
        SpeedManager speedManager = planetsManager.objectsLayer.gameController.speedManager;
        float velocity = VelocityManager.getInstance().value;
        viewPosition.angle -= 0.01 * velocity * speedManager.getSpeed();
    }


    private SpeedManager getSpeedManager() {
        return planetsManager.objectsLayer.gameController.speedManager;
    }


    public void setPosition(CircleYio pos) {
        setPosition(pos.center.x, pos.center.y, pos.radius);
    }


    public void setPosition(double x, double y, double r) {
        position.set(x, y, r);
        onPositionChanged();
    }


    public void onPositionChanged() {
        forceViewPosition();
        updateShields();
    }


    private void forceViewPosition() {
        viewPosition.setBy(position);
    }


    public boolean isDefensive() {
        return type == PlanetType.defensive;
    }


    public boolean isEconomic() {
        return type == PlanetType.economic;
    }


    public void setFraction(FractionType fraction) {
        if (this.fraction == fraction) return;

        FractionType previousFraction = this.fraction;
        this.fraction = fraction;

        onFractionChanged(previousFraction);
    }


    private void onFractionChanged(FractionType previousFraction) {
        if (previousFraction == null) return;

        setType(PlanetType.empty);
        setAutoTarget(null);
        setCurrentDefense(0);
        deselect();
        SoundManager.playSound(SoundManager.fall);
        planetsManager.lastCapturedPlanet = this;
        aiData.reset();
    }


    public void setType(PlanetType type) {
        if (this.type == type) return;

        PlanetType previousType = this.type;
        this.type = type;

        onTypeChanged(previousType, type);
    }


    private void onTypeChanged(PlanetType previousType, PlanetType type) {
        viewTypeFactor.reset();
        viewTypeFactor.appear(3, 0.2);

        if (previousType == PlanetType.defensive || type == PlanetType.defensive) {
            updateMaxDefense();
            createShields();
            syncShieldsWithCurrentDefense();
        }

        if (type == PlanetType.economic) {
            setCurrentDefense(0);
        }
    }


    public Link getLink(Planet planet) {
        for (Link adjoinedLink : adjoinedLinks) {
            if (!adjoinedLink.contains(planet)) continue;
            return adjoinedLink;
        }
        return null;
    }


    public void onUnitReached(Unit unit) {
        if (unit.fraction == fraction) {
            onSameFractionsReached(unit);
        } else {
            onDifferentFractionReached(unit);
        }
    }


    private void onSameFractionsReached(Unit unit) {
        increaseUnitsInside(unit.value);
        applyPopEffect();
    }


    public int getFullHp() {
        return (int) unitsInside + (int) currentDefense;
    }


    public boolean isNeutral() {
        return fraction == FractionType.neutral;
    }


    private void onDifferentFractionReached(Unit unit) {
        if (unit.value <= (int) currentDefense) {
            decreaseCurrentDefense(unit.value);
            spawnExplosionCausedByUnit(unit);
            return;
        }

        if (unit.value < getFullHp()) {
            int damage = unit.value - (int) currentDefense;
            setCurrentDefense(0);
            setUnitsInside(unitsInside - damage);
            spawnExplosionCausedByUnit(unit);
            return;
        }

        int v = unit.value - getFullHp();
        setCurrentDefense(0);
        setFraction(unit.fraction);
        setType(PlanetType.empty);
        setUnitsInside(v);
        applyPopEffect();
        spawnExplosionCausedByUnit(unit);
        planetsManager.objectsLayer.linksManager.syncFractions();
    }


    private void spawnExplosionCausedByUnit(Unit unit) {
        getParticlesManager().spawnExplosion(viewPosition.center, 2, getParticlesManager().convertFractionIntoPaViewType(unit.fraction));
    }


    private ParticlesManager getParticlesManager() {
        return planetsManager.objectsLayer.particlesManager;
    }


    public boolean isLinkedTo(Planet planet) {
        return getLink(planet) != null;
    }


    public void decreaseCurrentDefense(int value) {
        setCurrentDefense(currentDefense - value);
    }


    public void setCurrentDefense(float currentDefense) {
        this.currentDefense = currentDefense;
        syncShieldsWithCurrentDefense();
    }


    private void updateMaxDefense() {
        switch (type) {
            default:
                maxDefense = 7;
                break;
            case defensive:
                maxDefense = 14;
                break;
        }
    }


    public void onBuilt() {
        updateMaxDefense();
        setCurrentDefense(maxDefense);
        createShields();
    }


    public void syncShieldsWithCurrentDefense() {
        for (int i = 0; i < shields.size(); i++) {
            Shield shield = shields.get(i);
            if (i <= currentDefense - 1) {
                shield.setActive(true);
            } else {
                shield.setActive(false);
            }
        }
    }


    private void createShields() {
        poolShields.clearExternalList();
        int n = maxDefense;
        double a = Math.PI / 2;
        double da = (2 * Math.PI) / n;
        for (int i = 0; i < n; i++) {
            Shield freshObject = poolShields.getFreshObject();
            freshObject.setAngle(a);
            a -= da;
            freshObject.setActive(true);
        }
    }


    public void onEndCreation() {
        forceViewPosition();
        moveText();
        updateShields();
    }


    public void forceDeselection() {
        selectionEngineYio.factorYio.setValue(0.1);
    }


    public void increaseUnitsInside(double delta) {
        setUnitsInside(unitsInside + delta);
    }


    public void decreaseUnitsInside(int value) {
        setUnitsInside(unitsInside - value);
    }


    public void setUnitsInside(double delta) {
        unitsInside = (float) delta;

        if (unitsInside > MAX_UNITS_CAPACITY) {
            unitsInside = MAX_UNITS_CAPACITY;
        }
        if (unitsInside < 0) {
            unitsInside = 0;
        }

        syncTextWithUnitsInside();
    }


    public boolean isControlledByPlayer() {
        return !GameRules.aiOnlyMode && fraction == FractionType.green;
    }


    public void syncTextWithUnitsInside() {
        text.setString(convertUnitsQuantityToString());
        text.updateMetrics();
    }


    private String convertUnitsQuantityToString() {
        int value = (int) unitsInside;

        if (value < 1000) {
            return "" + value;
        }

        if (value < 10000) {
            float v = value;
            v /= 1000;
            v = (float) Yio.roundUp(v, 1);
            return v + "k";
        }

        int iv = value;
        iv /= 1000;
        return iv + "k";
    }


    public void select() {
        selectionEngineYio.select();
        holdSelection = true;
        setHalvedSelectionMode(false);
    }


    public void setAutoTarget(Planet autoTarget) {
        if (this.autoTarget == autoTarget) return;

        Planet previousAutoTarget = this.autoTarget;
        this.autoTarget = autoTarget;

        onAutoTargetChanged(previousAutoTarget);
    }


    public void prepareToSendAutoTargetQuickly() {
        repeatAutoTarget.setCountDown(1);
        atAccumulator = 1.1f;
    }


    public boolean hasAutoTarget() {
        return autoTarget != null;
    }


    private void onAutoTargetChanged(Planet previousAutoTarget) {
        if (autoTarget == null) {
            atFactor.destroy(1, 1);
            return;
        }

        atFactor.appear(3, 1);
        atAngle = position.center.angleTo(autoTarget.position.center);
        if (previousAutoTarget == null) {
            atViewPosition.angle = atAngle;
        }
        prepareAtViewPositionAngle();
    }


    private void prepareAtViewPositionAngle() {
        while (atViewPosition.angle > atAngle + Math.PI) {
            atViewPosition.angle -= 2 * Math.PI;
        }
        while (atViewPosition.angle < atAngle - Math.PI) {
            atViewPosition.angle += 2 * Math.PI;
        }
    }


    public void deselect() {
        holdSelection = false;
    }


    public void setHoldHighlight(boolean holdHighlight) {
        this.holdHighlight = holdHighlight;
    }


    public void setHalvedSelectionMode(boolean halvedSelectionMode) {
        this.halvedSelectionMode = halvedSelectionMode;
    }


    public int getNumberOfAdjoinedEnemies() {
        int c = 0;
        for (Link adjoinedLink : adjoinedLinks) {
            Planet oppositePlanet = adjoinedLink.getOppositePlanet(this);
            if (oppositePlanet.isNeutral()) continue;
            if (oppositePlanet.fraction == fraction) continue;
            c++;
        }
        return c;
    }


    public int countAdjoinedPlanetsByFraction(FractionType filterType) {
        int c = 0;
        for (Link adjoinedLink : adjoinedLinks) {
            Planet oppositePlanet = adjoinedLink.getOppositePlanet(this);
            if (oppositePlanet.fraction != filterType) continue;
            c++;
        }
        return c;
    }


    public void applyMaxShields() {
        if (isEconomic()) return;
        setCurrentDefense(maxDefense);
    }


    public void setAlgoPointer(Planet algoPointer) {
        this.algoPointer = algoPointer;
    }


    @Override
    public String saveToString() {
        double rx = Yio.roundUp(position.center.x / GraphicsYio.width, 3);
        double ry = Yio.roundUp(position.center.y / GraphicsYio.width, 3);
        return fraction + " " + type + " " + (int) unitsInside + " " + rx + " " + ry;
    }


    @Override
    public String toString() {
        return "[Planet: " +
                getUniqueCode() + " " +
                fraction + " " + Yio.roundUp(unitsInside, 2) + " " +
                "]";
    }
}
