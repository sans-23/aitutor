package lld.problems.BookMyShow.sanskar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Show {
    Map<String, Seat> seats; // Changed to Map for efficient seat management
    Movie movie;
    LocalDate date;
    PricingStrategy pricingStrategy;

    Show(Movie movie, List<Seat> initialSeats, LocalDate date, PricingStrategy pricingStrategy){
        this.movie = movie;
        this.date = date;
        this.seats = new HashMap<>();
        for (Seat seat : initialSeats) {
            this.seats.put(seat.id, seat);
        }
        this.pricingStrategy = pricingStrategy;
    }

    public List<Seat> getAvailableSeats() {
        List<Seat> availableSeats = new ArrayList<>();
        for (Seat seat : seats.values()) {
            if (seat.status == SeatStatus.AVAILABLE) {
                availableSeats.add(seat);
            }
        }
        return availableSeats;
    }

    public void blockSeats(List<String> seatIds) throws SeatUnavailableException {
        for (String seatId : seatIds) {
            Seat seat = seats.get(seatId);
            if (seat == null || seat.status != SeatStatus.AVAILABLE) {
                throw new SeatUnavailableException("Seat " + seatId + " is not available.");
            }
        }
        for (String seatId : seatIds) {
            seats.get(seatId).status = SeatStatus.BLOCKED;
        }
    }

    public void unblockSeats(List<String> seatIds) {
        for (String seatId : seatIds) {
            Seat seat = seats.get(seatId);
            if (seat != null && seat.status == SeatStatus.BLOCKED) {
                seat.status = SeatStatus.AVAILABLE;
            }
        }
    }

    public void bookSeats(List<String> seatIds) throws InvalidBookingException {
        for (String seatId : seatIds) {
            Seat seat = seats.get(seatId);
            if (seat == null || seat.status != SeatStatus.BLOCKED) {
                throw new InvalidBookingException("Seat " + seatId + " is not blocked or does not exist.");
            }
        }
        for (String seatId : seatIds) {
            seats.get(seatId).status = SeatStatus.BOOKED;
        }
    }

    public double calculateTotalPrice(List<String> seatIds) {
        List<Seat> selectedSeats = new ArrayList<>();
        for (String seatId : seatIds) {
            selectedSeats.add(seats.get(seatId));
        }
        return pricingStrategy.calculatePrice(selectedSeats);
    }
}
