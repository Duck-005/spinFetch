package cli;

import commands.SpinCube;
import commands.SpinTorus;
import commands.InfoCommand;

public class Main {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: spinfetch [torus|cube|info]");
            return;
        }

        String command = args[0];   

        switch(command) {

            case "torus":
                new SpinTorus().renderTorus();
                break;

            case "cube":
                new SpinCube().renderCube();
                break;

            case "info":
                new InfoCommand().printSystemInfo();
                break;

            default:
                System.out.println("Unknown command");
        }
    }
}