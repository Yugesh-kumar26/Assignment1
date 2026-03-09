import java.util.*;

public class Assignment1 {

    class TokenBucket {
        int tokens;
        int maxTokens;
        long lastRefillTime;

        TokenBucket(int maxTokens) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.lastRefillTime = System.currentTimeMillis();
        }
    }

    private HashMap<String, TokenBucket> clients = new HashMap<>();
    private int limit = 1000;
    private long window = 3600000;

    public synchronized String checkRateLimit(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            bucket = new TokenBucket(limit);
            clients.put(clientId, bucket);
        }

        long now = System.currentTimeMillis();

        if (now - bucket.lastRefillTime >= window) {
            bucket.tokens = bucket.maxTokens;
            bucket.lastRefillTime = now;
        }

        if (bucket.tokens > 0) {
            bucket.tokens--;
            return "Allowed (" + bucket.tokens + " requests remaining)";
        } else {
            long retry = (window - (now - bucket.lastRefillTime)) / 1000;
            return "Denied (0 requests remaining, retry after " + retry + "s)";
        }
    }

    public String getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            return "{used: 0, limit: " + limit + "}";
        }

        int used = bucket.maxTokens - bucket.tokens;
        long reset = bucket.lastRefillTime + window;

        return "{used: " + used + ", limit: " + bucket.maxTokens + ", reset: " + reset + "}";
    }
}