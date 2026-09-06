package prep.problems.day03_rate_limiter;

import java.util.Deque;
import java.util.LinkedList;

public class SlidingWindowRateLimiter implements RateLimiter {
    private final long windowSizeInMillis;
    private final long maxRequests;
    private final Deque<Long> requestTimestamps;

    public SlidingWindowRateLimiter(long maxRequests, long windowSizeInMillis) {
        this.windowSizeInMillis = windowSizeInMillis;
        this.maxRequests = maxRequests;
        this.requestTimestamps = new LinkedList<>();
    }

    @Override
    public synchronized boolean allowRequest(int tokens) {
        long now = System.currentTimeMillis();

        // Remove timestamps outside the sliding window
        while (!requestTimestamps.isEmpty() && now - requestTimestamps.peekFirst() >= windowSizeInMillis) {
            requestTimestamps.pollFirst();
        }

        // Check if we have capacity
        if (requestTimestamps.size() + tokens <= maxRequests) {
            for(int i=0; i<tokens; i++){
                requestTimestamps.offerLast(now);
            }
            return true;
        }

        return false;
    }
}
