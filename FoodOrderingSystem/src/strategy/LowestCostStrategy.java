package strategy;

import model.Order ;
import model.Restaurant ;
import java.util.List;
import java.util.Optional;

public class LowestCostStrategy implements RestaurantSelectionStrategy {
    @Override
    public Restaurant selectRestaurant(Order order, List<Restaurant> availableRestaurants) {
        return availableRestaurants.stream()
                .filter(restaurant -> restaurant.hasAllItems(order.getItems()))
                .min((r1, r2) -> Double.compare(r1.calculateOrderCost(order.getItems()), r2.calculateOrderCost(order.getItems())))
                .orElse(null);
    }
}