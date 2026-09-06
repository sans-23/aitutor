package prep.problems.day03_rate_limiter;

public class Client {
    private final String clientId;
    private final Tier tier;

    public Client(String clientId, Tier tier) {
        this.clientId = clientId;
        this.tier = tier;
    }

    public String getClientId() {
        return clientId;
    }

    public Tier getTier() {
        return tier;
    }
}
