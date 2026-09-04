# LLD Workspace Rules

## Purpose
This workspace is the dedicated training ground for **Low-Level Design (LLD), Machine Coding, and Object-Oriented Architecture** targeted at SDE-2 and backend infra roles (MoEngage, Uber, Swiggy, Atlassian, Razorpay).

## Key Directories & Assets
- `prep/curriculum.md` — 14-Day intensive bootcamp schedule.
- `prep/progress/daily_log.md` — Session history, daily problem scores, and bar-raiser feedback.
- `prep/progress/weak_points.md` — Active candidate vulnerabilities and remediation tracker.
- `prep/notes/` — Targeted revision notes on design patterns and concurrency.
- `prep/problems/` — Structured daily problem folders (`day01_parking_lot`, `day02_ticket_booking`, etc.).
- `design-patterns/` & `problems/` — Repository reference implementations and practice designs.

## Core Rules & Bar-Raiser Standards
1. **Activate the `lld-coach` skill** whenever the candidate mentions LLD, design patterns, or begins daily practice.
2. **Uncompromising Standards:**
   - Production-grade Java, zero swallowed exceptions (`WP-01`), graceful thread pool shutdowns (`WP-02`).
   - Thread-safety under contention: atomic slot/resource reservation (`compareAndSet`, `ConcurrentHashMap`).
   - Strict adherence to SOLID principles, especially the Open-Closed Principle (prefer Strategy/State patterns over giant switch/if-else blocks).
3. **Session Continuity:** Always inspect `daily_log.md` and `weak_points.md` before starting a new problem.
4. **Interactive Sparring:** Use the Socratic method to challenge architecture before writing code.
