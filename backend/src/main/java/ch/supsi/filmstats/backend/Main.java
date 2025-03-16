package ch.supsi.filmstats.backend;

import ch.supsi.filmstats.backend.config.PropertiesConfig;
import ch.supsi.filmstats.backend.repository.FilmRepository;
import ch.supsi.filmstats.backend.service.FilmService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PropertiesConfig propConf = new PropertiesConfig();
        FilmService filmService = new FilmService();
        propConf.loadProperties();
        filmService.writeStats();
    }
}
