package cli;

import java.util.*;

public class Layout {

    int width;
    int height;
    int objWidth;
    int infoWidth;

    public Layout(int width, int height, int objWidth) {
        this.width = width;
        this.height = height;
        this.objWidth = objWidth;
        this.infoWidth = width - objWidth - 3;
    }

    public char[][] compose(char[][] objFrame, List<String> infoLines) {

    char[][] frame = new char[height][width];

    int offset = (height - infoLines.size()) / 2;

    for (int y = 0; y < height; y++) {

        int x = 0;

        // object area
        if (y < objFrame.length) {
            int objRowLen = objFrame[y].length;

            for (; x < objWidth; x++) {
                frame[y][x] = (x < objRowLen) ? objFrame[y][x] : ' ';
            }
        } else {
            for (; x < objWidth; x++) {
                frame[y][x] = ' ';
            }
        }

        for (int i = 0; i < 3; i++) {
            frame[y][x++] = ' ';
        }

        // info section
        if (y >= offset && y < offset + infoLines.size()) {

            String line = infoLines.get(y - offset);
            int limit = Math.min(line.length(), infoWidth);

            for (int i = 0; i < limit && x < width; i++) {
                frame[y][x++] = line.charAt(i);
            }
        }

        // pad remaining width
        while (x < width) {
            frame[y][x++] = ' ';
        }
    }

    return frame;
}
}
