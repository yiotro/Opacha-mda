package yio.tro.opacha.stuff.factor_yio;

class MbSimple extends AbstractMoveBehavior {

    public MbSimple() {
    }


    @Override
    void onAppear(FactorYio f) {
        f.speedMultiplier *= 20;
    }


    @Override
    void onDestroy(FactorYio f) {
        f.speedMultiplier *= 20;
    }


    void applyStrictBounds(FactorYio f) {
        if (f.dy > 0 && f.value > 1) {
            f.value = 1;
        }
        if (f.dy < 0 && f.value < 0) {
            f.value = 0;
        }
    }


    @Override
    void move(FactorYio f) {
        if (f.dy == 0) {
            f.dy = f.gravity;
        }
        if (f.needsToMove) {
            f.value += f.speedMultiplier * f.dy;
        }
        applyStrictBounds(f);
    }
}
