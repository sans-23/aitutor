# 🚦 Day 03: Distributed-Ready In-Memory Rate Limiter

> **Difficulty:** Medium-Hard | **Target Role:** SDE-2 (Backend Infra / API Gateway)  
> **Company Relevance:** Uber, Swiggy, MoEngage, Atlassian, Stripe  
> **Allowed Time:** 45 - 60 Minutes

---

## 📌 1. Real-World Business Context

At high-scale tech companies (Uber, Swiggy, MoEngage), edge API gateways process hundreds of thousands of HTTP requests per second. Without robust rate limiting:
1. **Noisy Neighbor Problem:** A single buggy or rogue client script can saturate backend threads, starving critical services.
2. **Denial of Service (DoS):** Uncontrolled traffic bursts can cascade failures across downstream databases.
3. **Monetization & Quotas:** Tier-based API access (e.g., Free vs. Pro vs. Enterprise) requires strict, per-tenant quota enforcement.

Your task is to design and implement a **production-grade, thread-safe In-Memory Rate Limiter** library that can be plugged into an API gateway filter.

---

## 🎯 2. Functional Requirements

### P0 (Must Have - Core Deliverables)
1. **Multi-Tenant / Client Isolation:**
   - Rate limit by client/tenant identifier (`clientId`, `apiKey`, or `userId`).
   - Rate limits for Client A must operate completely independently of Client B.
2. **Pluggable Algorithms (Strategy Pattern):**
   - The system must support multiple rate-limiting strategies without changing core client/gateway code:
     - **Algorithm 1: Token Bucket** (Supports steady rate + configurable burst capacity).
     - **Algorithm 2: Sliding Window Counter** (or **Fixed Window / Sliding Window Log**) (Smooth distribution across rolling time window).
3. **Core API Contract:**
   - `boolean allowRequest(String clientId)`: Evaluates whether 1 request is allowed.
   - `boolean allowRequest(String clientId, int tokens)`: Evaluates whether a batch of tokens/cost is allowed.
4. **Tier / Quota Configuration:**
   - Support configurable rules per client tier (e.g. `FREE`: 5 req/sec with max burst 5; `PREMIUM`: 50 req/sec with max burst 50).
   - If a client has no explicit tier, fall back to a sensible default.

### P1 (Good to Have - Bar Raiser)
- **Idle Client Eviction / Memory Bound:** Idle client entries should not leak memory indefinitely.
- **Metrics / Observability:** Track dropped vs allowed request counters per client.

---

## ⚙️ 3. Non-Functional Requirements & Engineering Standards

1. **High Concurrency & Low Latency:**
   - The rate limiter sits in the critical path of *every single request*.
   - **Zero Global Synchronization:** Never synchronize on the entire `RateLimiterService` class or global map! Locks (if used) or atomic operations must be scoped per client.
   - Use proper concurrent data structures (`ConcurrentHashMap`, `AtomicLong`, `AtomicInteger`, or fine-grained `ReentrantLock`).
2. **Strict SOLID Compliance:**
   - **Open-Closed Principle (OCP):** Adding a new algorithm (e.g. Leaky Bucket) must require ZERO changes to existing algorithm classes or caller services.
   - **Single Responsibility Principle (SRP):** Clear separation between algorithm mechanics, client state, configuration rules, and registry management.
3. **Vulnerabilities Under Watch (From Active Log):**
   - **WP-05 (Collection Thread-Safety Reflex):** No bare `HashMap` or `ArrayList` anywhere in multi-threaded paths.
   - **WP-06 (Short-Circuit Side Effects):** Never mutate tokens or state inside short-circuited boolean checks before confirming prerequisites.
4. **Verification & Test Harness:**
   - Provide a comprehensive, executable `main()` method:
     - Test 1: Single-thread quota exhaustion & rejection.
     - Test 2: Time-based token refill / window recovery.
     - Test 3: High-concurrency stress test with multiple threads contending for tokens (demonstrate no race conditions, no over-allocation, and clean thread pool shutdown with `awaitTermination`).
     - Test 4: Dynamic switching between Token Bucket and Sliding Window strategies.

---

## ⏱️ Recommended 45-Minute Execution Plan

- **00 - 10 min:** Clarify requirements, define domain models, declare Strategy interfaces (`RateLimiterAlgorithm`).
- **10 - 25 min:** Implement **Token Bucket** algorithm with atomic precision and thread-safe per-client state.
- **25 - 35 min:** Implement **Sliding Window** (or alternative strategy) + Tier/Config Manager.
- **35 - 45 min:** Implement `RateLimiterService` (Registry) + Concurrency stress test harness in `main()`.

---

## 🚀 Let's Go!
Start by outlining:
1. What interfaces and domain entities are you planning?
2. How will you store per-client state to guarantee thread safety without a global bottleneck?
