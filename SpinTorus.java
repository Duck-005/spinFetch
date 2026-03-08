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
        
        int height = constants.height, width = constants.width;
        char[] ramp = constants.ramp;
        int K1 = constants.K1, K2 = constants.K2;
        Vector light = constants.light;

        double[][] zbuffer = new double[height][width];
        char[][] screen = new char[height][width];

        double rx = 20, rz = 0, ry = 30;

        engine.clearScreen();
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

                    point = rotation.rotateX(point, rx);
                    point = rotation.rotateZ(point, rz);
                    point = rotation.rotateY(point, ry);
    
                    Vector normal = torus.calculateNormals(theta, phi);
                    normal = normal.normalize();
                    
                    normal = rotation.rotateX(normal, rx);
                    normal = rotation.rotateZ(normal, rz);
                    normal = rotation.rotateY(normal, ry);
    
                    double brightness = normal.dot(light);
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
                engine.resetCursor();
                engine.printScreen(screen);

                Thread.sleep(50);
            } catch (Exception e) {
                System.out.println("Error in rendering the object" + e);
            }
        }
    }
}