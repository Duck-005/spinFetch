package cli;

import java.net.InetAddress;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class SystemInfo {
    static String getOS() {
        return "OS: " + System.getProperty("os.name").toLowerCase();
    }

    static String getUser() {
        return "User: " + System.getProperty("user.name") + "@" +  InetAddress.getLocalHost().getHostName();
    }

    static String getKernel(String os) {

        if(os.contains("win")) {
            "Kernel: " + "NT " + System.getProperty("os.version");
        } else if(os.contains("mac")) {
            "Kernel: " + "Darwin " + System.getProperty("os.version");
        } else {
            "Kernel: " + System.getProperty("os.version");
        }
    }

    static String getUptime() {
        OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        long seconds = os.getSystemUptime();

        long days = seconds / 86400;
        seconds %= 86400;

        long hours = seconds / 3600;
        seconds %= 3600;

        long minutes = seconds / 60;

        return days + "d " + hours + "h " + minutes + "m";
    }

    static String getCPU(String os) {
        try {
            String cpu;
            if(os.contains("win")) {
                Process p = Runtime.getRuntime().exec("wmic cpu get name");

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
                String line;
                while ((line = reader.readLine()) != null) {

                    line = line.trim();

                    if (!line.isEmpty() && !line.equalsIgnoreCase("Name")) {
                        return line;
                    }
                }   

            } else if(os.contains("mac")) {
                Process p = Runtime.getRuntime().exec(
                    "sysctl -n machdep.cpu.brand_string"
                );

                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                cpu = r.readLine();

            } else {
                List<String> lines = Files.readAllLines(Path.of("/proc/cpuinfo"));

                for (String line : lines) {
                    if (line.startsWith("model name")) {
                        cpu = line.split(":")[1].trim();
                    }
                }
            }

            int cores = Runtime.getRuntime().availableProcessors();

            return "CPU: " + cpu + " (" + cores + ")";

        } catch (Exception e) {
            return "Unknown CPU";
        }
        return "Unknown CPU";
    }

    static String getRAMUsage() {
        OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        long totalRAM = os.getTotalPhysicalMemorySize();
        long freeRAM = os.getFreePhysicalMemorySize();
        long usedRAM = totalRAM - freeRAM;

        long totalGB = totalRAM / (1024 * 1024 * 1024);
        long usedGB = usedRAM / (1024 * 1024 * 1024);

        return "Memory: " + usedRAM + "GB / " + totalRAM + "GB";
    }

    static int getCPUUsage() {
        OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        return (int)(os.getCpuLoad() * 100);
    }

    List<String> getInfo() {
        List<String> info = new ArrayList<>();

        String os = System.getProperty("os.name").toLowerCase();

        info.add(getUser());
        info.add(getOS());
        info.add(getKernel());
        info.add(getUptime());
        info.add(getCPU());
        info.add(getRAMUsage());
        info.add(getCPUUsage());

        return info;
    }
}
