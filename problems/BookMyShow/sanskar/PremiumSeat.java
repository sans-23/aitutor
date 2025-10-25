package lld.problems.BookMyShow.sanskar;

public class PremiumSeat extends Seat {
    public PremiumSeat(String id, int price) {
        super(id, price, SeatStatus.AVAILABLE);
    }
}
