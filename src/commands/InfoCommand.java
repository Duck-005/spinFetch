package commands;

import cli.SystemInfo;

import java.util.*;

public class InfoCommand {
    public void printSystemInfo() {
        SystemInfo infoObj = new SystemInfo();

        List<String> info = infoObj.getInfo();
        for(String item: info) {
            System.out.println(item);
        }
    }
}