package spinDaWheel;

import java.util.*;

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

public class SpinTorus {
    public static void main(String[] args) {

        Engine engine = new Engine();
        Rotation rotation = new Rotation();
        Constants constants = new Constants();

        Torus torus = new Torus(20, 5);
        
        int HEIGHT = constants.HEIGHT, WIDTH = constants.WIDTH;
        char[] RAMP = constants.RAMP;
        int K1 = constants.K1, K2 = constants.K2;
        Vector LIGHT = constants.LIGHT;

        double[][] zbuffer = new double[HEIGHT][WIDTH];
        char[][] screen = new char[HEIGHT][WIDTH];

        double rx = 20, rz = 0, ry = 30;

        engine.clearScreen();
        while(true) {            
            for (int i = 0; i < HEIGHT; i++) {
                Arrays.fill(zbuffer[i], 0);
                Arrays.fill(screen[i], ' ');
            }

            rx = (rx + 2) % 360;
            rz = (rz + 1.5) % 360;
            ry = (ry + 1) % 360;
    
            for(double theta = 0; theta < 360; theta += 10) {
                for(double phi = 0; phi < 360; phi += 5) {

                    Vector point = torus.surfacePoint(theta, phi);

                    point = rotation.rotateX(point, rx);
                    point = rotation.rotateY(point, ry);
                    point = rotation.rotateZ(point, rz);
    
                    Vector normal = torus.calculateNormals(theta, phi);
                    
                    normal = rotation.rotateX(normal, rx);
                    normal = rotation.rotateY(normal, ry);
                    normal = rotation.rotateZ(normal, rz);

                    normal = normal.normalize();
    
                    double brightness = Math.max(0, normal.dot(LIGHT));
    
                    double index = brightness * (RAMP.length - 1);
                    index = Math.max(0, Math.min(index, RAMP.length-1));
    
                    char asciiChar = RAMP[(int)index];

                    double invZ = 1 / (point.z + K2);
    
                    double screenX = WIDTH/2  + K1 * point.x * invZ;
                    double screenY = HEIGHT/2 - 0.5 * K1 * point.y * invZ;
    
                    if(screenX >= 0 && screenX < WIDTH) {
                        if(screenY >= 0 && screenY < HEIGHT) {
    
                            if (invZ > zbuffer[(int)screenY][(int)screenX]) {
                                zbuffer[(int)screenY][(int)screenX] = invZ;
                                screen[(int)screenY][(int)screenX] = asciiChar;
                            }
                        }
                    }
                }   
            }
            
            try {
                engine.resetCursor();
                engine.printScreen(screen);

                Thread.sleep(30);
            } catch (Exception e) {
                System.out.println("Error in rendering the object" + e);
            }
        }
    }
}