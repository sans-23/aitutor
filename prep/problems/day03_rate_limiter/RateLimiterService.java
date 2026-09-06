package prep.problems.day03_rate_limiter;

import java.util.concurrent.ConcurrentHashMap;
import java.time.Duration;
import java.util.Map;

public class RateLimiterService {
    Map<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();
    Map<String, Client> clients = new ConcurrentHashMap<>();
    Map<Tier, Quota> tierQuotas = new ConcurrentHashMap<>();
    RateLimiterFactory factory = new RateLimiterFactory();

    Quota defaultQuota = new Quota(100, Duration.ofMinutes(1), AlgorithmType.SLIDING_WINDOW);

    public boolean allowRequest(Request request, int tokens){
        Client client = clients.getOrDefault(request.getClientId(), new Client(request.getClientId(), Tier.FREE));
        Quota quota = tierQuotas.getOrDefault(client.getTier(), defaultQuota);
        RateLimiter limiter = rateLimiters.computeIfAbsent(request.getKey(), k -> factory.getRateLimiter(quota));
        return limiter.allowRequest(tokens);
    }

    public void registerClient(Client client){
        clients.put(client.getClientId(), client);
    }

    public void registerQuota(Tier tier, Quota quota){
        tierQuotas.put(tier, quota);
    }

    public void unregisterClient(String clientId){
        clients.remove(clientId);
    }

    public void unregisterQuota(Tier tier){
        tierQuotas.remove(tier);
    }
}
