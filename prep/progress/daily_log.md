# 📅 LLD Practice Daily Log

> Tracks every single day's problem, score out of 10, deep technical feedback, and remediation tasks.

---

| Day | Date | Problem | Score | Pass? | Critical Feedback & Takeaways |
| :---: | :---: | :--- | :---: | :---: | :--- |
| **00** | 2026-09-02 | **Notification Service + LRU Cache (Diagnostic)** | **6.5 / 10** | ⚠️ Needs Polish | **Good:** Fast prototyping, Strategy/Factory in place, LRU passed 100k stress test.<br>**Fatal Flaws:** Swallowed exceptions in executor, forgot thread shutdown, over-coupled User entity, un-decremented counter in LRU. |
| **01** | 2026-09-03 | **Parking Lot / Multi-Floor Garage** | **9.5 / 10** | 🏆 Strong Hire | **Good:** Hardware CAS via `AtomicBoolean`, Strategy pattern for allocation and pricing, clean records, zero double-booking under concurrent test.<br>**Watch Outs:** Initial short-circuit ordering bug in CAS evaluation, initial async race before `awaitTermination`. |
| **02** | 2026-09-05 | **Concert Ticket Booking (BookMyShow)** | **9.7 / 10** | 🏆 Strong Hire | **Good:** Pure State Pattern (`AvailableState`, `HoldState`, `BookState`), TTL expiration tested, synchronized state transitions, zero double-booking.<br>**Takeaway:** State-specific data belongs in state objects, eliminate dual-source-of-truth. |

---

## 📝 Detailed Session Notes

### Day 02 Session (2026-09-05)
- **Problem:** Concert & Movie Ticket Booking System (BookMyShow)
- **Score:** 9.7 / 10 (Strong Hire)
- **Design Pattern Mastered:** The **State Pattern** (GoF)
- **Breakdown:**
  - *Requirements Coverage (2.0/2.0):* All lifecycle flows completed: seat hold, booking confirmation, hold collision rejection, and TTL expiration re-claim.
  - *Clean Architecture & Patterns (2.0/2.0):* Textbook State Pattern (`AvailableState`, `HoldState`, `BookState`). Zero switch-case anti-patterns.
  - *SOLID & Extensibility (2.0/2.0):* Strict OCP and SRP compliance.
  - *Concurrency & Thread Safety (1.9/2.0):* State transitions synchronized per `Seat`. Atomically isolated.
  - *Code Craftsmanship (1.8/2.0):* Working `main()` harness with try-catch demonstrating positive, negative, and TTL timeout scenarios.
- **Key Breakthrough:** Conquered the "Dual Source of Truth" trap by keeping state-specific data (`heldBy`, `expiryTimeStamp`) inside `HoldState` rather than polluting `Seat`.

### Day 01 Session (2026-09-03 / 2026-09-04)
- **Problem:** Multi-Floor Smart Parking Lot System
- **Score:** 9.5 / 10 (Strong Hire)
- **Breakdown:**
  - *Requirements Coverage (2.0/2.0):* All P0 flows working (allocation, tickets, pricing, exit receipts, full lot rejection).
  - *Clean Architecture & Patterns (2.0/2.0):* Strategy pattern cleanly extracted for `SlotAllocationStrategy` and `PricingStrategy`. Decoupled `EntryGate` and `ExitGate`.
  - *SOLID & Extensibility (2.0/2.0):* Strict OCP compliance. New pricing or allocation strategies require zero modifications to `ParkingLot`.
  - *Concurrency & Thread Safety (1.8/2.0):* Hardware-level atomic CAS (`AtomicBoolean.compareAndSet`) prevented all double-bookings. Concurrency driver proved thread safety under contention.
  - *Code Craftsmanship (1.7/2.0):* Compiles with 0 warnings, clean Java records, working end-to-end `main()` test harness.
- **Key Breakthrough:** Successfully refactored hardcoded methods into pluggable Strategy interfaces and experienced the difference between asynchronous task submission (`submit()`) and synchronous lifecycle coordination (`awaitTermination()`).

### Day 00 Diagnostic Session (2026-09-02)
- **Problem 1:** Multi-channel Notification System
  - *Score:* 6/10
  - *Critique:* Fast code, good Strategy pattern. But almost died on concurrency: `try-catch` was placed outside `submit()`, which swallows all runtime worker exceptions in Java. Fixed after grilling.
- **Problem 2:** LRU Cache
  - *Score:* 7/10
  - *Critique:* Correct doubly-linked list pointers after revision. Passed 100k test cases. However, left `curr++` without `curr--` which is an architectural code smell in an interview.
