package service;

import model.Restaurant ;
import model.Order ;
import model.User ;
import strategy.RestaurantSelectionStrategy ;
import java.util.* ;

public class OrderService {
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final Map<String, Order> orders = new HashMap<>();
    private RestaurantSelectionStrategy selectionStrategy;

    public OrderService(RestaurantService restaurantService, UserService userService, RestaurantSelectionStrategy selectionStrategy) {
        this.restaurantService = restaurantService;
        this.selectionStrategy = selectionStrategy;
        this.userService = userService ;
    }

    public void setSelectionStrategy(RestaurantSelectionStrategy selectionStrategy) {
        this.selectionStrategy = selectionStrategy;
    }

    public String placeOrder(String userName, Map<String, Integer> items) {
        String userId = userService.createUser(userName) ;

        Order order = new Order(userId, items);
        List<Restaurant> availableRestaurants = restaurantService.getAllRestaurants().stream()
                .filter(Restaurant::canAcceptOrder)
                .toList();

        Restaurant assignedRestaurant = selectionStrategy.selectRestaurant(order, availableRestaurants);

        if (assignedRestaurant != null) {
            order.setRestaurantId(assignedRestaurant.getId());
            assignedRestaurant.addActiveOrder(order);
            orders.put(order.getId(), order);
            return order.getId();
        } else {
            throw new IllegalStateException("Cannot assign the order. No restaurant can fulfill all the items or has reached its capacity.");
        }
    }

    public Order getOrderById(String orderId) {
        return orders.get(orderId);
    }
}
