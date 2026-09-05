package prep.problems.day02_ticket_booking;

public class HoldState implements SeatState {
    long expiryTimeStamp;
    User heldBy;

    public HoldState(long ttl, User user) {
        this.expiryTimeStamp = System.currentTimeMillis() + ttl;
        this.heldBy = user;
    }

    @Override
    public void holdSeat(Seat seat, User user, long ttl) {
        if (isExpired()) {
            // Expired! Anyone can claim it:
            seat.setSeatState(new HoldState(ttl, user));
            return;
        }
        // Not expired yet! Reject:
        throw new IllegalStateException("Seat " + seat.getSeatId() + " is already held by " + this.heldBy.getUserId());
    }

    @Override
    public void bookSeat(Seat seat, User user) {
        if(user == null || !user.equals(heldBy)) {
            throw new IllegalStateException("Seat is not held by this user");
        }
        if (isExpired()) {
            seat.setSeatState(new AvailableState());
            throw new IllegalStateException("Seat " + seat.getSeatId() + " is expired");
        }else{
            seat.setSeatState(new BookState());
        }
    }

    @Override
    public void release(Seat seat) {
        seat.setSeatState(new AvailableState());
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTimeStamp;
    }
}
