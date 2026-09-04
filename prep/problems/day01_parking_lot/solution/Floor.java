package prep.problems.day01_parking_lot.solution;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Floor {
    int floor_number;
    Map<String,ParkingSlot> parkingSlots;

    public Floor(int floor_number) {
        this.floor_number = floor_number;
        this.parkingSlots = new HashMap<>();
    }

    public Optional<ParkingSlot> findFreeSlot(Vehicle.VehicleType type){
        for(ParkingSlot slot : parkingSlots.values()){
            if(slot.getVehicleType() == type && slot.tryOccupy()){
                return Optional.of(slot);
            }
        }
        return Optional.empty();
    }

    public int getFloor_number() {
        return floor_number;
    }
    public void setFloor_number(int floor_number) {
        this.floor_number = floor_number;
    }
    
    public void addSlot(ParkingSlot slot){
        parkingSlots.put(slot.getSlotId(), slot);
    }

    public ParkingSlot getSlotById(String slotId){
        if(parkingSlots.containsKey(slotId))
            return parkingSlots.get(slotId);
        return null;
    }
}
