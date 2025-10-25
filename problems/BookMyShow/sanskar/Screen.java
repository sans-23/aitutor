package lld.problems.BookMyShow.sanskar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Screen {
    String id;
    List<Show> shows;
    List<Seat> seatingArrangement;

    Screen(List<Seat> seatingArrangement){
        this.id = UUID.randomUUID().toString();
        this.seatingArrangement = seatingArrangement;
        this.shows = new ArrayList<>();
    }

    public void addShow(Movie movie, LocalDate date){
        Show show = new Show(movie, seatingArrangement, date, new DefaultPricingStrategy());
        shows.add(show);
    }
}
