package yio.tro.opacha;

import yio.tro.opacha.stuff.RepeatYio;

import java.util.ArrayList;

public class MovableController {

    ArrayList<MovableYio> movables, addBuffer;
    RepeatYio<MovableController> repeatCheckToRemove;
    boolean bufferHasSomething;


    public MovableController() {

        movables = new ArrayList<>();
        addBuffer = new ArrayList<>();
        bufferHasSomething = false;

        repeatCheckToRemove = new RepeatYio<MovableController>(this, 600) {
            @Override
            public void performAction() {
                parent.checkToRemove();
            }
        };
    }


    public void clear() {
        movables.clear();
    }


    public void addMovable(MovableYio movable) {
        bufferHasSomething = true;
        Yio.addToEndByIterator(addBuffer, movable);
    }


    private void addFromBuffer(MovableYio movable) {
        Yio.addToEndByIterator(movables, movable);

        movable.onStart();
    }


    private void checkToRemove() {
        for (int i = movables.size() - 1; i >= 0; i--) {
            MovableYio movableYio = movables.get(i);
            if (!movableYio.isValid()) {
                Yio.removeByIterator(movables, movableYio);
            }
        }
    }


    public void move() {
        checkToProcessBuffer();
        for (MovableYio movable : movables) {
            if (!movable.isValid()) continue;
            movable.move();
        }

        repeatCheckToRemove.move();
    }


    private void checkToProcessBuffer() {
        if (!bufferHasSomething) return;

        while (addBuffer.size() > 0) {
            MovableYio movableYio = addBuffer.get(0);
            addBuffer.remove(0);
            addFromBuffer(movableYio);
        }

        bufferHasSomething = false;
    }
}
