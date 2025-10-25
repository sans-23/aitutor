package lld.problems.BookMyShow.sanskar;

import java.util.List;

public interface PricingStrategy {
    double calculatePrice(List<Seat> seats);
}
