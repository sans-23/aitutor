package prep.problems.day03_rate_limiter;

public interface RateLimiter {
    boolean allowRequest(int tokens);

    default boolean allowRequest() {
        return allowRequest(1);
    }
}
