# 🛡️ Concurrency Survival Kit for Java LLD

> **Targeted at:** Crushing WP-01 (Exception Swallowing), WP-02 (Thread Hangs), and WP-05 (Thread-Safety Reflexes).

---

## 1. The ExecutorService Golden Template (Memorize This)

```java
public class ResilientService {
    // 1. Thread Pool: 10 fixed workers
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public void processTaskAsync(Task task) {
        // 2. ALWAYS put try-catch INSIDE the runnable lambda!
        executor.submit(() -> {
            try {
                // Actual heavy work (network, DB, etc.)
                task.execute();
            } catch (Exception e) {
                // 3. NEVER swallow! Log with context
                System.err.println("Task failed for id " + task.getId() + ": " + e.getMessage());
            }
        });
    }

    // 4. ALWAYS provide graceful shutdown
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
```

---

## 2. Java Collections Reflex Cheat-Sheet

| Context | ❌ Dangerous Choice | ✅ Bar-Raiser Choice | Why? |
| :--- | :--- | :--- | :--- |
| **Shared key-value state** | `HashMap` | `ConcurrentHashMap` | Atomic operations (`computeIfAbsent`, `merge`, `putIfAbsent`), lock striping. |
| **Slot reservation / counters** | `int counter;` | `AtomicInteger` | Lock-free CAS (`incrementAndGet`, `decrementAndGet`, `compareAndSet`). |
| **Shared List (read-heavy)** | `ArrayList` | `CopyOnWriteArrayList` | Safe concurrent iterations without `ConcurrentModificationException`. |
| **Producer-Consumer Buffer** | `LinkedList` | `LinkedBlockingQueue(cap)` | Built-in thread blocking, bounds memory usage, prevents OOM. |
| **Priority Processing (e.g. OTP > Promo)** | `PriorityQueue` | `PriorityBlockingQueue` | Thread-safe heap for prioritized execution. |

---

## 3. Atomic State Updates (Avoiding Race Conditions)

```java
// ❌ RACE CONDITION (Check-then-act anti-pattern):
if (slots.containsKey(id)) {
    slots.put(id, value); // Another thread can delete/modify in between!
}

// ✅ ATOMIC & THREAD-SAFE:
slots.computeIfAbsent(id, k -> new Slot(k));
slots.computeIfPresent(id, (k, slot) -> slot.reserve());
```
