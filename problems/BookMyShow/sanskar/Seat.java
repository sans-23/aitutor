package lld.problems.BookMyShow.sanskar;

public abstract class Seat {
    int price;
    String id; // Changed to String for unique identification
    SeatStatus status;

    public Seat(String id, int price, SeatStatus status) {
        this.id = id;
        this.price = price;
        this.status = status;
    }
}
