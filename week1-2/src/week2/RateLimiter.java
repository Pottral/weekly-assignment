package week2;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class RateLimiter {

    // 1. The Token Bucket Structure (Inner Class)
    static class TokenBucket {
        private final long maxTokens;
        private final long refillRatePerSecond;
        private final AtomicLong currentTokens;
        private final AtomicLong lastRefillTimestamp;

        public TokenBucket(long limit, long windowSeconds) {
            this.maxTokens = limit;
            // Ensure refill rate is at least 1 if limit > 0
            this.refillRatePerSecond = Math.max(1, limit / windowSeconds);
            this.currentTokens = new AtomicLong(limit);
            this.lastRefillTimestamp = new AtomicLong(System.currentTimeMillis());
        }

        public synchronized boolean allowRequest() {
            refill();
            if (currentTokens.get() > 0) {
                currentTokens.decrementAndGet();
                return true;
            }
            return false;
        }

        private void refill() {
            long now = System.currentTimeMillis();
            long lastRefill = lastRefillTimestamp.get();
            long deltaMs = now - lastRefill;

            // Calculate how many tokens should be added based on time passed
            long tokensToAdd = (deltaMs / 1000) * refillRatePerSecond;

            if (tokensToAdd > 0) {
                long newVal = Math.min(maxTokens, currentTokens.get() + tokensToAdd);
                currentTokens.set(newVal);
                lastRefillTimestamp.set(now);
            }
        }

        public long getRemaining() {
            return currentTokens.get();
        }

        public long getResetTimeSeconds() {
            return 3600 - ((System.currentTimeMillis() - lastRefillTimestamp.get()) / 1000);
        }
    }

    // 2. Client Management
    private final Map<String, TokenBucket> clientBuckets = new ConcurrentHashMap<>();
    private final long LIMIT = 1000;
    private final long WINDOW_SECONDS = 3600; // 1 hour

    public String checkRateLimit(String clientId) {
        TokenBucket bucket = clientBuckets.computeIfAbsent(clientId,
                k -> new TokenBucket(LIMIT, WINDOW_SECONDS));

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.getRemaining() + " requests remaining)";
        } else {
            return "Denied (0 requests remaining, retry after " + bucket.getResetTimeSeconds() + "s)";
        }
    }

    // 3. Main Method
    public static void main(String[] args) {
        RateLimiter limiter = new RateLimiter();
        String testClient = "abc123";

        System.out.println("--- Testing Burst Traffic ---");
        for (int i = 0; i < 5; i++) {
            System.out.println(limiter.checkRateLimit(testClient));
        }

        System.out.println("\n--- Testing Limit Exceeded ---");
        TokenBucket bucket = limiter.clientBuckets.get(testClient);
        if (bucket != null) {
            bucket.currentTokens.set(0); // Manually draining for demonstration
        }
        System.out.println(limiter.checkRateLimit(testClient));
    }
}