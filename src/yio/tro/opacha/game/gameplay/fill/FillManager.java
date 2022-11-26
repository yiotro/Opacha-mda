package yio.tro.opacha.game.gameplay.fill;

import yio.tro.opacha.AdaptiveDifficultyManager;
import yio.tro.opacha.Yio;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.CameraController;
import yio.tro.opacha.game.campaign.CampaignManager;
import yio.tro.opacha.game.gameplay.IGameplayManager;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.game.gameplay.model.MatchResults;
import yio.tro.opacha.game.gameplay.model.Planet;
import yio.tro.opacha.game.touch_modes.TouchMode;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.CircleYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.RectangleYio;
import yio.tro.opacha.stuff.calendar.CalendarManager;
import yio.tro.opacha.stuff.factor_yio.FactorYio;

public class FillManager implements IGameplayManager{

    ObjectsLayer objectsLayer;
    Planet parent;
    public CircleYio viewPosition;
    public FactorYio appearFactor;
    float targetRadius;
    public MatchResults matchResults;


    public FillManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        parent = null;
        viewPosition = new CircleYio();
        appearFactor = new FactorYio();
    }


    @Override
    public void defaultValues() {
        parent = null;
        viewPosition.reset();
        appearFactor.reset();
        targetRadius = 0;
    }


    @Override
    public void onEndCreation() {

    }


    @Override
    public void moveActually() {

    }


    private void updateViewPosition() {
        viewPosition.center.setBy(parent.viewPosition.center);
        viewPosition.setRadius(appearFactor.get() * targetRadius);
    }


    public void launch(Planet planet) {
        parent = planet;
        appearFactor.appear(6, 0.7);
        updateTargetRadius();
        objectsLayer.gameController.setTouchMode(TouchMode.tmMatchEnded);
        Scenes.gameOverlay.destroy();
        Scenes.selectionOverlay.destroy();
        Scenes.velocityControls.destroy();
        Scenes.speedControls.destroy();
    }


    private void updateTargetRadius() {
        targetRadius = 0;
        CameraController cameraController = objectsLayer.gameController.cameraController;
        RectangleYio frame = cameraController.getFrame();
        assureTargetRadius(frame.x, frame.y);
        assureTargetRadius(frame.x + frame.width, frame.y);
        assureTargetRadius(frame.x, frame.y + frame.height);
        assureTargetRadius(frame.x + frame.width, frame.y + frame.height);

        targetRadius *= 1.15f;
    }


    public void assureTargetRadius(double x, double y) {
        PointYio pos = parent.viewPosition.center;
        targetRadius = (float) Math.max(targetRadius, Yio.distance(pos.x, pos.y, x, y));
    }


    public boolean isActive() {
        return appearFactor.isInAppearState();
    }


    public void setMatchResults(MatchResults matchResults) {
        this.matchResults = matchResults;
    }


    @Override
    public void moveVisually() {
        if (appearFactor.move()) {
            updateViewPosition();
            checkToFinish();
        }
    }


    private void checkToFinish() {
        if (appearFactor.get() < 1) return;
        checkToSaveCampaignProgress();
        checkToSaveCalendarProgress();
        switchToMenus();
    }


    private void checkToSaveCalendarProgress() {
        if (!matchResults.humanWon) return;
        if (!matchResults.calendarMode) return;
        CalendarManager.getInstance().onCalendarDayCompleted(matchResults);
    }


    private void checkToSaveCampaignProgress() {
        if (!matchResults.humanWon) return;
        if (!matchResults.campaignMode) return;
        CampaignManager.getInstance().onLevelCompleted(matchResults.levelIndex);
    }


    private void switchToMenus() {
        YioGdxGame yioGdxGame = objectsLayer.gameController.yioGdxGame;
        MenuControllerYio menuControllerYio = yioGdxGame.menuControllerYio;
        yioGdxGame.setGamePaused(true);
        menuControllerYio.destroyGameView();
        AdaptiveDifficultyManager.getInstance().applyMatchResults(matchResults);

        if (matchResults.campaignMode) {
            if (matchResults.humanWon) {
                Scenes.onCampaignLevelCompleted.create();
                Scenes.onCampaignLevelCompleted.setCurrentLevelIndex(matchResults.levelIndex);
                menuControllerYio.onReturningBackButtonPressed();
                return;
            } else {
                Scenes.campaign.create();
                menuControllerYio.onReturningBackButtonPressed();
                return;
            }
        }

        if (matchResults.calendarMode) {
            Scenes.calendar.create();
            menuControllerYio.onReturningBackButtonPressed();
            return;
        }

        Scenes.chooseGameMode.create();
        menuControllerYio.onReturningBackButtonPressed();
    }
}
