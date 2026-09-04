# 🅿️ Day 01: Multi-Floor Smart Parking Lot System

> **Difficulty:** Medium (Core SDE-2 Machine Coding Classic)  
> **Time Limit:** 50 Minutes  
> **Target Concepts:** Strategy Pattern, Factory/Enum Modeling, Concurrency & Atomic Slot Locking, State Invariants

---

## 🏢 Business Context
You are tasked with designing and coding the core backend engine for a multi-floor automated parking garage operating 24/7 across multiple entry and exit gates.

---

## 📋 Functional Requirements (P0 - Must Have)

1. **Multi-Floor & Multi-Slot Architecture:**
   - The parking lot has multiple floors.
   - Each floor has dedicated slots for different vehicle types:
     - `TWO_WHEELER` (Bikes / Scooters)
     - `COMPACT` (Hatchbacks / Sedans)
     - `LARGE` (SUVs / Vans)
     - `HEAVY` (Buses / Trucks)
   - A vehicle can ONLY park in an eligible slot (e.g., a Two-Wheeler can park in a `TWO_WHEELER` slot, but a `HEAVY` vehicle cannot park in a `COMPACT` slot).

2. **Entry & Ticket Issuance:**
   - When a vehicle arrives at an entry gate:
     - The system selects an optimal available slot based on an **Allocation Strategy**.
     - Marks the slot as occupied.
     - Issues an immutable `ParkingTicket` with: `ticketId`, `licensePlate`, `vehicleType`, `allocatedSlotId`, `entryTime`.
   - If no suitable slot is available, cleanly reject the entry with a meaningful message/error.

3. **Pluggable Slot Allocation Strategies (Strategy Pattern):**
   - Must support at least two interchangeable allocation strategies:
     - **Strategy A (Default):** Lowest floor first, then lowest slot number (compact allocation).
     - **Strategy B:** Natural distribution (e.g. nearest to entrance or floor with maximum available slots).

4. **Exit, Billing & Payment (Strategy Pattern):**
   - When a vehicle leaves at an exit gate, provide the `ticketId`.
   - Calculate fee based on duration and a pluggable **Pricing Strategy**:
     - Example: Flat rate for first 2 hours, then per-hour rate dynamic by `VehicleType`.
   - Free up the parking slot.
   - Return a `Receipt` with total duration, fee, and exit timestamp.

---

## ⚡ Non-Functional Requirements (The Bar-Raiser Checklist)

1. **Thread-Safety & Race Condition Prevention (CRITICAL):**
   - Two vehicles entering from different gates simultaneously must **never** be assigned the same parking slot!
   - Slot reservation must be atomic and thread-safe.
2. **State Invariant Balance (Watch WP-03):**
   - Floor and lot availability counters must cleanly increment on exit and decrement on entry without drift.
3. **Clean Code & Extensibility:**
   - Adding a new vehicle type (`ELECTRIC_VEHICLE`) or a new pricing strategy (e.g. Peak Surge Pricing) should require zero changes to the core `ParkingLotService` (Open-Closed Principle).

---

## 🚀 Getting Started

Create your solution in this folder:
`prep/problems/day01_parking_lot/ParkingLotSystem.java`

Include a self-contained `public static void main(String[] args)` that demonstrates:
1. Parking lot initialization with 2+ floors and varied slot types.
2. Multiple vehicles parking and receiving tickets.
3. Attempting to park when a vehicle category is full.
4. Vehicles unparking, calculating fees, and freeing up slots.
5. Concurrent parking test (simulating 2 threads parking simultaneously).
