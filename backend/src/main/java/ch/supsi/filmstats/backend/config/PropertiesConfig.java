package ch.supsi.filmstats.backend.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesConfig {
    private final Properties properties = new Properties();
    private final String propertiesFilePath;
    private final String projectRoot;

    public PropertiesConfig() {
        projectRoot = System.getProperty("user.dir");
        propertiesFilePath = projectRoot + "/src/main/resources/configuration.properties";
        loadProperties();
    }
    public void loadProperties() {
        File propertiesFile = new File(propertiesFilePath);

        if (!propertiesFile.exists()) {
            try (FileOutputStream out = new FileOutputStream(propertiesFile)) {
                properties.setProperty("userhomedir", System.getProperty("user.home"));
                properties.setProperty("destinationdir", System.getProperty("user.home") + "/Desktop");
                properties.setProperty("filename", "imdb_top_1000.csv");

                properties.store(out, "Directory di sistema");
                System.out.println("File 'configuration.properties' creato con successo!");
            } catch (IOException e) {
                System.out.println("C'é stato un'errore nella creazione del file 'configuration.properties'");
            }
        } else {
            System.out.println("Il file 'configuration.properties' esiste già.");
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public String getPropertiesFilePath() {
        return propertiesFilePath;
    }

    public String getProjectRoot() {
        return projectRoot;
    }
}
