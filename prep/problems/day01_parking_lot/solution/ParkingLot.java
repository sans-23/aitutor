package prep.problems.day01_parking_lot.solution;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import prep.problems.day01_parking_lot.solution.Vehicle.VehicleType;

public class ParkingLot {
    
    List<Floor> floors;
    List<EntryGate> entryGates;
    List<ExitGate> exitGates;
    Map<String, ParkingTicket> tickets;
    Map<String, Vehicle> vehicleParkingMap;
    Map<String, ParkingSlot> slotMap;

    private SlotAllocationStrategy allocationStrategy;
private PricingStrategy pricingStrategy;

    public ParkingLot(List<Floor> floors, SlotAllocationStrategy allocationStrategy, PricingStrategy pricingStrategy) {
        this.floors = floors;
        this.allocationStrategy = allocationStrategy;
        this.pricingStrategy = pricingStrategy;
        this.tickets = new ConcurrentHashMap<>();
        this.vehicleParkingMap = new ConcurrentHashMap<>();
        this.slotMap = new ConcurrentHashMap<>();
        this.entryGates = new ArrayList<>();
        this.exitGates = new ArrayList<>();
    }

    public class ExitGate {
        String id;
        public ExitGate(String id) { this.id = id; }
        public PaymentReceipt processExit(String ticketId){
            ParkingTicket ticket = tickets.get(ticketId);
            if (ticket == null) {
                throw new IllegalArgumentException("Invalid or already processed ticket ID: " + ticketId);
            }
            exitVehicle(ticket);
            double fee = pricingStrategy.calculateFee(ticket.vehicleType(), ticket.entryTime());
            return new PaymentReceipt(
                ticket.ticketId(), 
                ticket.licensePlate(), 
                ticket.vehicleType(), 
                ticket.slotId(), 
                ticket.entryTime(),
                LocalDateTime.now(),
                fee);
        }
    }

    public class EntryGate {
        String id;
        public EntryGate(String id) { this.id = id; }
        public ParkingTicket issueTicket(Vehicle vehicle){
            String ticketId = UUID.randomUUID().toString();
            ParkingSlot slot = allocationStrategy.allocate(floors, vehicle.getType()).orElseThrow(()->new IllegalStateException("No available slot for: " + vehicle.getType()));
            tickets.put(ticketId, new ParkingTicket(ticketId, vehicle.getLicensePlate(), vehicle.getType(), slot.getSlotId(), id, LocalDateTime.now()));
            vehicleParkingMap.put(vehicle.getLicensePlate(), vehicle);
            slotMap.put(slot.getSlotId(), slot);
            return tickets.get(ticketId);
        }
    }

    public void addEntryGate(){
        entryGates.add(new EntryGate(UUID.randomUUID().toString()));
    }

    public void addExitGate(){
        exitGates.add(new ExitGate(UUID.randomUUID().toString()));
    }

    public ParkingSlot findFreeSlot(Vehicle.VehicleType type){
        for(Floor floor : floors){
            Optional<ParkingSlot> slot = floor.findFreeSlot(type);
            if(slot.isPresent()){
                return slot.get();
            }
        }
        throw new IllegalStateException("No available slot for: " + type);
    }

    public void exitVehicle(ParkingTicket ticket){
        String slotId = ticket.slotId();
        ParkingSlot slot = slotMap.get(slotId);
        slot.vacate();
        tickets.remove(ticket.ticketId());
        vehicleParkingMap.remove(ticket.licensePlate());
    }

    public static void main(String[] args) {
        Vehicle v1 = new Vehicle(VehicleType.COMPACT, "KA01AB1234");
        Vehicle v2 = new Vehicle(VehicleType.TWO_WHEELER, "KA01BC4567");
        Vehicle v3 = new Vehicle(VehicleType.TWO_WHEELER, "KA01CD8901");
        Vehicle v4 = new Vehicle(VehicleType.TWO_WHEELER, "KA01DE1234");

        List<Floor> floors = new ArrayList<>();
        
        Floor f1 = new Floor(1);
        f1.addSlot(new ParkingSlot("1A", VehicleType.TWO_WHEELER));
        f1.addSlot(new ParkingSlot("1B", VehicleType.COMPACT));
        f1.addSlot(new ParkingSlot("1D", VehicleType.HEAVY));
        floors.add(f1);

        Floor f2 = new Floor(2);
        f2.addSlot(new ParkingSlot("2A", VehicleType.TWO_WHEELER));
        f2.addSlot(new ParkingSlot("2B", VehicleType.COMPACT));
        f2.addSlot(new ParkingSlot("2D", VehicleType.HEAVY));
        floors.add(f2);

        ParkingLot parkingLot = new ParkingLot(floors, new LowestFloorFirstStrategy(), new FlatHourlyPricingStrategy());
        parkingLot.addEntryGate();
        parkingLot.addEntryGate();
        parkingLot.addExitGate();
        parkingLot.addExitGate();

        ExecutorService ex = Executors.newFixedThreadPool(3);

        ex.submit(() -> {
            try{
            parkingLot.entryGates.get(0).issueTicket(v1);}
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        });

        ex.submit(() -> {
            try {
                parkingLot.entryGates.get(1).issueTicket(v2);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        ex.submit(()-> {
            try {
                parkingLot.entryGates.get(0).issueTicket(v3);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            
        });
        ex.submit(()-> {
            try {
                parkingLot.entryGates.get(0).issueTicket(v4);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        ex.shutdown();
        try {
            ex.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(ParkingTicket ticket : parkingLot.tickets.values()){
            PaymentReceipt r = parkingLot.exitGates.get(0).processExit(ticket.ticketId());
            System.out.println(r); 
        }  
    }
}
