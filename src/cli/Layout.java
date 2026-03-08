package cli;

import java.util.*;

public class Layout {

    int width;
    int height;
    int torusWidth;
    int infoWidth;

    public Layout(int width, int height, int torusWidth) {
        this.width = width;
        this.height = height;
        this.torusWidth = torusWidth;
        this.infoWidth = width - torusWidth - 3;
    }

    public String compose(char[][] torusFrame, List<String> infoLines) {
        StringBuilder frame = new StringBuilder();

        int offset = (height - infoLines.size()) / 2;

        for (int y = 0; y < height; y++) {

            if (y < torusFrame.length) {
                int torusRowLen = torusFrame[y].length;
                for (int x = 0; x < torusWidth; x++) {
                    frame.append(x < torusRowLen ? torusFrame[y][x] : ' ');
                }
            } else {
                frame.append(" ".repeat(torusWidth));
            }

            frame.append("   ");

            if (y >= offset && y < offset + infoLines.size()) {
                String line = infoLines.get(y - offset);
                frame.append(line, 0, Math.min(line.length(), infoWidth));
            }

            frame.append('\n');
        }

        return frame.toString();
    }
}