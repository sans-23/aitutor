package lld.problems.BookMyShow.sanskar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// singleton pattern
public class BookMyShow {
    public static BookMyShow bookMyShow;
    Map<String, User> users; // Changed to Map
    Map<String, Theatre> theatres; // Changed to Map

    private BookMyShow(){
        this.users = new HashMap<>(); // Initialized as HashMap
        this.theatres = new HashMap<>(); // Initialized as HashMap
    }

    public static BookMyShow init(){
        if(bookMyShow==null){
            bookMyShow = new BookMyShow();
        }
        return bookMyShow;
    }

    public void addUser(User u){
        users.put(u.id, u);
    }

    public void addTheatre(Theatre t){
        theatres.put(t.id, t);
    }

    public Booking createBooking(User user, Show show, List<String> seatIds) {
        // 1. Block seats
        try {
            show.blockSeats(seatIds);
        } catch (SeatUnavailableException e) {
            System.out.println("Booking failed: " + e.getMessage());
            return null;
        }

        // Calculate total price
        double totalPrice = show.calculateTotalPrice(seatIds);

        // 2. Simulate payment (placeholder)
        boolean paymentSuccessful = simulatePayment(); // This would be an actual payment gateway integration

        if (paymentSuccessful) {
            // 3. Book seats
            try {
                show.bookSeats(seatIds);
                List<Seat> bookedSeats = new ArrayList<>();
                for (String seatId : seatIds) {
                    bookedSeats.add(show.seats.get(seatId));
                }
                String bookingId = UUID.randomUUID().toString();
                Booking booking = new Booking(bookingId, user, show, bookedSeats, LocalDateTime.now(), BookingStatus.CONFIRMED, totalPrice);
                System.out.println("Booking confirmed! Booking ID: " + bookingId + ", Total Price: " + totalPrice);
                return booking;
            } catch (InvalidBookingException e) {
                System.out.println("Booking failed after payment: " + e.getMessage() + ". Unblocking seats.");
                show.unblockSeats(seatIds);
                return null;
            }
        } else {
            System.out.println("Payment failed. Unblocking seats.");
            show.unblockSeats(seatIds);
            return null;
        }
    }

    private boolean simulatePayment() {
        // In a real system, this would involve calling a payment gateway
        // For now, let's assume payment is always successful.
        return true;
    }

    public List<Movie> searchMovies(String query, String genre, String language) {
        List<Movie> result = new ArrayList<>();
        for (Theatre theatre : theatres.values()) { // Iterate over map values
            for (Screen screen : theatre.screens) {
                for (Show show : screen.shows) {
                    Movie movie = show.movie;
                    boolean matches = true;
                    if (query != null && !movie.name.toLowerCase().contains(query.toLowerCase())) {
                        matches = false;
                    }
                    if (genre != null && !movie.genre.equalsIgnoreCase(genre)) {
                        matches = false;
                    }
                    if (language != null && !movie.language.equalsIgnoreCase(language)) {
                        matches = false;
                    }
                    if (matches && !result.contains(movie)) {
                        result.add(movie);
                    }
                }
            }
        }
        return result;
    }

    public List<Show> getShowsForMovieAndTheatre(Movie movie, Theatre theatre) {
        List<Show> result = new ArrayList<>();
        for (Screen screen : theatre.screens) {
            for (Show show : screen.shows) {
                if (show.movie.name.equals(movie.name)) {
                    result.add(show);
                }
            }
        }
        return result;
    }

    public List<Show> getShowsForMovieTheatreAndDate(Movie movie, Theatre theatre, LocalDate date) {
        List<Show> result = new ArrayList<>();
        for (Screen screen : theatre.screens) {
            for (Show show : screen.shows) {
                if (show.movie.name.equals(movie.name) && show.date.equals(date)) {
                    result.add(show);
                }
            }
        }
        return result;
    }

    public boolean cancelBooking(Booking booking) {
        if (booking == null || booking.getStatus() == BookingStatus.CANCELLED) {
            System.out.println("Booking is already cancelled or does not exist.");
            return false;
        }

        Show show = booking.getShow();
        List<Seat> bookedSeats = booking.getBookedSeats();
        List<String> seatIdsToUnblock = new ArrayList<>();

        for (Seat seat : bookedSeats) {
            seatIdsToUnblock.add(seat.id);
        }

        show.unblockSeats(seatIdsToUnblock);
        booking.setStatus(BookingStatus.CANCELLED);
        System.out.println("Booking " + booking.getBookingId() + " has been cancelled.");
        return true;
    }
}
