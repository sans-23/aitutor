package prep.problems.day03_rate_limiter;

import java.time.Duration;

public record Quota(
    long capacity, 
    Duration windowDuration, 
    AlgorithmType algo
) {
    public double getRefillRatePerSecond() {
        return (double) capacity / windowDuration.toSeconds();
    }
    public long getWindowSizeInMillis() {
        return windowDuration.toMillis();
    }
}
