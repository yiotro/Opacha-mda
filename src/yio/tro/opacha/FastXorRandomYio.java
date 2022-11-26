package yio.tro.opacha;

import java.util.Random;

public class FastXorRandomYio {

    private static FastXorRandomYio instance;
    private long value;


    public FastXorRandomYio() {
        value = (new Random()).nextInt();
    }


    public static void initialize() {
        instance = null;
    }


    public static FastXorRandomYio getInstance() {
        if (instance == null) {
            instance = new FastXorRandomYio();
        }

        return instance;
    }


    public float getRandomFloat() {
        return ((float) getRandomInteger(251)) / 250f;
    }


    public int getRandomInteger(int limit) {
        return Math.abs(getRandomInteger()) % limit;
    }


    public int getRandomInteger() {
        value ^= value << 16;
        value ^= value >> 12;

        return (int)(value);
    }
}
