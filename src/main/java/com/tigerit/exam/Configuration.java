package com.tigerit.exam;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Faisal Ahmed
 */
public class Configuration {

    private static String type;

    private static final Properties properties = new Properties();

    private Configuration() {
        try {
            InputStream input = Configuration.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(input);
            type = "classpath";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Configuration(String path) {
        Objects.requireNonNull(path, "External file path is empty");
        try {
            FileInputStream input = new FileInputStream(path);
            properties.load(input);
            type = "external";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Configuration load(String... args) {
        if(args.length == 0) return new Configuration();
        return new Configuration(args[0]);
    }

    public String get(String key) {
        Objects.requireNonNull(key, "Property key can't be null");
        return properties.getProperty(key.trim());
    }

    public String type() {
        return type;
    }

}
