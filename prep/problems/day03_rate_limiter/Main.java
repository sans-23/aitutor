package prep.problems.day03_rate_limiter;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        RateLimiterService rateLimiterService = new RateLimiterService();
        rateLimiterService.registerClient(new Client("client1", Tier.FREE));
        rateLimiterService.registerQuota(Tier.FREE, new Quota(10, Duration.ofSeconds(2), AlgorithmType.FIXED_WINDOW));

        for (int i = 0; i < 15; i++) {
            Request request = new Request("client1", "api1");
            System.out.println("Request " + (i + 1) + ": " + (rateLimiterService.allowRequest(request, 1) ? "Allowed" : "Denied"));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        rateLimiterService.registerClient(new Client("client2", Tier.ENTERPRISE));
        rateLimiterService.registerQuota(Tier.ENTERPRISE, new Quota(10, Duration.ofSeconds(2), AlgorithmType.SLIDING_WINDOW));

        CountDownLatch gun = new CountDownLatch(1);
        ExecutorService ex = Executors.newFixedThreadPool(10);

        AtomicInteger allowed = new AtomicInteger(0);
        AtomicInteger denied = new AtomicInteger(0);

        for(int i = 0; i<30; i++){
            ex.submit(() -> {
                try{
                    gun.await();
                    Request request = new Request("client2", "api1");
                    if(rateLimiterService.allowRequest(request, 1)){
                        allowed.incrementAndGet();
                    }else{
                        denied.incrementAndGet();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            });
        }  

        try {
            gun.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ex.shutdown();
        try {
            ex.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Allowed: " + allowed.get() + " Denied: " + denied.get());
    }
}
