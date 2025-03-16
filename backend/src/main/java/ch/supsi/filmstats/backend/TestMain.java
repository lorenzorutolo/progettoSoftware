package ch.supsi.filmstats.backend;
import ch.supsi.filmstats.backend.model.ActorModel;
import ch.supsi.filmstats.backend.model.DirectorModel;
import ch.supsi.filmstats.backend.model.FilmModel;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TestMain {
    public static void main(String[] args) {
        List<FilmModel> filmModels = new ArrayList<>();
        String userHomeDirectory = "", destinationDirectory = "", fileDirectory = "";
        String projectRoot = System.getProperty("user.dir");
        String propertiesFilePath = projectRoot + "/src/main/resources/configuration.properties";
        File propertiesFile = new File(propertiesFilePath);
        Properties properties = new Properties();

        if (!propertiesFile.exists()) {
            try (FileOutputStream out = new FileOutputStream(propertiesFile)) {
                properties.setProperty("userhomedir", System.getProperty("user.home"));
                properties.setProperty("destinationdir", System.getProperty("user.home") + "/Desktop");
                properties.setProperty("filename", "imdb_top_1000.csv");

                properties.store(out, "Directory di sistema");
                System.out.println("File 'configuration.properties' creato con successo!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Il file 'configuration.properties' esiste già.");
        }

        String pathStart = projectRoot + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "configuration.properties";
        try (FileInputStream inputStream = new FileInputStream(pathStart)) {
            properties.load(inputStream);

            userHomeDirectory = properties.getProperty("userhomedir");
            destinationDirectory = properties.getProperty("destinationdir");
            fileDirectory = userHomeDirectory + File.separator + properties.getProperty("filename");
        } catch (IOException e) {
            System.out.println("File non trovato");
        }


        //Reader
        String[][] matrix = {
                {"Number of Movies", "Average of Runtime Movies", "Best DirectorModel", "Most present actor", "Most productive year"},
                {"", "", "", "", ""}
        };;
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

                filmModels.add(new FilmModel(link, title, releaseYear, certificate, runTime, genre, rating, overView, metaScore, directorModel, actorModels, numberVotes, gross));
            }

            //TODO: nr. of movies
            long nrMovies = filmModels.stream().count();
            System.out.println("Number of Movies: " + nrMovies);
            matrix[1][0] = String.valueOf(nrMovies);

            //TODO: avg. movies runtime
            double avgRuntime = filmModels.stream().mapToDouble(FilmModel::getRunTime).average().orElse(0.0);
            System.out.println("Average of Runtime Movies: " + avgRuntime);
            matrix[1][1] = String.valueOf(avgRuntime);


            //TODO: best director
            Map<String, Double> bestDirectorMap = filmModels.stream().collect(Collectors.groupingBy(filmModel -> filmModel.getDirector().getName(), Collectors.averagingDouble(FilmModel::getRating)));
            Map.Entry<String, Double> bestDirector = bestDirectorMap.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new NoSuchElementException("No directors found"));
            System.out.println("Best DirectorModel: " + bestDirector.getKey() + " with " + bestDirector.getValue() + " of rating");
            matrix[1][2] = String.valueOf(bestDirector.getKey());

            //TODO: most present actor
            Map<String, Long> mostPresentActorMap = filmModels.stream().flatMap(a -> a.getActors().stream()).collect(Collectors.groupingBy(ActorModel::getName, Collectors.counting()));
            Map.Entry<String, Long> mostPresentActor = mostPresentActorMap.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new NoSuchElementException("No actors found"));
            System.out.println("Most present actor: " + mostPresentActor.getKey() + " with " + mostPresentActor.getValue() + " appearances");
            matrix[1][3] = String.valueOf(mostPresentActor.getKey());

            //TODO: most productive year
            Map<Integer, Long> mostProductiveYear = filmModels.stream().collect(Collectors.groupingBy(FilmModel::getReleaseYear, Collectors.counting()));
            Map.Entry<Integer, Long> mostProductive = mostProductiveYear.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new NoSuchElementException("No productive year found"));
            System.out.println("Most productive year: " + mostProductive.getKey() + " with " + mostProductive.getValue() + " productions");
            matrix[1][4] = String.valueOf(mostProductive.getKey());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

        String pathDestination = destinationDirectory + File.separator + "stats_imdb.csv";

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