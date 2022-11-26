package yio.tro.opacha.menu.elements;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.game.loading.LoadingParameters;
import yio.tro.opacha.game.loading.LoadingType;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RectangleYio;
import yio.tro.opacha.stuff.RenderableTextYio;

public class LoadingScreenView extends InterfaceElement<LoadingScreenView> {

    LoadingType loadingType;
    LoadingParameters loadingParameters;
    boolean readyToLaunchLoading;
    public RectangleYio bandPosition, progressPosition;
    float bandHeight;
    double progress;
    public RenderableTextYio title;


    public LoadingScreenView(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);

        loadingType = LoadingType.skirmish_create;
        loadingParameters = null;
        readyToLaunchLoading = false;
        progress = 0;
        bandPosition = new RectangleYio();
        progressPosition = new RectangleYio();
        bandHeight = 4 * GraphicsYio.borderThickness;

        title = new RenderableTextYio();
        title.setString(LanguagesManager.getInstance().getString("loading"));
        title.setFont(Fonts.smallFont);
        title.updateMetrics();
    }


    public void setLoadingType(LoadingType loadingType) {
        this.loadingType = loadingType;
    }


    public void setLoadingParameters(LoadingParameters loadingParameters) {
        this.loadingParameters = loadingParameters;
    }


    @Override
    protected LoadingScreenView getThis() {
        return this;
    }


    @Override
    public void move() {
        updateViewPosition();
        updateProgressPosition();
        checkToGoAboveGameView();
    }


    private void checkToGoAboveGameView() {
        if (!factorMoved) return;
        if (appearFactor.get() != 1) return;

        setOnTopOfGameView(true);
    }


    @Override
    protected void updateViewPosition() {
        if (!factorMoved) return;

        if (appearFactor.isInAppearState() && appearFactor.get() < 1) {
            viewPosition.width = position.width;
            viewPosition.height = position.height;
            viewPosition.x = position.x;
            viewPosition.y = position.y - (1 - appearFactor.get()) * position.height;
            return;
        }

        viewPosition.setBy(position);
    }


    private void updateProgressPosition() {
        progressPosition.setBy(bandPosition);
        progressPosition.width = (float) (progress * bandPosition.width);
    }


    private boolean checkToLaunchLoading() {
        if (!readyToLaunchLoading) return false;
        if (getFactor().get() < 1) return false;

        launchLoading();
        return true;
    }


    @Override
    protected void onSizeChanged() {
        super.onSizeChanged();
        updateMetrics();
    }


    @Override
    protected void onPositionChanged() {
        super.onPositionChanged();
        updateMetrics();
    }


    private void updateMetrics() {
        updateBandPosition();
        updateTextPosition();
    }


    private void updateTextPosition() {
        title.centerHorizontal(position);
        title.position.y = position.y + 0.52f * position.height;
    }


    private void updateBandPosition() {
        bandPosition.width = 0.8f * position.width;
        bandPosition.x = position.x + (position.width - bandPosition.width) / 2;
        bandPosition.height = bandHeight;
        bandPosition.y = position.y + 0.45f * position.height;
    }


    private void launchLoading() {
        readyToLaunchLoading = false;

        menuControllerYio.yioGdxGame.loadingManager.startLoading(loadingType, loadingParameters);
    }


    @Override
    public void onDestroy() {

    }


    public void updateProgress(double progress) {
        if (getFactor().get() < 1) return;

        this.progress = progress;
        updateProgressPosition();
    }


    public double getProgress() {
        return progress;
    }


    @Override
    public void onAppear() {
        progress = 0;
        readyToLaunchLoading = true;
    }


    @Override
    public boolean checkToPerformAction() {
        return checkToLaunchLoading();
    }


    @Override
    public boolean touchDown() {
        return false;
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
        return MenuRenders.renderLoadingScreenView;
    }
}
