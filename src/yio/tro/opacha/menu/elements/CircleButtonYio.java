package yio.tro.opacha.menu.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.SoundManager;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.JumpEngineYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.factor_yio.FactorYio;

public class CircleButtonYio extends InterfaceElement<CircleButtonYio> {

    public FactorYio selectionFactor;
    private boolean needToPerformAction;
    boolean returningBackButton;
    private long timeToPerformAction;
    public TextureRegion textureRegion;
    float touchOffset;
    float effectDeltaRadius;
    JumpEngineYio jumpEngineYio;
    PointYio centerView;
    Reaction reaction;
    String texturePath;


    public CircleButtonYio(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);

        selectionFactor = new FactorYio();
        jumpEngineYio = new JumpEngineYio();
        needToPerformAction = false;
        timeToPerformAction = 0;
        returningBackButton = false;
        textureRegion = null;
        reaction = Reaction.rbNothing;
        centerView = new PointYio();
        effectDeltaRadius = 0.1f * GraphicsYio.width;
        texturePath = null;

        setAnimation(AnimationYio.def);
    }


    @Override
    protected CircleButtonYio getThis() {
        return this;
    }


    @Override
    public void move() {
        selectionFactor.move();
        jumpEngineYio.move();

        updateViewPosition();
    }


    @Override
    protected void onApplyParent() {
        super.onApplyParent();
        updateCenterView();
    }


    private void updateCenterView() {
        centerView.x = viewPosition.x + viewPosition.width / 2;
        centerView.y = viewPosition.y + viewPosition.height / 2;
    }


    public boolean isSelected() {
        return selectionFactor.get() > 0;
    }


    @Override
    public void onDestroy() {
        appearFactor.destroy(1, 3);
    }


    @Override
    public void onAppear() {
        jumpEngineYio.reset();

        selectionFactor.setValues(0, 0);
        selectionFactor.stop();
    }


    @Override
    public boolean checkToPerformAction() {
        if (needToPerformAction && System.currentTimeMillis() > timeToPerformAction) {
            needToPerformAction = false;
            reaction();
            if (returningBackButton) {
                menuControllerYio.onReturningBackButtonPressed();
            }
            return true;
        }

        return false;
    }


    private void reaction() {
        reaction.performReactActions(menuControllerYio);
    }


    public CircleButtonYio tagAsBackButton() {
        returningBackButton = true;
        return getThis();
    }


    @Override
    public void pressArtificially() {
        press();
    }


    @Override
    public boolean touchDown() {
        if (currentTouch.distanceTo(centerView) < viewPosition.width / 2 + touchOffset) {
            press();
            return true;
        }

        return false;
    }


    public void press() {
        if (getFactor().get() < 0.7) return;
        if (appearFactor.isInDestroyState()) return;

        selectionFactor.setValues(1, 0);
        selectionFactor.destroy(1, 0.5);

        jumpEngineYio.apply(1, 0.45);

        playSound();
        menuControllerYio.yioGdxGame.render();
        needToPerformAction = true;
        timeToPerformAction = System.currentTimeMillis() + 100;
    }


    private void playSound() {
        // special sound for buttons that return back
        if (returningBackButton) {
            SoundManager.playSound(SoundManager.backButton);
            return;
        }

        SoundManager.playSound(SoundManager.button);
    }


    public float getEffectX() {
        return viewPosition.x + viewPosition.width / 2;
    }


    public float getEffectY() {
        return viewPosition.y + viewPosition.height / 2;
    }


    public float getEffectRadius() {
        return 0.5f * viewPosition.width + jumpEngineYio.getValue() * effectDeltaRadius;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    public CircleButtonYio setReaction(Reaction reaction) {
        this.reaction = reaction;

        return getThis();
    }


    @Override
    public CircleButtonYio clone(InterfaceElement src) {
        super.clone(src);

        CircleButtonYio srcButton = (CircleButtonYio) src;
        touchOffset = srcButton.touchOffset;

        setAnimation(src.animType);

        return getThis();
    }


    public CircleButtonYio loadTexture(String path) {
        textureRegion = GraphicsYio.loadTextureRegion(path, true);
        texturePath = path;

        return this;
    }


    public CircleButtonYio setTouchOffset(double offset) {
        touchOffset = (float) (offset * GraphicsYio.width);

        return this;
    }


    @Override
    public boolean touchUp() {
        return false;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderCircleButton;
    }


    public boolean isReturningBackButton() {
        return returningBackButton;
    }


    @Override
    public String toString() {
        return "[CircleButton: " +
                texturePath +
                "]";
    }
}
