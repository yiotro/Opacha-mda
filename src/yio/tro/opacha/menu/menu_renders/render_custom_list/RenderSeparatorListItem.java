package yio.tro.opacha.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.opacha.menu.elements.customizable_list.SeparatorListItem;
import yio.tro.opacha.stuff.GraphicsYio;

public class RenderSeparatorListItem extends AbstractRenderCustomListItem{


    private TextureRegion separatorTexture;
    private SeparatorListItem separatorListItem;


    @Override
    public void loadTextures() {
        separatorTexture = GraphicsYio.loadTextureRegion("menu/campaign/separator.png", true);
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        separatorListItem = (SeparatorListItem) item;

        GraphicsYio.setBatchAlpha(batch, separatorListItem.customizableListYio.getAlpha());
        GraphicsYio.drawLine(batch, separatorTexture, separatorListItem.left);
        GraphicsYio.drawLine(batch, separatorTexture, separatorListItem.right);
        GraphicsYio.setBatchAlpha(batch, 1);

        renderTextOptimized(separatorListItem.title, separatorListItem.customizableListYio.getAlpha());
    }
}
