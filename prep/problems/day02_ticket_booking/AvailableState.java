package prep.problems.day02_ticket_booking;

public class AvailableState implements SeatState {

    @Override
    public void holdSeat(Seat seat, User user, long ttl) {
        seat.setSeatState(new HoldState(ttl, user));
    }

    @Override
    public void bookSeat(Seat seat, User user) {
        throw new IllegalStateException("Seat is not held by any user");
    }

    @Override
    public void release(Seat seat) {
        // No-op: Seat is already available
        return;
    }
}
