package chain_source;
import java.util.Locale;

/* Author Tudor Finaru
/* 
 * This class is responsible for checking (and caching in a static variable) whether the operating system is Windows.
 */

public class OS_Util {
    private static String OS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

    public static boolean isWindows() {
    	return OS.startsWith("windows");
    }
}