package engine;

public class Engine {
    public void printScreen(char[][] screen) {

        StringBuilder frame = new StringBuilder();
    
        for(int i = 0; i < screen.length; i++) {
            for(int j = 0; j < screen[0].length; j++) {
                frame.append(screen[i][j]);
            }
            frame.append('\n');
        }
    
        System.out.print(frame.toString());
    }

    public void clearScreen() {
        System.out.print("\033[2J");
        System.out.print("\033[H");
    }
    
    public void resetCursor() {
        System.out.print("\033[H");
    }
}
