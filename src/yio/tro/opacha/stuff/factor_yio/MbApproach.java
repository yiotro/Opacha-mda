package yio.tro.opacha.stuff.factor_yio;

public class MbApproach extends AbstractMoveBehavior{


    @Override
    void move(FactorYio f) {
        if (f.inAppearState) {
            moveUp(f);
            return;
        }
        moveDown(f);
    }


    private void moveUp(FactorYio f) {
        if (f.value > 0.99) {
            f.value = 1;
        }
        f.value += f.speedMultiplier * (1 - f.value);
        applyStrictBounds(f);
    }


    private void moveDown(FactorYio f) {
        if (f.value < 0.01) {
            f.value = 0;
        }
        f.value -= f.speedMultiplier * f.value;
        applyStrictBounds(f);
    }


    @Override
    void onAppear(FactorYio f) {
        super.onAppear(f);
        f.speedMultiplier *= 0.26;
    }


    @Override
    void onDestroy(FactorYio f) {
        super.onDestroy(f);
        f.speedMultiplier *= 0.26;
    }
}
