package prep.problems.day01_parking_lot.solution;

import java.util.concurrent.atomic.AtomicBoolean;

public class ParkingSlot {
    String slotId;
    Vehicle.VehicleType vehicleType;
    AtomicBoolean isOccupied;

    public ParkingSlot(String slotId, Vehicle.VehicleType vehicleType) {
        this.slotId = slotId;
        this.vehicleType = vehicleType;
        this.isOccupied = new AtomicBoolean(false);
    }

    public String getSlotId() {
        return slotId;
    }
    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }
    public Vehicle.VehicleType getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(Vehicle.VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
    public boolean isOccupied() {
        return isOccupied.get();
    }
    public void setOccupied(boolean isOccupied) {
        this.isOccupied.set(isOccupied);
    }

    public boolean tryOccupy() {
        return isOccupied.compareAndSet(false, true);
    }

    public void vacate() {
        isOccupied.set(false);
    }
}
