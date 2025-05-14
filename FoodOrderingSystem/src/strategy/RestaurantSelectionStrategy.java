package strategy;

import model.Order ;
import model.Restaurant ;
import java.util.List;

public interface RestaurantSelectionStrategy {
    Restaurant selectRestaurant(Order order, List<Restaurant> availableRestaurants);
}
