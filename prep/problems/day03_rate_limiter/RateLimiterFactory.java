package prep.problems.day03_rate_limiter;

public class RateLimiterFactory {

    public RateLimiter getRateLimiter(Quota quota){
        return switch(quota.algo()){
            case FIXED_WINDOW -> new FixedWindowRateLimiter(quota.capacity(), quota.getWindowSizeInMillis());
            case SLIDING_WINDOW -> new SlidingWindowRateLimiter(quota.capacity(), quota.getWindowSizeInMillis());
            case TOKEN_BUCKET -> new TokenBucketRateLimiter(quota.capacity(), quota.getRefillRatePerSecond());
            default -> throw new IllegalArgumentException("Unknown algorithm type: " + quota.algo());
        };
    }
}
