package lld.problems.BookMyShow.sanskar;

public class NormalSeat extends Seat {
    public NormalSeat(String id, int price) {
        super(id, price, SeatStatus.AVAILABLE);
    }
}
