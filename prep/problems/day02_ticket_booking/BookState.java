package prep.problems.day02_ticket_booking;

public class BookState implements SeatState {

    @Override
    public void holdSeat(Seat seat, User user, long ttl) {
        throw new IllegalStateException("Seat is already held");
    }

    @Override
    public void bookSeat(Seat seat, User user) {
        throw new IllegalStateException("Seat is already booked");
    }

    @Override
    public void release(Seat seat) {
        seat.setSeatState(new AvailableState());
    }
}
