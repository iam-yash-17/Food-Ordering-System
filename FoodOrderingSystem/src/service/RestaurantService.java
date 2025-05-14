package service;

import model.Restaurant ;
import model.MenuItem ;
import java.util.* ;

public class RestaurantService {
    private final Map<String, Restaurant> restaurants = new HashMap<>();

    public void onboardRestaurant(String name, int maxNoOfOrders, double rating, Map<String, Double> initialMenu) {
        if (restaurants.containsKey(name)) {
            throw new IllegalArgumentException("Restaurant with name '" + name + "' already exists.");
        }
        Restaurant restaurant = new Restaurant(name, maxNoOfOrders, rating, initialMenu);
        restaurants.put(restaurant.getId(), restaurant);
    }

    public Restaurant getRestaurantById(String id) {
        return restaurants.get(id);
    }

    public List<Restaurant> getAllRestaurants() {
        return new ArrayList<>(restaurants.values());
    }

    public void updateMenu(String restaurantId, String operation, String itemName, double price) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant with ID '" + restaurantId + "' not found.");
        }
        restaurant.updateMenu(operation, itemName, price);
    }

    public void markOrderCompleted(String restaurantId, String orderId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant with ID '" + restaurantId + "' not found.");
        }
        restaurant.markOrderCompleted(orderId);
    }
}
