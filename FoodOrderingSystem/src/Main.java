import model.Order;
import model.Restaurant;
import service.OrderService;
import service.RestaurantService;
import service.UserService ;
import strategy.HighestRatingStrategy;
import strategy.LowestCostStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Main {

    private static RestaurantService restaurantService = new RestaurantService();
    private static UserService userService = new UserService();
    private static OrderService orderService = new OrderService(restaurantService, userService, new LowestCostStrategy());
    private static Map<String, Restaurant> restaurantsByName = new HashMap<>();

    public static void main(String[] args) {
        List<Consumer<Void>> scenarios = new ArrayList<>();
        scenarios.add(Main::scenario1);
        scenarios.add(Main::scenario2);
        scenarios.add(Main::scenario3);
        scenarios.add(Main::scenario4);
        scenarios.add(Main::scenario5);

        scenarios.forEach(scenario -> {
            restaurantService = new RestaurantService();
            userService = new UserService();
            orderService = new OrderService(restaurantService, userService, new LowestCostStrategy());
            restaurantsByName.clear();
            scenario.accept(null);
            System.out.println("\n--- Scenario Completed ---\n");
        });
    }

    private static Restaurant onboard(String name, int maxOrders, double rating, Map<String, Double> menu) {
        restaurantService.onboardRestaurant(name, maxOrders, rating, menu);
        Restaurant r = restaurantService.getAllRestaurants().stream().filter(rest -> rest.getName().equals(name)).findFirst().orElse(null);
        restaurantsByName.put(name, r);
        System.out.println("Onboarded Restaurant: " + name + " (Capacity: " + maxOrders + ")");
        return r;
    }

    private static void updateMenu(String restaurantName, String operation, String itemName, double price) {
        Restaurant r = restaurantsByName.get(restaurantName);
        if (r != null) {
            restaurantService.updateMenu(r.getId(), operation, itemName, price);
            System.out.println("Updated " + restaurantName + " Menu: " + r.getMenu());
        }
    }

    private static Order placeOrder(String userName, Map<String, Integer> items, String selectionStrategy) {
        if ("Highest Rating".equalsIgnoreCase(selectionStrategy)) {
            orderService.setSelectionStrategy(new HighestRatingStrategy());
        } else {
            orderService.setSelectionStrategy(new LowestCostStrategy());
        }
        Order order = null;
        try {
            String orderId = orderService.placeOrder(userName, items);
            order = orderService.getOrderById(orderId);
            if (order != null && order.getRestaurantId() != null) {
                String assignedRestaurantName = restaurantService.getRestaurantById(order.getRestaurantId()).getName();
                System.out.println("Order by " + userName + " assigned to: " + assignedRestaurantName + " (Strategy: " + selectionStrategy + ")");
            } else {
                System.out.println("Order by " + userName + " could not be assigned (Strategy: " + selectionStrategy + ")");
            }
        } catch (IllegalStateException e) {
            System.out.println("Order by " + userName + " could not be placed: " + e.getMessage() + " (Strategy: " + selectionStrategy + ")");
        }
        return order;
    }

    private static void markComplete(String restaurantName, Order order) {
        Restaurant r = restaurantsByName.get(restaurantName);
        if (r != null && order != null) {
            restaurantService.markOrderCompleted(r.getId(), order.getId());
            System.out.println(restaurantName + " marked order " + order.getId() + " as COMPLETED");
        }
    }

    private static void scenario1(Void unused) {
        System.out.println("--- Scenario 1: Basic Order Placement ---");
        onboard("R1", 5, 4.5, Map.of("Veg Biryani", 100.0, "Chicken Biryani", 150.0));
        onboard("R2", 5, 4.0, Map.of("Idli", 10.0, "Dosa", 50.0, "Veg Biryani", 80.0, "Chicken Biryani", 175.0));
        onboard("R3", 1, 4.9, Map.of("Idli", 15.0, "Dosa", 30.0, "Gobi Manchurian", 150.0, "Chicken Biryani", 175.0));
        placeOrder("Ashwin", Map.of("Idli", 3, "Dosa", 1), "Lowest Cost"); // Expected: R3
    }

    private static void scenario2(Void unused) {
        System.out.println("--- Scenario 2: Restaurant Capacity Check ---");
        onboard("R2", 1, 4.0, Map.of("Idli", 10.0, "Dosa", 50.0));
        placeOrder("Harish1", Map.of("Idli", 1, "Dosa", 1), "Lowest Cost"); // Fills R2
        placeOrder("Harish2", Map.of("Idli", 1, "Dosa", 1), "Lowest Cost"); // Expected: Cannot assign, handled by try-catch
    }

    private static void scenario3(Void unused) {
        System.out.println("--- Scenario 3: Highest Rating Strategy ---");
        onboard("R1", 5, 4.5, Map.of("Veg Biryani", 100.0, "Dosa", 60.0));
        onboard("R2", 5, 4.0, Map.of("Veg Biryani", 80.0, "Dosa", 50.0));
        placeOrder("Shruthi", Map.of("Veg Biryani", 1, "Dosa", 1), "Highest Rating"); // Expected: R1
    }

    private static void scenario4(Void unused) {
        System.out.println("--- Scenario 4: Order Completion and Re-booking ---");
        onboard("R3", 1, 4.9, Map.of("Idli", 15.0, "Dosa", 30.0));
        Order order1 = placeOrder("Ashwin", Map.of("Idli", 1, "Dosa", 1), "Lowest Cost"); // Assigned to R3
        markComplete("R3", order1);
        placeOrder("Harish", Map.of("Idli", 1, "Dosa", 1), "Lowest Cost"); // Expected: R3 again
    }

    private static void scenario5(Void unused) {
        System.out.println("--- Scenario 5: Items Not Present ---");
        onboard("R1", 5, 4.5, Map.of("Veg Biryani", 100.0));
        onboard("R2", 5, 4.0, Map.of("Idli", 10.0, "Dosa", 50.0));
        placeOrder("Diya", Map.of("Idli", 1, "Paneer Tikka", 1), "Lowest Cost"); // Expected: Cannot assign, handled by try-catch
    }
}