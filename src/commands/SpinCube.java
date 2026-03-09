package commands;

import java.util.*;

import cli.Layout;
import cli.SystemInfo;

import engine.Engine;
import engine.Constants;
import engine.Rotation;
import engine.Vector;
import engine.Renderer;

class Cube {
    int length, breadth, height;

    Cube(int length, int breadth, int height) {
        this.length = length;
        this.breadth = breadth;
        this.height = height;
    }

    Vector surfacePoint(double x, double y, double z) {
        return new Vector(x, y, z);
    }
}

public class SpinCube {

    static Engine engine = new Engine();
    static Rotation rotation = new Rotation();
    static Constants constants = new Constants();

    static int HEIGHT = constants.HEIGHT, WIDTH = constants.WIDTH;
    static char[] RAMP = constants.RAMP;
    static int K1 = constants.K1, K2 = constants.K2;
    static Vector LIGHT = constants.LIGHT;

    static double[][] zbuffer = new double[HEIGHT][WIDTH];
    static char[][] screen = new char[HEIGHT][WIDTH];

    static double rx = 0, rz = 0, ry = 0;

    static void renderFace(Vector point, Vector normal) {
        point = rotation.rotateX(point, rx);
        point = rotation.rotateY(point, ry);
        point = rotation.rotateZ(point, rz);

        normal = rotation.rotateX(normal, rx);
        normal = rotation.rotateY(normal, ry);
        normal = rotation.rotateZ(normal, rz);

        normal = normal.normalize();

        double brightness = Math.max(0, normal.dot(LIGHT));
    
        double index = brightness * (RAMP.length - 1);
        index = Math.max(0, Math.min(index, RAMP.length-1));
    
        char asciiChar = RAMP[(int)index];

        double depth = Math.max(point.z + K2, 1);
        double invZ = 1 / depth;
    
        int screenX = (int)(WIDTH/2  + K1 * point.x * invZ);
        int screenY = (int)(HEIGHT/2 - 0.5 * K1 * point.y * invZ);
    
        if(screenX >= 0 && screenX < WIDTH) {
            if(screenY >= 0 && screenY < HEIGHT) {
    
                if (invZ > zbuffer[screenY][screenX]) {
                    zbuffer[screenY][screenX] = invZ;
                    screen[screenY][screenX] = asciiChar;
                }
            }
        }
    }

    public void renderCube() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.print("\033[?25h");                               // show cursor
            System.out.print("\033[" + (constants.HEIGHT + 1) + ";1H");  // move cursor below frame
            System.out.print("\033[0m");                                 // reset terminal formatting
        }));

        Cube cube = new Cube(45, 45, 45);

        Layout layout = new Layout(WIDTH * 2, HEIGHT, WIDTH);
        Renderer renderer = new Renderer(WIDTH, HEIGHT);
        
        List<String> INFO = new SystemInfo().getInfo();

        engine.clearScreen();
        System.out.print("\033[?25l");

        while(true) {
            for (int i = 0; i < HEIGHT; i++) {
                Arrays.fill(zbuffer[i], 0);
                Arrays.fill(screen[i], ' ');
            }

            rx = (rx + 3) % 360;
            rz = (rz + 1.5) % 360;
            ry = (ry + 6) % 360;

            double s = cube.height / 2;
            double step = 0.3;

            for(double x = -s; x <= s; x += step) {
                for(double y = -s; y <= s; y += step) {
                    renderFace(new Vector(x,y,s),  new Vector(0,0,1));  // front
                    renderFace(new Vector(x,y,-s), new Vector(0,0,-1)); // back
                }
            }
            
            s = cube.breadth / 2;

            for(double x = -s; x <= s; x += step) {
                for(double z = -s; z <= s; z += step) {
                    renderFace(new Vector(x,s,z),  new Vector(0,1,0));  // top
                    renderFace(new Vector(x,-s,z), new Vector(0,-1,0)); // bottom
                }
            }

            s = cube.length / 2;

            for(double y = -s; y <= s; y += step) {
                for(double z = -s; z <= s; z += step) {
                    renderFace(new Vector(s,y,z),  new Vector(1,0,0));  // right
                    renderFace(new Vector(-s,y,z), new Vector(-1,0,0)); // left
                }
            }
            
            try {
                char[][] frame = layout.compose(screen, INFO);

                renderer.draw(frame);

                Thread.sleep(30);
            } catch (Exception e) {
                System.out.println("Error in rendering the object: " + e);
            }
        }
    }
}