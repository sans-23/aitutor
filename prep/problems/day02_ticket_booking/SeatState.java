package prep.problems.day02_ticket_booking;

public interface SeatState {
    void holdSeat(Seat seat, User user, long ttl);
    void bookSeat(Seat seat, User user);
    void release(Seat seat);
}