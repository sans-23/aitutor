package prep.problems.day02_ticket_booking;

public class Seat {
    private String seatId;
    private SeatState seatState;

    public Seat(String seatId) {
        this.seatId = seatId;
        this.seatState = new AvailableState();
    }

    public synchronized void setSeatState(SeatState seatState) {
        this.seatState = seatState;
    }

    public synchronized void holdSeat(User user, long ttl) {
        seatState.holdSeat(this, user, ttl);
    }

    public synchronized void bookSeat(User user) {
        seatState.bookSeat(this, user);
    }

    public synchronized void release(User user) {
        seatState.release(this);
    }

    public String getSeatId() {
        return seatId;
    }
}
