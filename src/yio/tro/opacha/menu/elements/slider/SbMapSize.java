package yio.tro.opacha.menu.elements.slider;

import yio.tro.opacha.menu.LanguagesManager;

public class SbMapSize extends SliderBehavior {

    @Override
    public String getValueString(LanguagesManager languagesManager, SliderYio sliderYio) {
        switch (sliderYio.getValueIndex()) {
            default:
                return "unknown";
            case 0:
                return languagesManager.getString("tiny");
            case 1:
                return languagesManager.getString("small");
            case 2:
                return languagesManager.getString("medium");
            case 3:
                return languagesManager.getString("big");
        }
    }


    @Override
    public void onAnotherSliderValueChanged(SliderYio sliderYio, SliderYio anotherSlider) {

    }


    @Override
    public void onValueChanged(SliderYio sliderYio) {

    }
}
