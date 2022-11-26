package yio.tro.opacha.game.gameplay.ai;

import yio.tro.opacha.game.GameMode;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.gameplay.IGameplayManager;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.game.gameplay.model.FractionType;

import java.util.ArrayList;

public class AiManager implements IGameplayManager{

    ObjectsLayer objectsLayer;
    public ArrayList<AbstractAiEntity> entities;


    public AiManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        entities = new ArrayList<>();
    }


    @Override
    public void defaultValues() {

    }


    @Override
    public void onEndCreation() {
        createEntities();
        for (AbstractAiEntity entity : entities) {
            entity.onEndCreation();
        }
    }


    private void createEntities() {
        for (FractionType fractionType : FractionType.values()) {
            if (fractionType == FractionType.neutral) continue;
            if (!objectsLayer.planetsManager.isFractionAlive(fractionType)) continue;
            if (fractionType == FractionType.green && !GameRules.aiOnlyMode) continue;
            addEntity(fractionType);
        }
    }


    private void addEntity(FractionType fractionType) {
        AbstractAiEntity aiEntity = new AiCustomEntity(objectsLayer);
        aiEntity.setFractionType(fractionType);
        aiEntity.setDifficulty(GameRules.difficulty);
        entities.add(aiEntity);
    }


    @Override
    public void moveActually() {
        if (objectsLayer.gameController.gameMode == GameMode.editor) return;
        for (AbstractAiEntity entity : entities) {
            entity.moveActually();
        }
    }


    @Override
    public void moveVisually() {
        for (AbstractAiEntity entity : entities) {
            entity.moveVisually();
        }
    }
}
