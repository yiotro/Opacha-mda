package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.game.GameController;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.elements.ground.GpSrNone;

public abstract class AbstractLoadingSceneYio extends SceneYio{

    private ButtonYio label;
    boolean ready;


    @Override
    protected void initialize() {
        setBackground(getBackgroundIndex());
        getGround().setSpawnRule(new GpSrNone());
        ready = false;

        label = uiFactory.getButton()
                .setSize(0.7, 0.06)
                .centerHorizontal()
                .centerVertical()
                .applyText("...")
                .setBorder(false)
                .setBackgroundEnabled(false)
                .setTouchable(false);
    }


    @Override
    public void move() {
        super.move();

        if (!ready) return;
        if (label.getFactor().get() < 1) return;

        ready = false;
        applyAction();
    }


    protected abstract int getBackgroundIndex();


    protected abstract void applyAction();


    @Override
    protected void onAppear() {
        super.onAppear();

        yioGdxGame.setGamePaused(true);
        GameController gameController = yioGdxGame.gameController;
        gameController.onEscapedToPauseMenu();
        menuControllerYio.destroyGameView();
        ready = true;
    }
}
