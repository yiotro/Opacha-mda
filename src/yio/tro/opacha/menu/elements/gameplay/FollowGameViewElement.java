package yio.tro.opacha.menu.elements.gameplay;

import yio.tro.opacha.game.view.GameView;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;

public class FollowGameViewElement extends InterfaceElement<FollowGameViewElement> {

    public FollowGameViewElement(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);
    }


    @Override
    protected FollowGameViewElement getThis() {
        return this;
    }


    @Override
    public void move() {
        updateViewPosition();
    }


    @Override
    protected void updateViewPosition() {
        GameView gameView = menuControllerYio.yioGdxGame.gameView;

        if (gameView.appearFactor.get() > 0 || gameView.appearFactor.isInAppearState()) {
            viewPosition.setBy(gameView.transitionFramePosition);
            return;
        }

        viewPosition.setBy(screen.getViewPosition());
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {

    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        return false;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        return false;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderFollowGameView;
    }
}
