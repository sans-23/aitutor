package prep.problems.day03_rate_limiter;

public class Request {
    private final String clientId;
    private final String apiId;
    private final long timestamp;

    public Request(String clientId, String apiId) {
        this(clientId, apiId, System.currentTimeMillis());
    }

    public Request(String clientId, String apiId, long timestamp) {
        this.clientId = clientId;
        this.apiId = apiId;
        this.timestamp = timestamp;
    }

    public String getClientId() {
        return clientId;
    }

    public String getApiId() {
        return apiId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getKey(){
        return clientId + ":" + apiId;
    }
}
