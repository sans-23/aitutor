# 🎯 Candidate Weak Points & Vulnerability Tracker

> **Candidate:** Sanskar Jain  
> **Target Level:** SDE-2 (Backend Infrastructure / Distributed Systems)  
> **Primary Language:** Java (Transitioned from C++ CP background)  
> **Status:** Active Bootcamp

---

## ⚡ Active Vulnerabilities (To Be Crushed)

| # | Vulnerability | Severity | First Spotted | Status | Remediation Action |
|---|---|:---:|:---:|:---:|---|
| **WP-01** | **Silent Exception Swallowing in Concurrency**<br>Putting `try-catch` around `executor.submit()` instead of *inside* the background worker lambda. | 🔴 Critical | 2026-09-02 (Notif Service) | 🟢 Remediated in Day 01 | Always write `executor.submit(() -> { try { ... } catch(Exception e) { ... } });` |
| **WP-02** | **Non-Daemon Thread Hangs / Race Conditions**<br>Leaving thread pools un-shutdown or failing to coordinate completion with `awaitTermination()`. | 🟠 High | 2026-09-02 (Notif Service) | 🟢 Remediated in Day 01 | Combine `ex.shutdown()` with `ex.awaitTermination(...)` to ensure tasks complete before asserting state. |
| **WP-03** | **Unbalanced State Counters**<br>Incrementing counters (e.g., `curr++`) but forgetting matching decrement on eviction/release, leading to broken state invariants. | 🟠 High | 2026-09-03 (LRU Cache) | 🟢 Remediated in Day 01 | Avoid raw counters; manage slot state via atomic pairs (`tryOccupy()` / `vacate()`) and thread-safe collections. |
| **WP-04** | **Over-Coupling Entity Scope (ISP Violation)**<br>Passing bloated domain entities to dumb sinks. | 🟡 Medium | 2026-09-02 (Notif Service) | 🟢 Remediated in Day 01 | Clean immutable `record` DTOs (`ParkingTicket`, `PaymentReceipt`) used effectively. |
| **WP-05** | **Collection Thread-Safety Reflex**<br>Defaulting to standard `HashMap` or `ArrayList` in shared multi-threaded service layers instead of `ConcurrentHashMap`. | 🔴 Critical | 2026-09-02 (Notif Service) | 🟢 Conquered in Day 03 | Passed Day 02 & Day 03 cleanly. `ConcurrentHashMap` with atomic `computeIfAbsent` used seamlessly. |
| **WP-06** | **Short-Circuit Side Effects in Guard Conditions**<br>Executing a mutating method (e.g., `slot.tryOccupy()`) before checking prerequisites (`slot.getVehicleType() == type`) in an `&&` expression. | 🔴 Critical | 2026-09-04 (Parking Lot) | 🟢 Remediated in Day 03 | In guard conditions, ALWAYS verify read-only preconditions FIRST (`type == expected`) before invoking state-mutating operations (`tryOccupy()`). |
| **WP-07** | **Time-Unit Mismatch & Integer Token Truncation**<br>Mixing `System.nanoTime()` with milliseconds or using integer division (`/ 1_000_000_000`), truncating sub-second intervals to zero and starving token refills. | 🟠 High | 2026-09-05 (Rate Limiter) | 🟢 Remediated in Day 03 | Standardize time units (millis vs nanos) across all algorithms; use `double currentTokens` to accumulate fractional refill tokens smoothly. |

---

## 💪 Confirmed Strengths (Do Not Lose These)
- ✅ Strong algorithmic intuition and fast complexity analysis (Codeforces background).
- ✅ Rapid implementation speed (writes runnable Java prototypes in <15 minutes).
- ✅ Grasps the **Strategy Pattern** deeply and cleanly executes dependency injection.
- ✅ High resilience and willingness to iterate under intense bar-raiser pressure.
- ✅ Uses modern Java `record` naturally for clean, immutable data modeling.
- ✅ Instinctively designs for multi-tenant isolation and per-instance concurrency to prevent global bottlenecks.

---

## 📈 Conquered Vulnerabilities
*(Items move here once candidate passes 2 consecutive problems without exhibiting the flaw)*
- 🏆 **WP-01: Silent Exception Swallowing in Concurrency** (Passed Day 01 & Day 02 cleanly)
- 🏆 **WP-02: Non-Daemon Thread Hangs / Lifecycle Leaks** (Passed Day 01 & Day 03 cleanly)
- 🏆 **WP-03: Unbalanced State Counters** (Passed Day 01 & Day 02 cleanly)
- 🏆 **WP-05: Collection Thread-Safety Reflex** (Passed Day 02 & Day 03 cleanly with `ConcurrentHashMap`)

