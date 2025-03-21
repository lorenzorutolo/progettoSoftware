package ch.supsi.filmstats.backend.model;

import java.util.List;

public class FilmModel {
    private String link;
    private String title;
    private int releaseYear;
    private String certificate;
    private int runTime;
    private List<String> genre;
    private double rating;
    private String overView;
    private int metaScore;
    private DirectorModel directorModel;
    private List<ActorModel> actorModels;
    private int numberVotes;
    private int gross;

    public FilmModel(String link, String title, int releaseYear, String certificate, int runTime, List<String> genre, double rating, String overView, int metaScore, DirectorModel directorModel, List<ActorModel> actorModels, int numberVotes, int gross) {
        this.link = link;
        this.title = title;
        this.releaseYear = releaseYear;
        this.certificate = certificate;
        this.runTime = runTime;
        this.genre = genre;
        this.rating = rating;
        this.overView = overView;
        this.metaScore = metaScore;
        this.directorModel = directorModel;
        this.actorModels = actorModels;
        this.numberVotes = numberVotes;
        this.gross = gross;
    }

    public int getRunTime() {
        return runTime;
    }

    public List<ActorModel> getActors() {
        return actorModels;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public double getRating() {
        return rating;
    }

    public DirectorModel getDirector() {
        return directorModel;
    }

    @Override
    public String toString() {
        return "Link: " + link + "\n" +
                "Title: " + title + "\n" +
                "Release Year: " + releaseYear + "\n" +
                "Certificate: " + certificate + "\n" +
                "Run Time: " + runTime + "\n" +
                "Genre: " + genre + "\n" +
                "Rating: " + rating + "\n" +
                "Over View: " + overView + "\n" +
                "Meta Score: " + metaScore + "\n" +
                "DirectorModel: " + directorModel + "\n" +
                "Actors: " + actorModels + "\n" +
                "Number of Votes: " + numberVotes + "\n" +
                "Gross: " + gross + "\n";
    }
}
