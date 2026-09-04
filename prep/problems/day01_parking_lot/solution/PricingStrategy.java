package prep.problems.day01_parking_lot.solution;

import java.time.LocalDateTime;

public interface PricingStrategy {
    double calculateFee(Vehicle.VehicleType type, LocalDateTime entryTime);
}
