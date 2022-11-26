package yio.tro.opacha.menu.elements.slider;

import yio.tro.opacha.menu.LanguagesManager;

public class SbGraphics extends SliderBehavior {

    @Override
    public String getValueString(LanguagesManager languagesManager, SliderYio sliderYio) {
        switch (sliderYio.getValueIndex()) {
            default:
            case 0:
                return languagesManager.getString("graphics_low");
            case 1:
                return languagesManager.getString("graphics_normal");
            case 2:
                return languagesManager.getString("graphics_high");
        }
    }


    @Override
    public void onAnotherSliderValueChanged(SliderYio sliderYio, SliderYio anotherSlider) {

    }


    @Override
    public void onValueChanged(SliderYio sliderYio) {

    }
}
