package yio.tro.opacha.stuff.factor_yio;

public class MbStay extends AbstractMoveBehavior {


    @Override
    void updateNeedsToMoveValue(FactorYio f) {
        if (!f.needsToMove) return;
        f.needsToMove = false;
    }


    @Override
    void move(FactorYio f) {
        // nothing
    }
}
