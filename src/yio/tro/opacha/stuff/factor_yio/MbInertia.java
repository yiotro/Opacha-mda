package yio.tro.opacha.stuff.factor_yio;

public class MbInertia extends AbstractMoveBehavior {


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
        if (f.value > 0.999) {
            f.value = 1;
        }
        if (f.value < 0.4) {
            f.value += 0.07 * f.speedMultiplier * f.value;
        } else {
            f.value += 0.03 * f.speedMultiplier * (1 - f.value);
        }
    }


    private void moveDown(FactorYio f) {
        if (f.value > 0.99) {
            f.value = 0.99;
        }
        if (f.value < 0.01) {
            f.value = 0;
        }
        if (f.value > 0.6) {
            f.value -= 0.07 * f.speedMultiplier * (1 - f.value);
        } else {
            f.value -= 0.03 * f.speedMultiplier * f.value;
        }
    }


    @Override
    void onAppear(FactorYio f) {
        f.speedMultiplier *= 15;
    }


    @Override
    void onDestroy(FactorYio f) {
        f.speedMultiplier *= 15;
    }
}
