package yio.tro.opacha.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.StringBuilder;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RenderableTextYio;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static yio.tro.opacha.Fonts.FONT_SIZE;

public class TextFitParser {

    private static TextFitParser instance = null;

    private static float horizontalOffset;
    ArrayList<String> text;
    ArrayList<Texture> texturesToDisposeInTheEnd;


    private TextFitParser() {
        texturesToDisposeInTheEnd = new ArrayList<>();
    }


    public static TextFitParser getInstance() {
        if (instance == null) {
            instance = new TextFitParser();
        }

        return instance;
    }


    public static void initialize() {
        instance = null;
    }


    private void updateInternalText(ButtonYio buttonYio) {
        text.clear();
        for (RenderableTextYio item : buttonYio.items) {
            text.add(item.string);
        }
    }


    private void resetHorizontalOffset(int FONT_SIZE) {
        horizontalOffset = (int) (0.3f * FONT_SIZE);
    }


    public void resetHorizontalOffset() {
        resetHorizontalOffset(FONT_SIZE);
    }


    public ArrayList<String> parseText(ArrayList<String> src, BitmapFont font) {
        return parseText(src, font, 0.99 * Gdx.graphics.getWidth(), false);
    }


    public ArrayList<String> parseText(ArrayList<String> src, BitmapFont font, double maxWidth, boolean resetOffset) {
        if (resetOffset) {
            resetHorizontalOffset();
        }

        ArrayList<String> result = new ArrayList<>();
        double currentX, currentWidth;
        StringBuilder builder = new StringBuilder();
        for (String srcLine : src) {
            currentX = horizontalOffset;
            String[] split = srcLine.split(" ");
            for (String token : split) {
                currentWidth = GraphicsYio.getTextWidth(font, token + " ");
                if (currentX + currentWidth > maxWidth) {
                    result.add(builder.toString());
                    builder = new StringBuilder();
                    currentX = 0;
                }
                builder.append(token).append(" ");
                currentX += currentWidth;
            }
            result.add(builder.toString());
            builder = new StringBuilder();
        }
        return result;
    }


    private int getExpectedOrthoWidth(ButtonYio buttonYio, BitmapFont font) {
        return Gdx.graphics.getWidth();
    }


    private int getTextWidth(String text, BitmapFont font) {
        return (int) GraphicsYio.getTextWidth(font, text);
    }


    private boolean hasTooLongWord(ButtonYio buttonYio, BitmapFont font) {
        updateInternalText(buttonYio);
        for (String s : text) {
            StringTokenizer tokenizer = new StringTokenizer(s, " ");
            while (tokenizer.hasMoreTokens()) {
                if (getTextWidth(tokenizer.nextToken() + " ", font) > 0.9 * getExpectedOrthoWidth(buttonYio, font))
                    return true;
            }
        }
        return false;
    }


    private void wrapText(ButtonYio buttonYio, BitmapFont font) {
        if (hasTooLongWord(buttonYio, font)) return;

        updateInternalText(buttonYio);
        if (text.size() > 3) {
            sayText(text);
        }
        ArrayList<String> tempList = new ArrayList<>();
        int lineWidth = getExpectedOrthoWidth(buttonYio, font) - (int) (0.1f * Gdx.graphics.getWidth());
        int currentWidth;

        for (int i = text.size() - 1; i >= 0; i--) {
            StringTokenizer tokenizer = new StringTokenizer(text.get(i), " ");
            StringBuffer stringBuffer = new StringBuffer();
            currentWidth = 0;

            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken();
                int wordWidth = getTextWidth(word + " ", font);

                if (currentWidth + wordWidth < lineWidth) { // add to current line
                    stringBuffer.append(word + " ");
                    currentWidth += wordWidth;
                } else { // next line
                    tempList.add(stringBuffer.toString());
                    stringBuffer = new StringBuffer();
                    stringBuffer.append(word + " ");
                }
            }

            if (stringBuffer.length() > 0) tempList.add(stringBuffer.toString());

            if (tempList.size() > 1) {
                text.remove(i);
                tempList.addAll(i, tempList);
            }
        }

        if (text.size() > 3) sayText(text);
    }


    private void sayText(ArrayList<String> text) {
        for (String s : text) {
            YioGdxGame.say("# " + s);
        }
        YioGdxGame.say("----");
    }


    public void disposeAllTextures() {
        for (Texture texture : texturesToDisposeInTheEnd) {
            texture.dispose();
        }

        texturesToDisposeInTheEnd.clear();
    }


    public void killInstance() {
        // this actually fixes stupid android bug
        // it's important and should not be deleted.

        // So here is the bug:
        // 1. Open app, go through menus, close app from main menu
        // 2. Open it again. Some buttons (with text) are now gray

        // It happens because android fucks up textures if they are in static objects
        // So I invalidate static instance of button renderer
        // to get them recreated after restart

        instance = null;
    }


}
