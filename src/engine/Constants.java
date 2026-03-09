package engine;

public class Constants {
    public final int HEIGHT = 32, WIDTH = 70;

    public final char[] RAMP = ".,-~:;=!*#$@".toCharArray();

    public final Vector LIGHT = new Vector(0, 1, -1).normalize();

    public int K1 = 15, K2 = 50; // scale and camera distance to object
}