import java.util.*;

abstract class SceneObject {

}

class Vector {
    double x, y, z;
    Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

class Torus extends SceneObject {
    double R, r;
    Torus(double R, double r) {
        this.R = R;
        this.r = r;
    }

    Vector surfacePoint(double theta, double phi) {
        double x = (this.R + this.r * Math.cos(Math.toRadians(theta))) * Math.cos(Math.toRadians(phi));
        double y = (this.R + this.r * Math.cos(Math.toRadians(theta))) * Math.sin(Math.toRadians(phi));
        double z = this.r * Math.sin(Math.toRadians(theta));

        return new Vector(x, y, z);
    }

    Vector calculateNormals(double theta, double phi) {
        double nx = Math.cos(Math.toRadians(theta)) * Math.cos(Math.toRadians(phi));
        double ny = Math.cos(Math.toRadians(theta)) * Math.sin(Math.toRadians(phi));
        double nz = Math.sin(Math.toRadians(theta));

        return new Vector(nx, ny, nz);
    }
}

public class spin {
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

    Vector normalize(Vector v) {
        double magnitude = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
        return new Vector(v.x / magnitude, v.y / magnitude, v.z / magnitude);
    }

    double dot(Vector v1, Vector v2) {
        double x = v1.x * v2.x;
        double y = v1.y * v2.y;
        double z = v1.z * v2.z;

        return x + y + z;
    }

    static void clearScreen() {
        System.out.print("\033[2J");
    }
    
    static void resetCursor() {
        System.out.print("\033[H");
    }

    public static void main(String[] args) {
        spin engine = new spin();

        Torus torus = new Torus(20, 5);
        int K1 = 15, K2 = 30;

        int height = 32, width = 80;

        char[] ramp = ".,-~:;=!*#$@".toCharArray();
        Vector light = engine.normalize(new Vector(0, 1, -1));

        double[][] zbuffer = new double[height][width];
        char[][] screen = new char[height][width];

        double rx = 20, rz = 0, ry = 30;

        clearScreen();
        while(true) {            
            for (int i = 0; i < height; i++) {
                Arrays.fill(zbuffer[i], 0);
                Arrays.fill(screen[i], ' ');
            }

            rx = (rx + 2) % 360;
            rz = (rz + 1.5) % 360;
            ry = (ry + 1) % 360;
    
            for(double theta = 0; theta < 360; theta += 10) {
                for(double phi = 0; phi < 360; phi += 5) {

                    Vector point = torus.surfacePoint(theta, phi);
                    point = engine.rotateX(point, rx);
                    point = engine.rotateZ(point, rz);
                    point = engine.rotateY(point, ry);
    
                    Vector normal = torus.calculateNormals(theta, phi);
                    normal = engine.normalize(normal);
                    normal = engine.rotateX(normal, rx);
                    normal = engine.rotateZ(normal, rz);
                    normal = engine.rotateY(normal, ry);
    
                    double brightness = engine.dot(normal, light);
                    brightness = Math.max(0, Math.min(1, brightness));
    
                    double index = brightness * (ramp.length - 1);
                    index = Math.max(0, Math.min(index, ramp.length-1));
    
                    char asciiChar = ramp[(int)index];
    
                    double screenX = width/2  + K1 * point.x / (point.z + K2);
                    double screenY = height/2 - 0.5 * K1 * point.y / (point.z + K2);
    
                    if(screenX >= 0 && screenX < width) {
                        if(screenY >= 0 && screenY < height) {
                            double invZ = 1 / (point.z + K2);
    
                            if (invZ > zbuffer[(int)screenY][(int)screenX]) {
                                zbuffer[(int)screenY][(int)screenX] = invZ;
                                screen[(int)screenY][(int)screenX] = asciiChar;
                            }
                        }
                    }
                }   
            }
            
            try {
                resetCursor();
                engine.printScreen(screen);

                Thread.sleep(50);
            } catch (Exception e) {
                System.out.println("Error in rendering the object" + e);
            }
        }
    }
}