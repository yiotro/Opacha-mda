package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.opacha.menu.elements.keyboard.NativeKeyboardElement;

public class SceneKeyboard extends ModalSceneYio {

    public NativeKeyboardElement nativeKeyboardElement;


    @Override
    protected void initialize() {
        nativeKeyboardElement = uiFactory.getNativeKeyboardElement()
                .setSize(1, 0.3)
                .centerHorizontal()
                .alignBottom()
                .setAnimation(AnimationYio.down);
    }


    @Override
    protected void onAppear() {
        forceElementToTop(nativeKeyboardElement);
    }


    public void setReaction(AbstractKbReaction reaction) {
        if (nativeKeyboardElement == null) return;

        nativeKeyboardElement.setReaction(reaction);
    }


    public void setValue(String value) {
        if (nativeKeyboardElement == null) return;

        nativeKeyboardElement.setValue(value);
    }


    public void hide() {
        if (nativeKeyboardElement == null) return;

        nativeKeyboardElement.destroy();
    }

}
