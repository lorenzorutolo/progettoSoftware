package ch.supsi.filmstats.backend.model;

public class DirectorModel {
    private String name;

    public DirectorModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
