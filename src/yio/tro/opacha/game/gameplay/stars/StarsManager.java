package yio.tro.opacha.game.gameplay.stars;

import yio.tro.opacha.SettingsManager;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.LevelSize;
import yio.tro.opacha.game.gameplay.AcceleratableYio;
import yio.tro.opacha.game.gameplay.IGameplayManager;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RepeatYio;
import yio.tro.opacha.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class StarsManager implements IGameplayManager {

    ObjectsLayer objectsLayer;
    public ArrayList<Star> stars;
    ObjectPoolYio<Star> poolStars;
    RepeatYio<StarsManager> repeatSpawn, repeatRemove;


    public StarsManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        stars = new ArrayList<>();
        initPools();
        initRepeats();
    }


    private void initRepeats() {
        repeatSpawn = new RepeatYio<StarsManager>(this, 6) {
            @Override
            public void performAction() {
                parent.spawnFewStars();
            }
        };
        repeatRemove = new RepeatYio<StarsManager>(this, 60) {
            @Override
            public void performAction() {
                parent.checkToRemoveStars();
            }
        };
    }


    void checkToRemoveStars() {
        for (int i = stars.size() - 1; i >= 0; i--) {
            Star star = stars.get(i);
            if (star.isAlive()) continue;
            if (star.appearFactor.get() > 0) continue;
            poolStars.removeFromExternalList(star);
        }
    }


    private void initPools() {
        poolStars = new ObjectPoolYio<Star>(stars) {
            @Override
            public Star makeNewObject() {
                return new Star(StarsManager.this);
            }
        };
    }


    private void spawnFewStars() {
        if (SettingsManager.getInstance().graphicsQuality == GraphicsYio.QUALITY_LOW) return;
        for (int spawnRate = getSpawnRate(); spawnRate > 0; spawnRate--) {
            spawnRandomStar();
        }
    }


    @Override
    public void defaultValues() {

    }


    private int getSpawnRate() {
        switch (objectsLayer.gameController.initialLevelSize) {
            default:
                return 1;
            case tiny:
                return 1;
            case small:
                return 4;
            case normal:
                return 16;
            case big:
                return 36;
            case giant:
                return 100;
        }
    }


    private void spawnRandomStar() {
        Star freshObject = poolStars.getFreshObject();
        freshObject.position.setRadius(getStarRadius());
        freshObject.setPosition(
                getStarRadius() + YioGdxGame.random.nextDouble() * (objectsLayer.gameController.boundWidth - 2 * getStarRadius()),
                getStarRadius() + YioGdxGame.random.nextDouble() * (objectsLayer.gameController.boundHeight - 2 * getStarRadius())
        );
        freshObject.spawn();
    }


    private float getStarRadius() {
        return 0.01f * GraphicsYio.width;
    }


    public void onEndCreation() {
        poolStars.clearExternalList();
        for (int i = 0; i < 600; i++) {
            moveActually();
            moveVisually();
        }
    }


    @Override
    public void moveActually() {
        moveStarsActually();
        repeatSpawn.move();
        repeatRemove.move();
    }


    private void moveStarsActually() {
        for (Star star : stars) {
            star.moveActually();
        }
    }


    @Override
    public void moveVisually() {
        moveStarsVisually();
    }


    private void moveStarsVisually() {
        for (Star star : stars) {
            star.moveVisually();
        }
    }
}
