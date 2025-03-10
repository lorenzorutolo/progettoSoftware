package ch.supsi.filmstats.backend;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TestMain {
    public static void main(String[] args) {
        List<Film> films = new ArrayList<>();
        String userHomeDirectory, destinationDirectory, fileDirectory = "";
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


        try (FileInputStream inputStream = new FileInputStream("C:\\Users\\geros\\Desktop\\SUPSI\\I2B\\ing_software\\progettoSoftware\\backend\\src\\main\\resources\\configuration.properties")) {
            properties.load(inputStream);

            userHomeDirectory = properties.getProperty("userhomedir");
            destinationDirectory = properties.getProperty("destinationdir");
            fileDirectory = userHomeDirectory + File.separator + properties.getProperty("filename");

            System.out.println(fileDirectory);
        } catch (IOException e) {
            System.out.println("file non trovato");
        }


        //Reader
        try (CSVReader reader = new CSVReader(new FileReader(fileDirectory))) {
            List<String[]> records = reader.readAll();
            records.remove(0);

            for(String[] record : records) {
                String link = record[0];
                String title = record[1];
                int releaseYear = Integer.parseInt(record[2]);
                String certificate = record[3];
                int runTime = Integer.parseInt(record[4].replace(" min", ""));
                List<String> genre = record[5].lines().toList();
                double rating = Double.parseDouble(record[6]);
                String overView = record[7];

                int metaScore = 0;
                if (!Objects.equals(record[8], "")){
                    metaScore = Integer.parseInt(record[8]);
                }

                Director director = new Director(record[9]);
                List<Actor> actors = new ArrayList<>();
                for (int i = 10; i < 14; i++) {
                    Actor actor = new Actor(record[i]);
                    actors.add(actor);
                }

                int numberVotes = Integer.parseInt(record[14]);

                int gross = 0;
                if (!Objects.equals(record[15], "")){
                    gross = Integer.parseInt(record[15].replace(",", ""));
                }

                films.add(new Film(link, title, releaseYear, certificate, runTime, genre, rating, overView, metaScore, director, actors, numberVotes, gross));
            }

            //TODO: nr. of movies
            long nrMovies = films.stream().count();
            System.out.println("Number of Movies: " + nrMovies);

            //TODO: avg. movies runtime
            double avgRuntime = films.stream().mapToDouble(Film::getRunTime).average().orElse(0.0);
            System.out.println("Average of Runtime Movies: " + avgRuntime);

            //TODO: best director
            Map<String, Double> bestDirectorMap = films.stream().collect(Collectors.groupingBy(film -> film.getDirector().getName(), Collectors.averagingDouble(Film::getRating)));
            Map.Entry<String, Double> bestDirector = bestDirectorMap.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new NoSuchElementException("No directors found"));
            System.out.println("Best Director: " + bestDirector.getKey() + " with " + bestDirector.getValue() + " of rating");

            //TODO: most present actor
            Map<String, Long> mostPresentActorMap = films.stream().flatMap(a -> a.getActors().stream()).collect(Collectors.groupingBy(Actor::getName,Collectors.counting()));
            Map.Entry<String, Long> mostPresentActor = mostPresentActorMap.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new NoSuchElementException("No actors found"));
            System.out.println("Most present actor: " + mostPresentActor.getKey() + " with " + mostPresentActor.getValue() + " appearances");

            //TODO: most productive year
            Map<Integer, Long> mostProductiveYear = films.stream().collect(Collectors.groupingBy(Film::getReleaseYear, Collectors.counting()));
            Map.Entry<Integer, Long> mostProductive = mostProductiveYear.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new NoSuchElementException("No productive year found"));
            System.out.println("Most productive year: " + mostProductive.getKey() + " with " + mostProductive.getValue() + " productions");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }
}