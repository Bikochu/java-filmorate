package ru.yandex.practicum.filmorate.model;

public class Genre {
    int id;
    String name;

    public Genre(int id, String genre) {
        this.id = id;
        this.name = genre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
