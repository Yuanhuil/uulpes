package heracles.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

public class SystemInfo {
    private static final String VERSION_KEY = "appliction.version";
    private static final String BUILD_DATE_KEY = "appliction.build.date";
    private static final String BUILD_REVISION_KEY = "appliction.build.revision";

    private static final long MEGABYTE = 1048576L;

    private static SystemInfo instance = new SystemInfo();

    private Configuration configuration;

    private SystemInfo() {

    }

    static public SystemInfo getInstance() {
        return instance;
    }

    static public SystemInfo getInstance(Configuration configuration) {
        instance.setConfiguration(configuration);
        return instance;
    }

    public static Map<String, String> getSystemProperties() {
        Map<String, String> props = new LinkedHashMap<String, String>();
        props.put("System Date", SimpleDateFormat.format(new Date()));
        props.put("System Time", SimpleDateFormat.format(new Date()));
        props.put("Current directory", getCurrentDirectory());
        props.put("Java Version", SystemUtils.JAVA_VERSION);
        props.put("Java Vendor", SystemUtils.JAVA_VENDOR);
        props.put("JVM Sepcification Version", SystemUtils.JAVA_VM_SPECIFICATION_VERSION);
        props.put("JVM Vendor", SystemUtils.JAVA_VM_VENDOR);
        props.put("JVM Implementation Version", SystemUtils.JAVA_VERSION_TRIMMED);
        props.put("Java Runtime", SystemUtils.JAVA_RUNTIME_NAME);
        props.put("Java VM", SystemUtils.JAVA_VM_NAME);
        props.put("User Name", SystemUtils.USER_NAME);
        props.put("User Timezone", SystemUtils.USER_TIMEZONE);
        props.put("Operating System", SystemUtils.OS_NAME + " " + SystemUtils.OS_VERSION);
        props.put("OS Architecture", SystemUtils.OS_ARCH);

        return props;
    }

    private static String getCurrentDirectory() {
        try {
            return new File(".").getCanonicalPath();
        } catch (IOException e) {
            return "<undefined>";
        }
    }

    public static Map<String, String> getJVMStatistics() {
        Map<String, String> stats = new LinkedHashMap<String, String>();
        stats.put("Max Memory", getMaxMemory() + "MB");
        stats.put("Total Memory", getTotalMemory() + "MB");
        stats.put("Free Memory", getFreeMemory() + "MB");
        stats.put("Used Memory", getUsedMemory() + "MB");
        return stats;
    }

    public Map<String, String> getBuildInfo() {
        Map<String, String> stats = new LinkedHashMap<String, String>();
        stats.put("Version", configuration.getString(VERSION_KEY));
        stats.put("Build Date", configuration.getString(BUILD_DATE_KEY));
        stats.put("Build Revision", configuration.getString(BUILD_REVISION_KEY));
        return stats;
    }

    static public String JVMStatistics() {
        return propertiesMapToString("VMStatistics", getJVMStatistics());
    }

    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory() / MEGABYTE;
    }

    public static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory() / MEGABYTE;
    }

    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory() / MEGABYTE;
    }

    public static long getUsedMemory() {
        return getTotalMemory() - getFreeMemory();
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(propertiesMapToString("Build", getBuildInfo()));
        buf.append(propertiesMapToString("System", getSystemProperties()));
        buf.append(propertiesMapToString("VMStatistics", getJVMStatistics()));
        return buf.toString();
    }

    private static String propertiesMapToString(String mapName, Map<?, ?> properties) {
        StringBuilder buf = new StringBuilder();
        buf.append(mapName);
        buf.append(":").append(SystemUtils.LINE_SEPARATOR);

        Iterator<?> iterator = properties.keySet().iterator();
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            String value = (String) properties.get(name);
            buf.append("   ");
            buf.append(StringUtils.rightPad(name + ":", 30));
            buf.append(value).append(SystemUtils.LINE_SEPARATOR);
        }
        buf.append("\n");
        return buf.toString();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

}
