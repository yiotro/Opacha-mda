package yio.tro.opacha.menu.elements.egg;

import yio.tro.opacha.Yio;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.CircleYio;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.RepeatYio;
import yio.tro.opacha.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class AnimationEggElement extends InterfaceElement<AnimationEggElement> {

    boolean readyToDestroy;
    public ArrayList<AeItem> masks;
    ObjectPoolYio<AeItem> poolItems;
    CircleYio tempCircle;
    RepeatYio<AnimationEggElement> repeatSpawnMasks, repeatRemoveMasks, repeatSpawnColors, repeatRemoveColors;
    boolean alive;
    public ArrayList<AeItem> colors;
    PointYio centerOfGravity;


    public AnimationEggElement(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);

        masks = new ArrayList<>();
        tempCircle = new CircleYio();
        colors = new ArrayList<>();
        centerOfGravity = new PointYio();

        initPools();
        initRepeats();
    }


    private void initRepeats() {
        repeatSpawnMasks = new RepeatYio<AnimationEggElement>(this, 25) {
            @Override
            public void performAction() {
                parent.spawnRandomMask();
            }
        };

        repeatRemoveMasks = new RepeatYio<AnimationEggElement>(this, 60) {
            @Override
            public void performAction() {
                parent.removeDeadMasks();
            }
        };

        repeatSpawnColors = new RepeatYio<AnimationEggElement>(this, 15) {
            @Override
            public void performAction() {
                parent.spawnRandomColor();
            }
        };

        repeatRemoveColors = new RepeatYio<AnimationEggElement>(this, 60) {
            @Override
            public void performAction() {
                parent.removeDeadColors();
            }
        };
    }


    void removeDeadColors() {
        for (int i = colors.size() - 1; i >= 0; i--) {
            AeItem aeItem = colors.get(i);
            if (aeItem.isAlive()) continue;

            removeColor(aeItem);
        }
    }


    void removeColor(AeItem color) {
        poolItems.add(color);
        colors.remove(color);
    }


    void removeDeadMasks() {
        for (int i = masks.size() - 1; i >= 0; i--) {
            AeItem aeItem = masks.get(i);
            if (aeItem.isAlive()) continue;

            removeMask(aeItem);
        }
    }


    private void removeMask(AeItem aeItem) {
        poolItems.add(aeItem);
        masks.remove(aeItem);
    }


    private void initPools() {
        poolItems = new ObjectPoolYio<AeItem>() {
            @Override
            public AeItem makeNewObject() {
                return new AeItem(AnimationEggElement.this);
            }
        };
    }


    @Override
    protected AnimationEggElement getThis() {
        return this;
    }


    @Override
    public void move() {

    }


    @Override
    protected void onApplyParent() {
        super.onApplyParent();
        fixViewPosition();
        updateCenterOfGravity();
        moveInternals();
    }


    private void fixViewPosition() {
        viewPosition.x = parent.getViewPosition().x;
        viewPosition.y = 0;
        viewPosition.width = GraphicsYio.width;
        viewPosition.height = GraphicsYio.height;

        if (appearFactor.isInAppearState() && appearFactor.get() < 1) {
            viewPosition.x = (1 - appearFactor.get()) * GraphicsYio.width;
        }
    }


    private void updateCenterOfGravity() {
        if (!factorMoved) return;

        centerOfGravity.x = viewPosition.x + viewPosition.width / 2;
        centerOfGravity.y = viewPosition.y + viewPosition.height / 2;
    }


    private void moveInternals() {
        if (appearFactor.get() < 1 && !appearFactor.isInDestroyState()) return;

        moveMasks();
        moveColors();
        if (alive) {
            repeatSpawnMasks.move();
            repeatSpawnColors.move();
        }
        repeatRemoveMasks.move();
        repeatRemoveColors.move();
    }


    private void moveColors() {
        for (AeItem color : colors) {
            color.move();
        }
    }


    private void spawnRandomColor() {
        if (colors.size() > 15) return;

        tempCircle.setRadius((0.07f + 0.1f * YioGdxGame.random.nextFloat()) * GraphicsYio.width);
        tempCircle.center.set(
                YioGdxGame.random.nextFloat() * GraphicsYio.width,
                YioGdxGame.random.nextFloat() * GraphicsYio.height
        );

        AeItem freshColor = getFreshColor();

        freshColor.startPosition.setBy(tempCircle);
        freshColor.startPosition.setRadius(0);
        freshColor.endPosition.setBy(tempCircle);
        freshColor.appearFactor.appear(3, 0.5 + 0.25 * YioGdxGame.random.nextDouble());
        freshColor.setViewIndex(YioGdxGame.random.nextInt(5));
    }


    private AeItem getFreshColor() {
        AeItem next = poolItems.getNext();
        colors.add(next);
        next.onSpawned();
        return next;
    }


    private void moveMasks() {
        for (AeItem mask : masks) {
            mask.move();
        }
    }


    private void spawnRandomMask() {
        if (masks.size() > 10) return;

        tempCircle.setRadius((0.09f + 0.12f * YioGdxGame.random.nextFloat()) * GraphicsYio.width);
        tempCircle.center.setBy(centerOfGravity);
        tempCircle.center.relocateRadial(
                0.4f * GraphicsYio.width * YioGdxGame.random.nextFloat(),
                Yio.getRandomAngle()
        );

        AeItem freshMask = getFreshMask();

        freshMask.startPosition.setBy(tempCircle);
        freshMask.startPosition.setRadius(0);
        freshMask.endPosition.setBy(tempCircle);
        freshMask.appearFactor.appear(3, 0.5 + 0.25 * YioGdxGame.random.nextDouble());
        freshMask.setGravity(3 * freshMask.gravity);
        freshMask.setGoToCenterAfterDeath(true);
    }


    private AeItem getFreshMask() {
        AeItem next = poolItems.getNext();
        masks.add(next);
        next.onSpawned();
        return next;
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        readyToDestroy = false;
        alive = true;
        clearMasks();
        clearColors();
    }


    private void clearColors() {
        while (colors.size() > 0) {
            removeColor(colors.get(0));
        }
    }


    private void clearMasks() {
        while (masks.size() > 0) {
            removeMask(masks.get(0));
        }
    }


    @Override
    public boolean checkToPerformAction() {
        if (readyToDestroy && areAllMasksDead()) {
            readyToDestroy = false;

            Scenes.mainMenu.create();

            return true;
        }

        return false;
    }


    @Override
    public boolean touchDown() {
        if (getFactor().get() < 0.1) return false;

        kill();

        return true;
    }


    boolean areAllMasksDead() {
        for (AeItem mask : masks) {
            if (mask.isAlive()) return false;
        }

        return true;
    }


    private void kill() {
        readyToDestroy = true;
        alive = false;

        for (AeItem mask : masks) {
            mask.kill();
        }
    }


    @Override
    public boolean touchDrag() {
        if (getFactor().get() < 0.1) return false;

        return true;
    }


    @Override
    public boolean touchUp() {
        if (getFactor().get() < 0.1) return false;

        return true;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderAnimationEgg;
    }
}
