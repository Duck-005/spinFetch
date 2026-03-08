package cli;

import java.util.*;
import java.lang.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.InetAddress;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class SystemInfo {
    static String getOS() {
        return "OS: " + System.getProperty("os.name").toLowerCase();
    }

    static String getUser() {
        try {
            return "User: " + System.getProperty("user.name") + "@" +  InetAddress.getLocalHost().getHostName();
        }  catch (Exception e) {
            return "Unknown User";
        }
    }

    static String getKernel(String os) {

        if(os.contains("win")) {
            return "Kernel: " + "NT " + System.getProperty("os.version");
        } else if(os.contains("mac")) {
            return "Kernel: " + "Darwin " + System.getProperty("os.version");
        } else {
            return "Kernel: " + System.getProperty("os.version");
        }
    }

    static String getUptime(String os) {
        if(os.contains("win")) {
            try {

                ProcessBuilder pb = new ProcessBuilder(
                    "powershell",
                    "-command",
                    "(Get-CimInstance Win32_OperatingSystem).LastBootUpTime"
                );

                pb.redirectErrorStream(true);

                Process p = pb.start();
                p.waitFor();

                String output = new String(p.getInputStream().readAllBytes());

                String boot = output.lines()
                    .map(String::trim)
                    .filter(l -> !l.isEmpty())
                    .findFirst()
                    .orElse(null);

                if (boot == null)
                    return "Unknown uptime";

                DateTimeFormatter fmt =
                    DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm:ss a", Locale.ENGLISH);

                LocalDateTime bootTime = LocalDateTime.parse(boot, fmt);

                long seconds = ChronoUnit.SECONDS.between(
                    bootTime,
                    LocalDateTime.now()
                );

                long days = seconds / 86400;
                seconds %= 86400;

                long hours = seconds / 3600;
                seconds %= 3600;

                long minutes = seconds / 60;

                return "Uptime: " + days + "d " + hours + "h " + minutes + "m";

                } catch (Exception e) {
                return "Unknown uptime: " + e.getMessage();
            }
        } else if(os.contains("mac")) {

        } else {
            try {

                String content = Files.readString(Path.of("/proc/uptime"));

                String uptimeSecondsStr = content.split(" ")[0];

                long seconds = (long) Double.parseDouble(uptimeSecondsStr);

                long days = seconds / 86400;
                seconds %= 86400;

                long hours = seconds / 3600;
                seconds %= 3600;

                long minutes = seconds / 60;

                return days + "d " + hours + "h " + minutes + "m";

            } catch (Exception e) {
                return "Unknown uptime";
            }
        } 
        return "Unknown uptime";
    }

    static String getCPU(String os) {
        try {
            String cpu = "Unknown CPU";
            if(os.contains("win")) {
                Process p = new ProcessBuilder("wmic", "cpu", "get", "name").start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            
                String line;
                while ((line = reader.readLine()) != null) {

                    line = line.trim();

                    if (!line.isEmpty() && !line.equalsIgnoreCase("Name")) {
                        cpu = line;
                        break;
                    }
                }   

            } else if(os.contains("mac")) {
                Process p = new ProcessBuilder(
                    "sysctl", "-n", "machdep.cpu.brand_string"
                ).start();

                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                cpu = r.readLine();

            } else {
                List<String> lines = Files.readAllLines(Path.of("/proc/cpuinfo"));

                for (String line : lines) {
                    if (line.startsWith("model name")) {
                        cpu = line.split(":")[1].trim();
                        break;
                    }
                }
            }

            int cores = Runtime.getRuntime().availableProcessors();

            return "CPU: " + cpu + " (" + cores + ")";

        } catch (Exception e) {
            return "Unknown CPU";
        }
    }

    static String getRAMUsage() {
        OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        long totalRAM = os.getTotalMemorySize();
        long freeRAM = os.getFreeMemorySize();
        long usedRAM = totalRAM - freeRAM;

        long totalGB = totalRAM / (1024 * 1024 * 1024);
        long usedGB = usedRAM / (1024 * 1024 * 1024);

        return "Memory: " + usedGB + "GB / " + totalGB + "GB";
    }

    static String getCPUUsage() {
        try {
            OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

            Thread.sleep(100);

            return "CPU Load: " + os.getCpuLoad() * 100 + "%";

        } catch (Exception e) {
            return "Unknown CPU load";
        }
    }

    public List<String> getInfo() {
        List<String> info = new ArrayList<>();

        String os = System.getProperty("os.name").toLowerCase();

        info.add(getUser());
        info.add(getOS());
        info.add(getKernel(os));
        info.add(getUptime(os));
        info.add(getCPU(os));
        info.add(getRAMUsage());
        info.add(getCPUUsage());

        return info;
    }
}
