package yio.tro.opacha.menu.menu_renders.render_custom_list;

import yio.tro.opacha.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.opacha.menu.elements.customizable_list.SampleListItem;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;

public class RenderSampleListItem extends AbstractRenderCustomListItem{

    private SampleListItem sampleListItem;


    public RenderSampleListItem() {

    }


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        sampleListItem = (SampleListItem) item;

        renderTextOptimized(sampleListItem.title, sampleListItem.customizableListYio.getAlpha());
    }

}
