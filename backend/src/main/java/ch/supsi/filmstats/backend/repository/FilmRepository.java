package ch.supsi.filmstats.backend.repository;

import ch.supsi.filmstats.backend.config.PropertiesConfig;
import ch.supsi.filmstats.backend.model.ActorModel;
import ch.supsi.filmstats.backend.model.DirectorModel;
import ch.supsi.filmstats.backend.model.FilmModel;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FilmRepository {
    private PropertiesConfig prop = new PropertiesConfig();
    String userHomeDirectory;
    String destinationDirectory;
    String fileDirectory;
    String pathDestination;


    public List<FilmModel> loadFilms() {
        List<FilmModel> films = new ArrayList<>();
        String pathStart = prop.getProjectRoot() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "configuration.properties";
        try (FileInputStream inputStream = new FileInputStream(pathStart)) {
            prop.getProperties().load(inputStream);

            userHomeDirectory = prop.getProperties().getProperty("userhomedir");
            destinationDirectory = prop.getProperties().getProperty("destinationdir");
            fileDirectory = userHomeDirectory + File.separator + prop.getProperties().getProperty("filename");
        } catch (IOException e) {
            System.out.println("File non trovato");
        }

        try (CSVReader reader = new CSVReader(new FileReader(fileDirectory))) {
            List<String[]> records = reader.readAll();
            records.remove(0);

            for (String[] record : records) {
                String link = record[0];
                String title = record[1];
                int releaseYear = Integer.parseInt(record[2]);
                String certificate = record[3];
                int runTime = Integer.parseInt(record[4].replace(" min", ""));
                List<String> genre = record[5].lines().toList();
                double rating = Double.parseDouble(record[6]);
                String overView = record[7];

                int metaScore = 0;
                if (!Objects.equals(record[8], "")) {
                    metaScore = Integer.parseInt(record[8]);
                }

                DirectorModel directorModel = new DirectorModel(record[9]);
                List<ActorModel> actorModels = new ArrayList<>();
                for (int i = 10; i < 14; i++) {
                    ActorModel actorModel = new ActorModel(record[i]);
                    actorModels.add(actorModel);
                }

                int numberVotes = Integer.parseInt(record[14]);

                int gross = 0;
                if (!Objects.equals(record[15], "")) {
                    gross = Integer.parseInt(record[15].replace(",", ""));
                }

                films.add(new FilmModel(link, title, releaseYear, certificate, runTime, genre, rating, overView, metaScore, directorModel, actorModels, numberVotes, gross));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

        return films;
    }

    public void writeFilms(String[][] matrix) {
        pathDestination = destinationDirectory + File.separator + "stats_imdb.csv";
        try (CSVWriter writer = new CSVWriter(new FileWriter(pathDestination),
                ';', //usiamo un costruttore avanzato quindi il ";" come separatore (non piu la virgola come nel costruttore di default)
                CSVWriter.NO_QUOTE_CHARACTER, //disattiva i doppi apici intorno ai valori
                CSVWriter.DEFAULT_ESCAPE_CHARACTER, //mantiene il carattere di escape predefinito (necessario da specificare se usiamo questo costruttore)
                CSVWriter.DEFAULT_LINE_END)) { //Usa il carattere di nuova linea corretto a seconda del sistema operativo
            writer.writeNext(matrix[0]);
            writer.writeNext(matrix[1]);

            System.out.println("CSV file creato con successo usando OpenCSV!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
