package prep.problems.day01_parking_lot.solution;

public class Vehicle {
    public enum VehicleType{
        TWO_WHEELER,
        COMPACT,
        LARGE,
        HEAVY
    }

    VehicleType type;
    String licensePlate;

    public Vehicle(VehicleType type, String licensePlate) {
        this.type = type;
        this.licensePlate = licensePlate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public VehicleType getType() {
        return type;
    }
   
    public void setType(VehicleType type) {
        this.type = type;
    }

}
