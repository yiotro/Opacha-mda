package yio.tro.opacha.menu;

import yio.tro.opacha.menu.elements.*;
import yio.tro.opacha.menu.elements.calendar.CalendarViewElement;
import yio.tro.opacha.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.opacha.menu.elements.egg.AnimationEggElement;
import yio.tro.opacha.menu.elements.gameplay.FollowGameViewElement;
import yio.tro.opacha.menu.elements.gameplay.ViewTouchModeElement;
import yio.tro.opacha.menu.elements.ground.GroundElement;
import yio.tro.opacha.menu.elements.keyboard.NativeKeyboardElement;
import yio.tro.opacha.menu.elements.scrollable_list.ScrollableListElement;
import yio.tro.opacha.menu.elements.slider.SliderYio;
import yio.tro.opacha.menu.elements.tutorial.ForefingerElement;
import yio.tro.opacha.menu.elements.tutorial.GlobalMessageElement;
import yio.tro.opacha.menu.scenes.SceneYio;

public class UiFactory {

    MenuControllerYio menuControllerYio;
    SceneYio sceneYio;
    private static int currentID = 0;


    public UiFactory(SceneYio sceneYio) {
        this.sceneYio = sceneYio;
        menuControllerYio = sceneYio.menuControllerYio;
    }


    public ButtonYio getButton() {
        ButtonYio buttonYio = new ButtonYio(menuControllerYio, getNewID());
        addElementToScene(buttonYio);

        if (sceneYio.getGround() != null) {
            buttonYio.setGroundIndex(sceneYio.getGround().getGroundIndex());
        }

        return buttonYio;
    }


    public SliderYio getSlider() {
        return (SliderYio) addElementToScene(new SliderYio(menuControllerYio, getNewID()));
    }


    public CheckButtonYio getCheckButton() {
        return (CheckButtonYio) addElementToScene(new CheckButtonYio(menuControllerYio, getNewID()));
    }


    public CircleButtonYio getCircleButton() {
        return (CircleButtonYio) addElementToScene(new CircleButtonYio(menuControllerYio, getNewID()));
    }


    public ScrollableAreaYio getScrollableAreaYio() {
        return (ScrollableAreaYio) addElementToScene(new ScrollableAreaYio(menuControllerYio, getNewID()));
    }


    public LoadingScreenView getLoadingScreen() {
        return (LoadingScreenView) addElementToScene(new LoadingScreenView(menuControllerYio, getNewID()));
    }


    public NotificationElement getNotificationElement() {
        return (NotificationElement) addElementToScene(new NotificationElement(menuControllerYio, getNewID()));
    }


    public GroundElement getGroundElement() {
        return (GroundElement) addElementToScene(new GroundElement(menuControllerYio, getNewID()));
    }


    public ScrollableListElement getScrollableList() {
        return (ScrollableListElement) addElementToScene(new ScrollableListElement(menuControllerYio, getNewID()));
    }


    public FollowGameViewElement getFollowGameViewElement() {
        return (FollowGameViewElement) addElementToScene(new FollowGameViewElement(menuControllerYio, getNewID()));
    }


    public ViewTouchModeElement getViewTouchModeElement() {
        return (ViewTouchModeElement) addElementToScene(new ViewTouchModeElement(menuControllerYio, getNewID()));
    }


    public CustomizableListYio getCustomizableListYio() {
        return (CustomizableListYio) addElementToScene(new CustomizableListYio(menuControllerYio, getNewID()));
    }


    public AnimationEggElement getAnimationEggElement() {
        return (AnimationEggElement) addElementToScene(new AnimationEggElement(menuControllerYio, getNewID()));
    }


    public LabelElement getLabelElement() {
        return (LabelElement) addElementToScene(new LabelElement(menuControllerYio, getNewID()));
    }


    public NativeKeyboardElement getNativeKeyboardElement() {
        return (NativeKeyboardElement) addElementToScene(new NativeKeyboardElement(menuControllerYio, getNewID()));
    }


    public LightBottomPanelElement getLightBottomPanelElement() {
        return (LightBottomPanelElement) addElementToScene(new LightBottomPanelElement(menuControllerYio, getNewID()));
    }


    public VelocitySliderElement getVelocitySliderElement() {
        return (VelocitySliderElement) addElementToScene(new VelocitySliderElement(menuControllerYio, getNewID()));
    }


    public QuickVelocityTutorialElement getQuickVelocityTutorialElement() {
        return (QuickVelocityTutorialElement) addElementToScene(new QuickVelocityTutorialElement(menuControllerYio, getNewID()));
    }


    public LockCameraElement getLockCameraElement() {
        return (LockCameraElement) addElementToScene(new LockCameraElement(menuControllerYio, getNewID()));
    }


    public ForefingerElement getForefingerElement() {
        return (ForefingerElement) addElementToScene(new ForefingerElement(menuControllerYio, getNewID()));
    }


    public GlobalMessageElement getGlobalMessageElement() {
        return (GlobalMessageElement) addElementToScene(new GlobalMessageElement(menuControllerYio, getNewID()));
    }


    public ScrollHelperElement getScrollHelperElement() {
        return (ScrollHelperElement) addElementToScene(new ScrollHelperElement(menuControllerYio, getNewID()));
    }


    public CalendarViewElement getCalendarViewElement() {
        return (CalendarViewElement) addElementToScene(new CalendarViewElement(menuControllerYio, getNewID()));
    }


    private int getNewID() {
        currentID++;
        return currentID;
    }


    private InterfaceElement addElementToScene(InterfaceElement interfaceElement) {
        sceneYio.addLocalElement(interfaceElement);
        menuControllerYio.addElement(interfaceElement);
        return interfaceElement;
    }
}
