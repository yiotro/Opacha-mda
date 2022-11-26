package yio.tro.opacha.menu.menu_renders;

import yio.tro.opacha.menu.MenuViewYio;
import yio.tro.opacha.menu.menu_renders.render_custom_list.*;

import java.util.ArrayList;

public class MenuRenders {

    static ArrayList<RenderInterfaceElement> list = new ArrayList<>();

    public static RenderButton renderButton = new RenderButton();
    public static RenderSlider renderSlider = new RenderSlider();
    public static RenderCheckButton renderCheckButton = new RenderCheckButton();
    public static RenderScrollableArea renderScrollableArea = new RenderScrollableArea();
    public static RenderCircleButton renderCircleButton = new RenderCircleButton();
    public static RenderLoadingScreenView renderLoadingScreenView = new RenderLoadingScreenView();
    public static RenderNotification renderNotification = new RenderNotification();
    public static RenderRoundShape renderRoundShape = new RenderRoundShape();
    public static RenderShadow renderShadow = new RenderShadow();
    public static RenderGround renderGround = new RenderGround();
    public static RenderRoundBorder renderRoundBorder = new RenderRoundBorder();
    public static RenderScrollableList renderScrollableList = new RenderScrollableList();
    public static RenderFollowGameView renderFollowGameView = new RenderFollowGameView();
    public static RenderViewTouchMode renderViewTouchMode = new RenderViewTouchMode();
    public static RenderCustomizableList renderCustomizableList = new RenderCustomizableList();
    public static RenderSampleListItem renderSampleListItem = new RenderSampleListItem();
    public static RenderCampaignCustomItem renderCampaignCustomItem = new RenderCampaignCustomItem();
    public static RenderSeparatorListItem renderSeparatorListItem = new RenderSeparatorListItem();
    public static RenderAnimationEgg renderAnimationEgg = new RenderAnimationEgg();
    public static RenderLabelElement renderLabelElement = new RenderLabelElement();
    public static RenderNativeKeyboard renderNativeKeyboard = new RenderNativeKeyboard();
    public static RenderScrollListItem renderScrollListItem = new RenderScrollListItem();
    public static RenderLightBottomPanel renderLightBottomPanel = new RenderLightBottomPanel();
    public static RenderVelocitySlider renderVelocitySlider = new RenderVelocitySlider();
    public static RenderQuickVelocityTutorial renderQuickVelocityTutorial = new RenderQuickVelocityTutorial();
    public static RenderLockCameraElement renderLockCameraElement = new RenderLockCameraElement();
    public static RenderForefinger renderForefinger = new RenderForefinger();
    public static RenderGlobalMessageElement renderGlobalMessageElement = new RenderGlobalMessageElement();
    public static RenderScrollHelperElement renderScrollHelperElement = new RenderScrollHelperElement();
    public static RenderCalendarViewElement renderCalendarViewElement = new RenderCalendarViewElement();


    public static void updateRenderSystems(MenuViewYio menuViewYio) {
        for (RenderInterfaceElement renderInterfaceElement : list) {
            renderInterfaceElement.update(menuViewYio);
        }
    }
}
