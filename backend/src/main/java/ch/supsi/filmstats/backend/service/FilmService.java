package ch.supsi.filmstats.backend.service;

import ch.supsi.filmstats.backend.model.ActorModel;
import ch.supsi.filmstats.backend.model.FilmModel;
import ch.supsi.filmstats.backend.repository.FilmRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class FilmService {
    private final FilmRepository filmRepository;
    private String[][] matrix;

    private List<FilmModel> filmModels;

    public FilmService() {
        filmRepository = new FilmRepository();
        filmModels = filmRepository.loadFilms();
        matrix = new String[][]{
            {"Number of Movies", "Average of Runtime Movies", "Best DirectorModel", "Most present actor", "Most productive year"},
            {"", "", "", "", ""}
        };
    }

    public void nrMovies(){
        long nrMovies = filmModels.stream().count();
        System.out.println("Number of Movies: " + nrMovies);
        matrix[1][0] = String.valueOf(nrMovies);
    }

    public void avgRuntime(){
        double avgRuntime = filmModels.stream().mapToDouble(FilmModel::getRunTime).average().orElse(0.0);
        System.out.println("Average of Runtime Movies: " + avgRuntime);
        matrix[1][1] = String.valueOf(avgRuntime);
    }



    public void bestDirectorMap(){
        Map<String, Double> bestDirectorMap = filmModels.stream().collect(Collectors.groupingBy(filmModel -> filmModel.getDirector().getName(), Collectors.averagingDouble(FilmModel::getRating)));
        Map.Entry<String, Double> bestDirector = bestDirectorMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new NoSuchElementException("No directors found"));
        System.out.println("Best DirectorModel: " + bestDirector.getKey() + " with " + bestDirector.getValue() + " of rating");
        matrix[1][2] = String.valueOf(bestDirector.getKey());
    }


    public void mostPresentActorMap(){
        Map<String, Long> mostPresentActorMap = filmModels.stream().flatMap(a -> a.getActors().stream()).collect(Collectors.groupingBy(ActorModel::getName, Collectors.counting()));
        Map.Entry<String, Long> mostPresentActor = mostPresentActorMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new NoSuchElementException("No actors found"));
        System.out.println("Most present actor: " + mostPresentActor.getKey() + " with " + mostPresentActor.getValue() + " appearances");
        matrix[1][3] = String.valueOf(mostPresentActor.getKey());
    }


    public void mostProductiveYear(){
        Map<Integer, Long> mostProductiveYear = filmModels.stream().collect(Collectors.groupingBy(FilmModel::getReleaseYear, Collectors.counting()));
        Map.Entry<Integer, Long> mostProductive = mostProductiveYear.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new NoSuchElementException("No productive year found"));
        System.out.println("Most productive year: " + mostProductive.getKey() + " with " + mostProductive.getValue() + " productions");
        matrix[1][4] = String.valueOf(mostProductive.getKey());
    }

    public void writeStats(){
        nrMovies();
        avgRuntime();
        bestDirectorMap();
        mostPresentActorMap();
        mostProductiveYear();
        filmRepository.writeFilms(matrix);
    }
}
