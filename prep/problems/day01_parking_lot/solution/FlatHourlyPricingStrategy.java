package prep.problems.day01_parking_lot.solution;

import java.time.LocalDateTime;

public class FlatHourlyPricingStrategy implements PricingStrategy{
    
    @Override
    public double calculateFee(Vehicle.VehicleType type, LocalDateTime entryTime) {
        long hours = Math.max(1, java.time.Duration.between(entryTime, LocalDateTime.now()).toHours());
        double hourlyRate = switch (type) {
            case TWO_WHEELER -> 10.0;
            case COMPACT -> 20.0;
            case LARGE -> 30.0;
            case HEAVY -> 50.0;
        };
        return hours * hourlyRate;
    }
}
