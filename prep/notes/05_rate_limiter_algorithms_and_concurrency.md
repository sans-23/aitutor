# 🚦 Rate Limiter Architecture: Algorithms, Concurrency & Gateway Integration

> **Targeted at:** API Gateway traffic shaping, multi-tenant fairness, zero global lock contention, and avoiding common timing/precision bugs in Java.

---

## 1. The Core Dilemma: Algorithm Comparison Cheat Sheet

| Algorithm | Data Structure | Memory per Tenant | Contention & Performance | Handling Bursts | Accuracy |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Token Bucket** | 2 numbers (`double tokens`, `long lastRefill`) | $O(1)$ constant (tiny: ~24 bytes) | High (fast lock or CAS) | ✅ Allows controlled burst up to bucket capacity | Very High (refills smoothly over time) |
| **Fixed Window** | 2 numbers (`long count`, `long windowStart`) | $O(1)$ constant | Extremely Fast | ❌ Boundary Burst Trap: $2\times$ burst at window boundaries | Low (spiky at edges) |
| **Sliding Window Log** | `Deque<Long>` (request timestamps) | $O(M)$ where $M$ is request count in window | Moderate (queue trimming overhead) | ❌ Hard cap, rejects bursts above limit | 100% Precise (no boundary spikes) |
| **Sliding Window Counter** | Current window count + Previous window weight | $O(1)$ constant | Fast | ⚠️ Approximated burst smoothing | ~99% accurate (industry standard: Cloudflare) |

---

## 2. Common Anti-Patterns & How to Avoid Them

### ❌ Anti-Pattern 1: The "Sub-Second Starvation" Integer Division Bug
When calculating token refills in a loop:
```java
// ❌ BROKEN: Integer division truncates sub-second intervals to 0!
long elapsedSeconds = (now - lastRefillTimestamp) / 1_000_000_000L;
long tokensToAdd = (long) (elapsedSeconds * refillRate);
currentTokens = Math.min(capacity, currentTokens + tokensToAdd);
lastRefillTimestamp = now; // 💥 Resets timestamp even when 0 tokens added!
```
**Why it fails:** If requests arrive every 100ms, `elapsedSeconds` is ALWAYS `0`. Because `lastRefillTimestamp` is reset to `now` every time, accumulated time is erased, and **zero tokens are ever added**.

**✅ Production Fix:**
Use `double currentTokens` and floating-point math:
```java
double elapsedSeconds = (now - lastRefillTimestamp) / 1_000_000_000.0;
double tokensToAdd = elapsedSeconds * refillRate;
currentTokens = Math.min(capacity, currentTokens + tokensToAdd);
lastRefillTimestamp = now;
```

---

### ❌ Anti-Pattern 2: The Global Lock Bottleneck
```java
// ❌ FATAL BOTTLENECK: Every tenant in the company serializes through this single lock!
public synchronized boolean allowRequest(String clientId) {
    RateLimiter limiter = limiters.get(clientId);
    return limiter.allowRequest();
}
```
**✅ Production Fix:**
Isolate synchronization **per-instance** / **per-tenant**:
```java
// In Gateway Service:
ConcurrentHashMap<String, RateLimiter> limiters = new ConcurrentHashMap<>();

public boolean allowRequest(Request request) {
    // Fast lock-free lookup or atomic initialization
    RateLimiter limiter = limiters.computeIfAbsent(request.getKey(), k -> factory.getRateLimiter(quota));
    
    // Locks ONLY this tenant's instance! Other tenants run completely in parallel.
    return limiter.allowRequest(1);
}
```

---

## 3. High-Concurrency Contention Pattern: `CountDownLatch` Gun & Target
To stress test race conditions in machine coding rounds:
```java
CountDownLatch gun = new CountDownLatch(1);
ExecutorService ex = Executors.newFixedThreadPool(10);
AtomicInteger allowed = new AtomicInteger(0);
AtomicInteger denied = new AtomicInteger(0);

for (int i = 0; i < 30; i++) {
    ex.submit(() -> {
        try {
            gun.await(); // All 30 threads hold here
            if (service.allowRequest(request, 1)) {
                allowed.incrementAndGet();
            } else {
                denied.incrementAndGet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    });
}

gun.countDown(); // BANG! All 30 threads fire at the exact same instant

ex.shutdown();
ex.awaitTermination(5, TimeUnit.SECONDS); // MANDATORY: coordinate worker completion!

// Assert invariants:
System.out.println("Allowed: " + allowed.get() + " Denied: " + denied.get());
```

---

## 4. Scaling from In-Memory to Distributed (Interview Discussion)
When asked *"How do you make this distributed across 50 gateway nodes?"*:
1. **Redis + Lua Script:** Single atomic Redis script performing Token Bucket or Sliding Window logic to prevent network round-trip race conditions.
2. **Local JVM Cache + Centralized Sync (Batching):** Instead of calling Redis on every HTTP request, each node claims a batch of tokens (e.g. 50 tokens at once) from Redis and dispenses them locally via `AtomicLong.decrementAndGet()`. Reduces Redis QPS by $50\times$!
