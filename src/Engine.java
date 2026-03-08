package spinDaWheel;

public class Engine {
    void printScreen(char[][] screen) {

        StringBuilder frame = new StringBuilder();
    
        for(int i = 0; i < screen.length; i++) {
            for(int j = 0; j < screen[0].length; j++) {
                frame.append(screen[i][j]);
            }
            frame.append('\n');
        }
    
        System.out.print(frame.toString());
    }

    static void clearScreen() {
        System.out.print("\033[2J");
    }
    
    static void resetCursor() {
        System.out.print("\033[H");
    }
}

class Constants {
    final int HEIGHT = 32, WIDTH = 55;

    final char[] RAMP = ".,-~:;=!*#$@".toCharArray();

    final Vector LIGHT = new Vector(0, 1, -1).normalize();

    int K1 = 15, K2 = 30;
}

abstract class SceneObject {

}

class Vector {
    double x, y, z;
    Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vector normalize() {
        double magnitude = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return new Vector(this.x / magnitude, this.y / magnitude, this.z / magnitude);
    }

    double dot(Vector v2) {
        double x = this.x * v2.x;
        double y = this.y * v2.y;
        double z = this.z * v2.z;

        return x + y + z;
    }
}

class Rotation {
    Vector rotateX(Vector v, double A) {
        double x = v.x;
        double y = v.y * Math.cos(Math.toRadians(A)) - v.z * Math.sin(Math.toRadians(A));
        double z = v.y * Math.sin(Math.toRadians(A)) + v.z * Math.cos(Math.toRadians(A));

        return new Vector(x, y, z);
    }

    Vector rotateZ(Vector v, double A) {
        double x = v.x * Math.cos(Math.toRadians(A)) - v.y * Math.sin(Math.toRadians(A));
        double y = v.x * Math.sin(Math.toRadians(A)) + v.y * Math.cos(Math.toRadians(A));
        double z = v.z;

        return new Vector(x, y, z);
    }

    Vector rotateY(Vector v, double A) {
        double x = v.x * Math.cos(Math.toRadians(A)) + v.z * Math.sin(Math.toRadians(A));
        double y = v.y;
        double z = - v.x * Math.sin(Math.toRadians(A)) + v.z * Math.cos(Math.toRadians(A));

        return new Vector(x, y, z);
    }
}