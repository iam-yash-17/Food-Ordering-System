package strategy;

import model.Order ;
import model.Restaurant ;
import java.util.List;
import java.util.Optional;

public class HighestRatingStrategy implements RestaurantSelectionStrategy {
    @Override
    public Restaurant selectRestaurant(Order order, List<Restaurant> availableRestaurants) {
        return availableRestaurants.stream()
                .filter(restaurant -> restaurant.hasAllItems(order.getItems()))
                .max((r1, r2) -> Double.compare(r1.getRating(), r2.getRating()))
                .orElse(null);
    }
}
