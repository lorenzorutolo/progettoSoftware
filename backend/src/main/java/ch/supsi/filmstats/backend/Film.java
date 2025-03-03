package ch.supsi.filmstats.backend;

import java.util.List;

public class Film {
    private String link;
    private String title;
    private int releaseYear;
    private String certificate;
    private int runTime;
    private List<String> genre;
    private double rating;
    private String overView;
    private int metaScore;
    private Director director;
    private List<Actor> actors;
    private int numberVotes;
    private int gross;

    public Film(String link, String title, int releaseYear, String certificate, int runTime, List<String> genre, double rating, String overView, int metaScore, Director director, List<Actor> actors, int numberVotes, int gross) {
        this.link = link;
        this.title = title;
        this.releaseYear = releaseYear;
        this.certificate = certificate;
        this.runTime = runTime;
        this.genre = genre;
        this.rating = rating;
        this.overView = overView;
        this.metaScore = metaScore;
        this.director = director;
        this.actors = actors;
        this.numberVotes = numberVotes;
        this.gross = gross;
    }

    public int getRunTime() {
        return runTime;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public double getRating() {
        return rating;
    }

    public Director getDirector() {
        return director;
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
                "Director: " + director + "\n" +
                "Actors: " + actors + "\n" +
                "Number of Votes: " + numberVotes + "\n" +
                "Gross: " + gross + "\n";
    }
}
