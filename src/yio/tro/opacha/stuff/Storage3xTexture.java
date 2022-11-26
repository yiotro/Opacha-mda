package yio.tro.opacha.stuff;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Storage3xTexture {

    TextureRegion normalTexture, lowTexture, lowestTexture;


    public Storage3xTexture() {
    }


    public Storage3xTexture(AtlasLoader atlasLoader, String fileName) {
        setTextures(atlasLoader, fileName);
    }


    public TextureRegion getNormal() {
        return normalTexture;
    }


    public void setNormalTexture(TextureRegion normalTexture) {
        this.normalTexture = normalTexture;
    }


    public TextureRegion getLow() {
        return lowTexture;
    }


    public void setLowTexture(TextureRegion lowTexture) {
        this.lowTexture = lowTexture;
    }


    public TextureRegion getLowest() {
        return lowestTexture;
    }


    public void setLowestTexture(TextureRegion lowestTexture) {
        this.lowestTexture = lowestTexture;
    }


    public TextureRegion getTexture(int quality) {
        if (quality == GraphicsYio.QUALITY_LOW) return getLowest();
        if (quality == GraphicsYio.QUALITY_NORMAL) return getLow();
        if (quality == GraphicsYio.QUALITY_HIGH) return getNormal();
        return getLowest();
    }


    public void setTextures(AtlasLoader atlasLoader, String fileName) {
        int index = fileName.indexOf(".");
        String name = fileName.substring(0, index);
        String ext = fileName.substring(index + 1, fileName.length());
        setNormalTexture(atlasLoader.getTexture(fileName));
        setLowTexture(atlasLoader.getTexture(name + "_low." + ext));
        setLowestTexture(atlasLoader.getTexture(name + "_lowest." + ext));
    }

}
