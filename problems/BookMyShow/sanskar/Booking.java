package lld.problems.BookMyShow.sanskar;

import java.time.LocalDateTime;
import java.util.List;

public class Booking {
    String bookingId;
    User user;
    Show show;
    List<Seat> bookedSeats;
    LocalDateTime bookingTime;
    BookingStatus status;
    double totalPrice;

    public Booking(String bookingId, User user, Show show, List<Seat> bookedSeats, LocalDateTime bookingTime, BookingStatus status, double totalPrice) {
        this.bookingId = bookingId;
        this.user = user;
        this.show = show;
        this.bookedSeats = bookedSeats;
        this.bookingTime = bookingTime;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public String getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getBookedSeats() {
        return bookedSeats;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
