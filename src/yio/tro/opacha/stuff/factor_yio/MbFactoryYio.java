package yio.tro.opacha.stuff.factor_yio;

import java.util.HashMap;

public class MbFactoryYio {

    private static MbFactoryYio instance;
    HashMap<MovementType, AbstractMoveBehavior> mapBehaviors;


    public MbFactoryYio() {
        mapBehaviors = new HashMap<>();
        mapBehaviors.put(MovementType.inertia, new MbInertia());
        mapBehaviors.put(MovementType.stay, new MbStay());
        mapBehaviors.put(MovementType.old_approach, new MbOldApproach());
        mapBehaviors.put(MovementType.simple, new MbSimple());
        mapBehaviors.put(MovementType.material, new MbMaterial());
        mapBehaviors.put(MovementType.old_lighty, new MbOldLighty());
        mapBehaviors.put(MovementType.lighty, new MbLighty());
        mapBehaviors.put(MovementType.approach, new MbApproach());
    }


    public static void initialize() {
        instance = null;
    }


    public static MbFactoryYio getInstance() {
        if (instance == null) {
            instance = new MbFactoryYio();
        }
        return instance;
    }


    public AbstractMoveBehavior getBehavior(MovementType movementType) {
        return mapBehaviors.get(movementType);
    }
}
