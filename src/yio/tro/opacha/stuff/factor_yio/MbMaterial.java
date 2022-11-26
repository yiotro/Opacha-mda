package yio.tro.opacha.stuff.factor_yio;

public class MbMaterial extends AbstractMoveBehavior {


    @Override
    void move(FactorYio f) {
        if (f.inAppearState) {
            moveUp(f);
            return;
        }
        moveDown(f);
    }


    private void moveUp(FactorYio f) {
        if (f.value < 0.01) {
            f.value = 0.01;
        }
        if (f.value > 0.99) {
            f.value = 1;
        }
        if (f.value < 0.5) {
            f.value += 0.05 * f.speedMultiplier * f.value;
        } else {
            f.value += 0.05 * f.speedMultiplier * (1 - f.value);
        }
    }


    private void moveDown(FactorYio f) {
        if (f.value > 0.99) {
            f.value = 0.99;
        }
        if (f.value < 0.01) {
            f.value = 0;
        }
        if (f.value > 0.5) {
            f.value -= 0.05 * f.speedMultiplier * (1 - f.value);
        } else {
            f.value -= 0.05 * f.speedMultiplier * f.value;
        }
    }


    @Override
    void onAppear(FactorYio f) {
        f.speedMultiplier *= 10;
    }


    @Override
    void onDestroy(FactorYio f) {
        f.speedMultiplier *= 10;
    }
}
