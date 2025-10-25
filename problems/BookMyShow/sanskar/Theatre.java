package lld.problems.BookMyShow.sanskar;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Theatre {
    String id;
    String name;
    String location;
    List<Screen> screens;

    Theatre(String name, String location){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.location = location;
        this.screens = new ArrayList<>();
    }

    Theatre(String id, String name, String location, List<Screen> screens){
        this.id = id;
        this.name = name;
        this.location = location;
        this.screens = screens;
    }

    public void addScreen(Screen s){
        screens.add(s);
    }

    public void removeScreen(Screen s){
        screens.remove(s);
    }
}
