package prep.problems.day03_rate_limiter;

public class TokenBucketRateLimiter implements RateLimiter {

    private final long capacity;
    private double currentTokens;
    private final double refillRate;
    private long lastRefillTimestamp;

    public TokenBucketRateLimiter(long capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.currentTokens = capacity;
        this.lastRefillTimestamp = System.nanoTime();
    }

    @Override
    public synchronized boolean allowRequest(int tokens) {
        refillTokens();
        if (currentTokens >= tokens) {
            currentTokens -= tokens;
            return true;
        }
        return false;
    }

    private void refillTokens() {
        long now = System.nanoTime();
        double elapsedSeconds = (now - lastRefillTimestamp)/1000000000.0;
        double tokensToAdd = elapsedSeconds * refillRate;
        currentTokens = Math.min(capacity, currentTokens + tokensToAdd);
        lastRefillTimestamp = now;
    }
}
