package prep.problems.day01_parking_lot.solution;

import java.util.List;
import java.util.Optional;

public class LowestFloorFirstStrategy implements SlotAllocationStrategy{
    
    @Override
    public Optional<ParkingSlot> allocate(List<Floor> floors, Vehicle.VehicleType type) {
        for(Floor floor : floors){
            Optional<ParkingSlot> slot = floor.findFreeSlot(type);
            if(slot.isPresent()){
                return slot;
            }
        }
        return Optional.empty();
    }

    @Override
    public void deallocate(ParkingSlot slot) {
        slot.vacate();
    }
}
