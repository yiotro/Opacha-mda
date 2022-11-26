package yio.tro.opacha.menu.elements;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.game.VelocityManager;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RectangleYio;
import yio.tro.opacha.stuff.RenderableTextYio;
import yio.tro.opacha.stuff.RepeatYio;
import yio.tro.opacha.stuff.factor_yio.FactorYio;

import java.util.ArrayList;

public class QuickVelocityTutorialElement extends InterfaceElement<QuickVelocityTutorialElement> {

    public VelocitySliderElement velocitySliderElement;
    public RectangleYio focusPosition;
    public ArrayList<RectangleYio> blackouts;
    public FactorYio realFactor;
    public RenderableTextYio title;
    RepeatYio<QuickVelocityTutorialElement> repeatDecreaseSpeed;
    float backupSpeed;


    public QuickVelocityTutorialElement(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);
        velocitySliderElement = null;
        realFactor = new FactorYio();
        focusPosition = new RectangleYio();
        createTitle();
        createBlackouts();
        initRepeats();
    }


    private void initRepeats() {
        repeatDecreaseSpeed = new RepeatYio<QuickVelocityTutorialElement>(this, 2, 30) {
            @Override
            public void performAction() {
                parent.applyDecreaseSpeed();
            }
        };
    }


    private void createTitle() {
        title = new RenderableTextYio();
        title.setFont(Fonts.smallFont);
        title.setString(LanguagesManager.getInstance().getString("velocity_slider_tutorial"));
        title.updateMetrics();
    }


    private void createBlackouts() {
        blackouts = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            blackouts.add(new RectangleYio());
        }
    }


    @Override
    protected QuickVelocityTutorialElement getThis() {
        return this;
    }


    @Override
    public void move() {
        updateViewPosition();
    }


    @Override
    protected void onApplyParent() {
        super.onApplyParent();
        updateFocusPosition();
        checkToLaunchRealFactor();
        realFactor.move();
        moveBlackouts();
        moveTitle();
        moveDecrease();
    }


    private void moveDecrease() {
        if (realFactor.get() == 0) return;
        if (appearFactor.isInDestroyState()) return;
        repeatDecreaseSpeed.move();
    }


    private void applyDecreaseSpeed() {
        velocitySliderElement.decreaseSpeed();
    }


    private void updateFocusPosition() {
        focusPosition.setBy(velocitySliderElement.viewPosition);
        focusPosition.increase(0.025 * GraphicsYio.width);
    }


    private void moveTitle() {
        title.position.x = focusPosition.x;
        RectangleYio src = focusPosition;
        title.position.y = src.y - 0.015f * GraphicsYio.height;
        title.updateBounds();
    }


    private void checkToLaunchRealFactor() {
        if (realFactor.isInAppearState()) return;
        if (appearFactor.get() < 0.95) return;
        if (!menuControllerYio.yioGdxGame.gameView.coversAllScreen()) return;

        realFactor.appear(3, 0.8);
    }


    private void moveBlackouts() {
        RectangleYio src = focusPosition;

        blackouts.get(0).set(0, 0, GraphicsYio.width, src.y);
        blackouts.get(1).set(0, src.y + src.height, GraphicsYio.width, 2 * GraphicsYio.height);
        blackouts.get(2).set(0, src.y, src.x, src.height);
        blackouts.get(3).set(src.x + src.width, src.y, GraphicsYio.width, src.height);
    }


    @Override
    public void onDestroy() {
        VelocityManager.getInstance().value = backupSpeed;
        velocitySliderElement.loadValues();
    }


    @Override
    public void onAppear() {
        velocitySliderElement = Scenes.velocityControls.velocitySliderElement;
        realFactor.reset();
        backupSpeed = VelocityManager.getInstance().value;
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        Scenes.quickVelocityTutorial.destroy();
        return true;
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
        return MenuRenders.renderQuickVelocityTutorial;
    }
}
