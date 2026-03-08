package cli;

public class Layout {
    int width;
    int height;
    int torusWidth;

    Layout(int width, int height, int torusWidth) {
        this.width = width;
        this.height = height;
        this.torusWidth = torusWidth;
    }

    String compose(char[][] torusFrame, List<String> infoLines) {

        StringBuilder frame = new StringBuilder();

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < torusWidth; x++) {
                frame.append(torusFrame[y][x]);
            }

            frame.append("   ");

            if (y < infoLines.size()) {
                frame.append(infoLines.get(y));
            }

            frame.append('\n');
        }

        return frame.toString();
    }
}