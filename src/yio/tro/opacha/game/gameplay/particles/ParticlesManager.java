package yio.tro.opacha.game.gameplay.particles;

import yio.tro.opacha.RefreshRateDetector;
import yio.tro.opacha.SettingsManager;
import yio.tro.opacha.Yio;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.gameplay.AcceleratableYio;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.game.gameplay.model.FractionType;
import yio.tro.opacha.game.gameplay.model.Link;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.RepeatYio;
import yio.tro.opacha.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class ParticlesManager implements AcceleratableYio{

    public static final int MAX_QUANTITY = 1000;
    ObjectsLayer objectsLayer;
    public ArrayList<Particle> particles;
    ObjectPoolYio<Particle> poolParticles;
    float friction;
    RepeatYio<ParticlesManager> repeatRemoveParticles;
    int currentId, maxId;
    PointYio tempPoint;


    public ParticlesManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;

        particles = new ArrayList<>();
        friction = 0.05f;
        currentId = 0;
        maxId = 2;
        tempPoint = new PointYio();

        initPools();
        initRepeats();
    }


    private void initRepeats() {
        repeatRemoveParticles = new RepeatYio<ParticlesManager>(this, 2 * 60) {
            @Override
            public void performAction() {
                parent.removeDeadParticles();
            }
        };
    }


    private void initPools() {
        poolParticles = new ObjectPoolYio<Particle>() {
            @Override
            public Particle makeNewObject() {
                return new Particle(ParticlesManager.this);
            }
        };
    }


    private void removeDeadParticles() {
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle particle = particles.get(i);
            if (!particle.isReadyToBeRemoved()) continue;

            removeParticle(particle);
        }
    }


    public void spawnSomeParticlesOnLink(Link link, FractionType previousFractionType) {
        if (particles.size() >= MAX_QUANTITY) return;

        FractionType effectFractionType = previousFractionType;
        if (previousFractionType == FractionType.neutral) {
            effectFractionType = link.fractionType;
        }

        for (int i = 0; i < 25; i++) {
            Particle freshParticle = getFreshParticle();

            tempPoint.setBy(link.one.viewPosition.center);
            tempPoint.relocateRadial(YioGdxGame.random.nextDouble() * link.distance, link.angle);

            freshParticle.setViewType(getRandomColoredViewType(convertFractionIntoPaViewType(effectFractionType)));
            freshParticle.viewPosition.center.setBy(tempPoint);
            freshParticle.viewPosition.setRadius((0.05f + 0.1f * YioGdxGame.random.nextFloat()) * 0.045f * GraphicsYio.width * 1);
            freshParticle.speed.relocateRadial(0.25f * 0.035f * GraphicsYio.width * YioGdxGame.random.nextFloat() * 1, Yio.getRandomAngle());
            freshParticle.setFriction(0.26f + 0.1f * YioGdxGame.random.nextFloat());
            checkForRedLine(freshParticle);

            freshParticle.updateMetrics();
        }
    }


    private void checkForRedLine(Particle freshParticle) {
        if (freshParticle.viewType != PaViewType.line_red) return;

        freshParticle.setHasAngle(true);
        freshParticle.setFriction(0.15f + 0.1f * YioGdxGame.random.nextFloat());
        freshParticle.viewPosition.radius *= 1.3f;
        freshParticle.speed.x *= 1.1f;
        freshParticle.speed.y *= 1.1f;
    }


    public PaViewType convertFractionIntoPaViewType(FractionType fractionType) {
        switch (fractionType) {
            default:
                return null;
            case neutral:
                return PaViewType.explosion1;
            case red:
                return PaViewType.red;
            case green:
                return PaViewType.green;
            case yellow:
                return PaViewType.yellow;
            case cyan:
                return PaViewType.cyan;
        }
    }


    public void spawnExplosion(PointYio pointYio, float scale, PaViewType paViewType) {
        if (particles.size() >= MAX_QUANTITY) return;

        for (int i = 0; i < 80; i++) {
            Particle freshParticle = getFreshParticle();

            freshParticle.setViewType(getRandomColoredViewType(paViewType));
            freshParticle.viewPosition.center.setBy(pointYio);
            freshParticle.viewPosition.setRadius((0.05f + 0.1f * YioGdxGame.random.nextFloat()) * 0.045f * GraphicsYio.width * scale);
            freshParticle.speed.relocateRadial(0.25f * 0.035f * GraphicsYio.width * YioGdxGame.random.nextFloat() * scale, Yio.getRandomAngle());
            freshParticle.setFriction(0.26f + 0.1f * YioGdxGame.random.nextFloat());
            checkForRedLine(freshParticle);

            freshParticle.updateMetrics();
        }
    }


    public PaViewType getRandomColoredViewType(PaViewType filterType) {
        if (YioGdxGame.random.nextFloat() < 0.2f) {
            return PaViewType.line_red;
        }

        return filterType;
    }


    private PaViewType getRandomViewType() {
        int index = YioGdxGame.random.nextInt(PaViewType.values().length);
        return PaViewType.values()[index];
    }


    private Particle getFreshParticle() {
        Particle next = poolParticles.getNext();
        next.setFriction(friction);
        next.setId(currentId);
        increaseCurrentId();

        if (SettingsManager.getInstance().graphicsQuality == GraphicsYio.QUALITY_LOW && currentId % 2 == 0) {
            return next;
        }

        particles.add(next);
        return next;
    }


    private void increaseCurrentId() {
        currentId++;

        if (currentId > maxId) {
            currentId = 0;
        }
    }


    public void clear() {
        while (particles.size() > 0) {
            removeParticle(particles.get(0));
        }
    }


    private void removeParticle(Particle particle) {
        poolParticles.add(particle);
        particles.remove(particle);
    }


    @Override
    public void moveActually() {
        moveParticlesActually();
        repeatRemoveParticles.move();
    }


    private void moveParticlesActually() {
        for (Particle particle : particles) {
            particle.moveActually();
        }
    }


    @Override
    public void moveVisually() {
        moveParticlesVisually();
    }


    private void moveParticlesVisually() {
        for (Particle particle : particles) {
            particle.moveVisually();
        }
    }
}
