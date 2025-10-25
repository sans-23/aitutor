package lld.problems.BookMyShow.sanskar;

import java.util.UUID;

public class Movie {
    String id;
    String name;
    String description;
    String genre;
    String language;

    Movie(String name, String description, String genre, String language){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.genre = genre;
        this.language = language;
    }
}
