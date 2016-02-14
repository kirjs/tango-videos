package com.tangovideos.models;

import java.util.ArrayList;

public class Song {
    String year;
    ArrayList<Performer> performer;
    private String name;
    private String orquestra;
    private String genre;

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrquestra(String orquestra) {
        this.orquestra = orquestra;
    }

    public String getOrquestra() {
        return orquestra;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }
}
