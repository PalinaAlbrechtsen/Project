package util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertiesUtil {

    private static final Properties PROPERTIES;

    static {
        PROPERTIES = new Properties();
        File propertiesFile = Paths.get("resource", "max_app.properties").toFile();
        try {
            PROPERTIES.load(new BufferedReader(new FileReader(propertiesFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key){
        return PROPERTIES.getProperty(key);
    }
}
