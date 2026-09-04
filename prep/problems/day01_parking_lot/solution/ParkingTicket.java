package prep.problems.day01_parking_lot.solution;

import java.time.LocalDateTime;

public record ParkingTicket(
    String ticketId, 
    String licensePlate, 
    Vehicle.VehicleType vehicleType, 
    String slotId,
    String entryGateId, 
    LocalDateTime entryTime) {
}
