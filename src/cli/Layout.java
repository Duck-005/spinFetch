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

    public String compose(char[][] objFrame, List<String> infoLines) {
        StringBuilder frame = new StringBuilder();

        int offset = (height - infoLines.size()) / 2;

        for (int y = 0; y < height; y++) {

            int rowStart = frame.length();

            if (y < objFrame.length) {
                int objRowLen = objFrame[y].length;
                for (int x = 0; x < objWidth; x++) {
                    frame.append(x < objRowLen ? objFrame[y][x] : ' ');
                }
            } else {
                frame.append(" ".repeat(objWidth));
            }

            frame.append("   ");

            if (y >= offset && y < offset + infoLines.size()) {
                String line = infoLines.get(y - offset);
                frame.append(line, 0, Math.min(line.length(), infoWidth));
            }

            // pad remaining width
            int rowLen = frame.length() - rowStart;
            while (rowLen < width) {
                frame.append(' ');
                rowLen++;
            }

            frame.append('\n');
        }

        return frame.toString();
    }
}