package yio.tro.opacha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import yio.tro.opacha.menu.CustomLanguageLoader;
import yio.tro.opacha.menu.LanguagesManager;

public class Fonts {

    public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<^>∞";
    // йцукенгшщзхъёфывапролджэячсмитьбюЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮіІїЇєЄ

    public static int FONT_SIZE;
    public static BitmapFont buttonFont;
    public static BitmapFont smallFont;
    public static BitmapFont miniFont;
    public static BitmapFont gameFont;
    public static BitmapFont unitFont;


    public static void initFonts() {
        // language should be loaded before fonts are created
        // because of lang_chars
        CustomLanguageLoader.loadLanguage();

        FileHandle fontFile = Gdx.files.internal("font.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FONT_SIZE = (int) (0.041 * Gdx.graphics.getHeight());

        parameter.size = (int) (1.0f * FONT_SIZE);
        parameter.characters = getAllCharacters();
        parameter.flip = false;
        buttonFont = generator.generateFont(parameter);
        buttonFont.setColor(Color.BLACK);

        parameter.size = (int) (0.8f * FONT_SIZE);
        parameter.flip = false;
        smallFont = generator.generateFont(parameter);
        smallFont.setColor(Color.BLACK);

        parameter.size = (int) (1.6f * FONT_SIZE);
        parameter.flip = false;
        gameFont = generator.generateFont(parameter);
        gameFont.setColor(Color.BLACK);
        gameFont.getData().setScale(0.3f);

        parameter.size = (int) (1.0f * FONT_SIZE);
        parameter.flip = false;
        unitFont = generator.generateFont(parameter);
        unitFont.setColor(Color.WHITE);
        unitFont.getData().setScale(0.3f);

        parameter.size = (int) (0.65 * FONT_SIZE);
        parameter.flip = false;
        miniFont = generator.generateFont(parameter);
        miniFont.setColor(Color.BLACK);

        generator.dispose();
    }


    public static String getAllCharacters() {
        String langChars = LanguagesManager.getInstance().getString("lang_characters");
        return langChars + FONT_CHARACTERS;
    }
}
