package lld.problems.BookMyShow.sanskar;

import java.util.List;

public class DefaultPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(List<Seat> seats) {
        double totalPrice = 0;
        for (Seat seat : seats) {
            totalPrice += seat.price;
        }
        return totalPrice;
    }
}
