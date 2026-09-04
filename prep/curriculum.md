# 🎯 14-Day Elite LLD & Machine Coding Bootcamp

> **Target:** SDE-2 / Backend Infra Roles (MoEngage, Uber, Swiggy, PhonePe, Atlassian, Razorpay)  
> **Standard:** Production-Grade Java, 45-60 Minute Timed Rounds, Zero Concurrency Flaws, Strict SOLID

---

## 🗓️ Master Schedule

| Day | Focus Category | Problem | Key Patterns & Concurrency Concepts |
| :---: | :--- | :--- | :--- |
| **01** | Resource Management & State | **Parking Lot / Multi-Floor Garage** | Strategy (Allocation), Factory, Concurrency (Slot reservation, AtomicInteger/Locks) |
| **02** | Traffic & Resilience | **Rate Limiter** | Token Bucket, Sliding Window Counter, `ConcurrentHashMap`, `AtomicLong` |
| **03** | In-Memory Storage & ACID | **Key-Value Store with TTL & Transactions** | Command Pattern (Begin/Commit/Rollback), ReadWriteLock, Scheduled Cleaner |
| **04** | Distributed Locking & State | **BookMyShow / Concert Ticket Booking** | State Pattern (Available, Held, Booked), Seat Lock Timeout, Double-Booking Prevention |
| **05** | Complex State & Mathematics | **Splitwise / Expense Sharing App** | Strategy (Equal, Exact, Percent split), Observer, Debt Minimization Graph Algorithm |
| **06** | High-Throughput I/O | **Async Logging Framework / Logger Library** | Strategy (Appenders/Sinks), Chain of Resp (Levels), Disruptor/RingBuffer/Producer-Consumer |
| **07** | **Week 1 Boss Fight** | **In-Memory Pub-Sub / Lightweight Kafka** | Topics, Partitioning, Consumer Groups, Offset Tracking, ReentrantLock |
| **08** | Task Execution & Timing | **Distributed Job / Task Scheduler** | `DelayQueue`, `PriorityBlockingQueue`, Cron parsing, Graceful Shutdown |
| **09** | Geospatial & Lifecycle State | **Ride Sharing / Cab Booking (Uber/Ola)** | Spatial/Proximity Strategy, State Pattern (Requested, Accepted, Enroute, Completed) |
| **10** | Scheduling & State Machines | **Elevator System (Multi-Car)** | SCAN / LOOK Elevator Algorithm, Dispatcher Strategy, Concurrency State Machine |
| **11** | Customer Engagement & Edge | **Enterprise Notification Engine (MoEngage)** | DND Quiet Hours, Multi-Provider Fallback, PriorityQueue OTP bypass, Idempotency |
| **12** | Structural Hierarchies | **In-Memory File System** | Composite Pattern, Visitor Pattern, Permissions, Search Filter Criteria |
| **13** | Concurrency Under Contention | **E-Commerce Flash Sale / Inventory** | Optimistic vs. Pessimistic Locking, Stock Decrement, Hold-and-Release Timeout |
| **14** | **Final Bar-Raiser Exam** | **Surprise Mock Interview (Timed 60m)** | Full unassisted timed execution + Bar Raiser cross-examination |

---

## 📊 Evaluation Rubric (10 Points Scale)

Each daily problem is scored across **5 Axes (2 pts each)**:

1. **Requirements Completeness (0-2):** All P0 functional requirements working and demonstrated with end-to-end driver code.
2. **Design Patterns & Modeling (0-2):** Appropriate patterns used cleanly (not forced). Clean domain model without God objects.
3. **SOLID & Extensibility (0-2):** OCP compliant (adding new strategy requires zero edits to existing classes). Information hiding.
4. **Concurrency & Thread Safety (0-2):** No race conditions under multiple concurrent callers. Clean synchronization, proper collection choices.
5. **Code Craftsmanship (0-2):** Zero compilation warnings, expressive naming, clean exceptions, proper unit/scenario tests.

**Passing Bar:** `>= 8.0 / 10.0`
