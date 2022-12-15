
/**
 * Utility to load config values
 * 
 * @author jweled
 * @version 1.0.0
 */
import java.util.Properties;
import java.io.FileInputStream;
public class ConfigLoader {
    public static Properties load(String path) {
        Properties config = new Properties();
        try {
            config.load(new FileInputStream(path));
        } catch (Exception e) {}
        return config;
    }
    public static String get(Properties p, String key) {
        return p.getProperty(key);
    }
}
