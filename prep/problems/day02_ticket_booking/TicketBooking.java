package prep.problems.day02_ticket_booking;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketBooking {
    private Map<String, Seat> seats;

    public TicketBooking(Map<String, Seat> seats) {
        this.seats = seats;
    }

    public void holdSeats(String seatId, User user, long ttl) {
        Seat seat = seats.get(seatId);
        seat.holdSeat(user, ttl);
    }

    public void confirmBooking(String seatId, User user) {
        Seat seat = seats.get(seatId);
        seat.bookSeat(user);
    }

    public void release(String seatId, User user) {
        Seat seat = seats.get(seatId);
        seat.release(user);
    }

    public static void main(String args[]) {
        Map<String, Seat> seats = new HashMap<>();
        seats.put("A1", new Seat("A1"));
        seats.put("A2", new Seat("A2"));
        seats.put("A3", new Seat("A3"));
        seats.put("A4", new Seat("A4"));
        seats.put("A5", new Seat("A5"));

        TicketBooking ticketBooking = new TicketBooking(seats);

        User user1 = new User("user1");
        User user2 = new User("user2");

        ticketBooking.holdSeats("A1", user1, 1000);
        try {
            ticketBooking.holdSeats("A1", user2, 1000);
        } catch (IllegalStateException e) {
            System.out.println("✅ Correctly rejected User 2 from holding A1: " + e.getMessage());
        }

        ticketBooking.holdSeats("A2", user1, 1000);
        ticketBooking.holdSeats("A3", user2, 1000);
        ticketBooking.holdSeats("A5", user2, 1000);

        ticketBooking.confirmBooking("A1", user1);
        ticketBooking.confirmBooking("A2", user1);
        ticketBooking.confirmBooking("A3", user2);
        ticketBooking.confirmBooking("A5", user2);

        ticketBooking.release("A1", user1);
        ticketBooking.release("A2", user1);
        ticketBooking.release("A3", user2);
        ticketBooking.release("A5", user2);

        // Test TTL
        ticketBooking.holdSeats("A4", user1, 2000);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            ticketBooking.holdSeats("A4", user2, 1000);
            System.out.println("✅ User 2 successfully booked A4 after User 1's hold expired");
        } catch (IllegalStateException e) {
            System.out.println("❌ User 2 could not book A4: " + e.getMessage());
        }

        // ----------------------------------------------------
        // 🔥 HIGH-CONTENTION CONCURRENCY STRESS TEST
        // 10 Threads race simultaneously to hold the SAME seat
        // ----------------------------------------------------
        System.out.println("\n--- 🚀 Running Multi-Threaded Contention Test (10 Threads -> 1 Seat) ---");
        seats.put("B1", new Seat("B1"));
        
        int numThreads = 10;
        ExecutorService ex = Executors.newFixedThreadPool(numThreads);
        CountDownLatch startGun = new CountDownLatch(1); // Fires all threads at the EXACT same millisecond
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger rejectCount = new AtomicInteger(0);

        for (int i = 1; i <= numThreads; i++) {
            final String uId = "user_" + i;
            ex.submit(() -> {
                try {
                    startGun.await(); // Wait for the start signal
                    ticketBooking.holdSeats("B1", new User(uId), 5000);
                    successCount.incrementAndGet();
                    System.out.println("🎉 " + uId + " WON the race and held B1!");
                } catch (IllegalStateException e) {
                    rejectCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Fire the gun! All 10 threads hit holdSeats() simultaneously
        startGun.countDown();
        ex.shutdown();
        try {
            ex.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("📊 Results: Successes = " + successCount.get() + " (Expected: 1) | Rejections = " + rejectCount.get() + " (Expected: 9)");
        if (successCount.get() == 1 && rejectCount.get() == numThreads - 1) {
            System.out.println("🏆 CONCURRENCY TEST PASSED: Zero double-booking under intense contention!");
        } else {
            System.out.println("❌ CONCURRENCY RACE DETECTED!");
        }
    }
}
