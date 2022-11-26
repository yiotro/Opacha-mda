package yio.tro.opacha.menu.elements.slider;

import yio.tro.opacha.menu.LanguagesManager;

public class SbDefault extends SliderBehavior {

    @Override
    public String getValueString(LanguagesManager languagesManager, SliderYio sliderYio) {
        return "" + sliderYio.getValueIndex();
    }


    @Override
    public void onAnotherSliderValueChanged(SliderYio sliderYio, SliderYio anotherSlider) {

    }


    @Override
    public void onValueChanged(SliderYio sliderYio) {

    }
}
