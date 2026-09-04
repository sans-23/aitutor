package prep.problems.day01_parking_lot.solution;

import java.util.List;
import java.util.Optional;

public interface SlotAllocationStrategy {
    Optional<ParkingSlot> allocate(List<Floor> floors, Vehicle.VehicleType type);
    void deallocate(ParkingSlot slot);
}
