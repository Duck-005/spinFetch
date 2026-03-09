package engine;

import java.util.Arrays;

public class Renderer {

    char[][] prev;
    int width;
    int height;

    public Renderer(int w, int h) {
        width = w;
        height = h;

        prev = new char[h][w];

        for (int y = 0; y < h; y++) {
            Arrays.fill(prev[y], '\0');
        }
    }

    public void draw(char[][] curr) {

        for (int y = 0; y < height; y++) {
        
            int x = 0;
            while (x < width) {

                if (prev[y][x] != curr[y][x]) {

                    System.out.print("\033[" + (y+1) + ";" + (x+1) + "H");

                    while (x < width && prev[y][x] != curr[y][x]) {
                        char c = curr[y][x];
                        System.out.print(c);
                        prev[y][x] = c;
                        x++;
                    }   
                } else {
                    x++;
                }   
            }
        }
        System.out.flush();
    }   
}