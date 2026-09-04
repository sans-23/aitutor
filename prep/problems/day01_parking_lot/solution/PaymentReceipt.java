package prep.problems.day01_parking_lot.solution;

import java.time.LocalDateTime;

public record PaymentReceipt(
    String ticketId, 
    String licensePlate, 
    Vehicle.VehicleType vehicleType, 
    String slotId, 
    LocalDateTime entryTime,
    LocalDateTime exitTime,
    double totalFee) {
}