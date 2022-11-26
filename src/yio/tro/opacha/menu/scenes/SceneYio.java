package yio.tro.opacha.menu.scenes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.Yio;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.loading.LoadingListener;
import yio.tro.opacha.menu.elements.ground.GroundElement;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.*;
import yio.tro.opacha.menu.elements.*;
import yio.tro.opacha.stuff.GraphicsYio;

import java.util.ArrayList;
import java.util.StringTokenizer;

public abstract class SceneYio implements LoadingListener{

    public static ArrayList<SceneYio> sceneList;

    boolean initialized;
    protected YioGdxGame yioGdxGame;
    int backgroundIndex;
    public MenuControllerYio menuControllerYio;
    protected UiFactory uiFactory;
    protected LanguagesManager languagesManager;
    private ArrayList<InterfaceElement> localElementsList;
    protected InterfaceElement previousElement, currentAddedElement;
    protected GroundElement ground;


    public SceneYio() {
        initialized = false;
        if (sceneList == null) {
            sceneList = new ArrayList<>();
        }

        Yio.addByIterator(sceneList, this);

        localElementsList = new ArrayList<>();
        backgroundIndex = -1;
        previousElement = null;
        currentAddedElement = null;
        ground = null;
    }


    public static void onGeneralInitialization() {
        sceneList = null;
    }


    public void addLocalElement(InterfaceElement interfaceElement) {
        Yio.addToEndByIterator(localElementsList, interfaceElement);
        interfaceElement.setSceneOwner(this);

        previousElement = currentAddedElement;
        currentAddedElement = interfaceElement;

        if (ground != null) {
            currentAddedElement.setParent(ground);
        }
    }


    private void checkToInitialize() {
        if (initialized) return;
        initialized = true;

        prepareGround();
        initialize();

        endInitialization();
    }


    protected void prepareGround() {
        ground = uiFactory.getGroundElement()
                .setPosition(0, 0, 1, 1)
                .setAnimation(AnimationYio.horizontal_directed);
    }


    protected void endInitialization() {

    }


    public void create() {
        beginCreation();

        checkToInitialize();
        changeBackground();
        appear();

        endCreation();
    }


    @Override
    public void onLevelCreationEnd() {

    }


    @Override
    public void makeExpensiveLoadingStep(int step) {

    }


    protected void beginCreation() {
        menuControllerYio.setCurrentScene(this);
        destroyAllVisibleElements();
        menuControllerYio.checkToRemoveInvisibleElements();
    }


    private void endCreation() {
        for (int i = 0; i < localElementsList.size(); i++) {
            InterfaceElement element = localElementsList.get(i);

            if (element instanceof ScrollableAreaYio) {
                ((ScrollableAreaYio) element).forceToTop();
            }

            element.onSceneEndCreation();
        }

        onEndCreation();
    }


    protected void onEndCreation() {
        //
    }


    public void move() {

    }


    private void changeBackground() {
        if (backgroundIndex == -1) return;

        yioGdxGame.beginBackgroundChange(backgroundIndex);
    }


    protected void onAppear() {

    }


    protected final void appear() {
        onAppear();
        for (InterfaceElement interfaceElement : localElementsList) {
            appearElement(interfaceElement);
        }
    }


    protected void appearElement(InterfaceElement interfaceElement) {
        interfaceElement.appear();
        menuControllerYio.addVisibleElement(interfaceElement);
    }


    public void destroy() {
        onDestroy();
        for (InterfaceElement interfaceElement : localElementsList) {
            interfaceElement.destroy();
        }
    }


    protected void onDestroy() {
        // nothing by default
    }


    public static void updateAllScenes(MenuControllerYio menuControllerYio) {
        for (SceneYio sceneYio : sceneList) {
            sceneYio.updateLinks(menuControllerYio);
        }
    }


    public void updateLinks(MenuControllerYio menuControllerYio) {
        this.menuControllerYio = menuControllerYio;
        yioGdxGame = menuControllerYio.yioGdxGame;
        uiFactory = new UiFactory(this);
        languagesManager = menuControllerYio.languagesManager;
    }


    protected CircleButtonYio spawnBackButton(Reaction reaction) {
        CircleButtonYio backButton;

        backButton = uiFactory.getCircleButton()
                .setSize(GraphicsYio.convertToWidth(0.09))
                .alignLeft(0.04)
                .alignTop(0.02)
                .setTouchOffset(0.05)
                .loadTexture("menu/back_icon.png")
                .setAnimation(AnimationYio.none)
                .setReaction(reaction)
                .tagAsBackButton();

        return backButton;
    }


    protected void destroyAllVisibleElements() {
        if (!isDisruptive()) return;

        for (InterfaceElement interfaceElement : menuControllerYio.getInterfaceElements()) {
            if (!interfaceElement.isVisible()) continue;
            interfaceElement.destroy();
        }
    }


    protected boolean isDisruptive() {
        return true;
    }


    protected abstract void initialize();


    public void tagUnInitialized() {
        initialized = false;
        localElementsList.clear();
    }


    public void forceElementsToTop() {
        for (InterfaceElement interfaceElement : localElementsList) {
            forceElementToTop(interfaceElement);
        }
    }


    public void forceElementToTop(InterfaceElement interfaceElement) {
        menuControllerYio.removeVisibleElement(interfaceElement);
        menuControllerYio.addVisibleElement(interfaceElement);
    }


    protected void renderTextAndSomeEmptyLines(ButtonYio buttonYio, String text, int emptyLines) {
        buttonYio.addTextLine(text);
        for (int i = 0; i < emptyLines; i++) {
            buttonYio.addTextLine(" ");
        }
        buttonYio.render();
    }


    public static ArrayList<String> convertStringToArray(String src) {
        ArrayList<String> list = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(src, "#");
        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }


    public boolean isCurrentlyVisible() {
        for (InterfaceElement interfaceElement : localElementsList) {
            if (interfaceElement.getFactor().isInAppearState()) return true;
        }
        return false;
    }


    protected TextureRegion getSelectionTexture() {
        return GraphicsYio.loadTextureRegion("menu/selection.png", true);
    }


    public void setBackground(int backgroundIndex) {
        if (ground != null) {
            ground.setGroundIndex(backgroundIndex);
        }
    }


    public ArrayList<InterfaceElement> getLocalElementsList() {
        return localElementsList;
    }


    public GroundElement getGround() {
        return ground;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
