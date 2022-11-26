package yio.tro.opacha.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.opacha.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.Masking;

public class RenderCustomizableList extends RenderInterfaceElement {


    private CustomizableListYio customizableListYio;
    private ShapeRenderer shapeRenderer;


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderFirstLayer(InterfaceElement element) {

    }


    @Override
    public void renderSecondLayer(InterfaceElement element) {
        customizableListYio = (CustomizableListYio) element;

        if (customizableListYio.getFactor().get() < 0.01) return;

//        GraphicsYio.setBatchAlpha(batch, customizableListYio.getAlpha());
//        renderShadow(customizableListYio.getViewPosition());
//        MenuRenders.renderRoundShape.renderRoundShape(
//                customizableListYio.getViewPosition(),
//                GroundIndex.BUTTON_WHITE,
//                customizableListYio.cornerRadius
//        );

        renderItems();

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderItems() {
//        batch.end();
//        Masking.begin();
//
//        shapeRenderer = menuViewYio.shapeRenderer;
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setProjectionMatrix(menuViewYio.orthoCam.combined);
//        shapeRenderer.rect(
//                customizableListYio.maskPosition.x,
//                customizableListYio.maskPosition.y,
//                customizableListYio.maskPosition.width,
//                customizableListYio.maskPosition.height
//        );
//        shapeRenderer.end();
//
//        batch.begin();
//        Masking.continueAfterBatchBegin();
        for (AbstractCustomListItem item : customizableListYio.items) {
            if (!item.isCurrentlyVisible()) continue;

            item.getRender().renderItem(item);
        }
//        Masking.end(batch);
    }


    @Override
    public void renderThirdLayer(InterfaceElement element) {

    }
}
