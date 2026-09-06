package prep.problems.day03_rate_limiter;

public class FixedWindowRateLimiter implements RateLimiter{
    
    private final long windowSizeInMillis;
    private final long maxRequests;
    private long requestCount;
    private long windowStartTime;

    public FixedWindowRateLimiter(long maxRequests, long windowSizeInMillis) {
        this.windowSizeInMillis = windowSizeInMillis;
        this.maxRequests = maxRequests;
        this.requestCount = 0;
        this.windowStartTime = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean allowRequest(int tokens) {
        long now = System.currentTimeMillis();
        if (now - windowStartTime >= windowSizeInMillis) {
            windowStartTime = now;
            requestCount = 0;
        }
        if (requestCount + tokens <= maxRequests) {
            requestCount += tokens;
            return true;
        }
        return false;
    }
}
