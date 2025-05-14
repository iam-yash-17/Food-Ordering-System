package model;

import java.util.* ;

public class Restaurant {
    private String id;
    private String name;
    private double rating;
    private int maxNoOfOrders;
    private Map<String, MenuItem> menu;
    private Map<String, Order> activeOrders;

    public Restaurant(String name, int maxNoOfOrders, double rating, Map<String, Double> initialMenu) {
        // Input validations
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name cannot be null or empty.");
        }
        if (maxNoOfOrders <= 0) {
            throw new IllegalArgumentException("Max number of orders must be greater than zero.");
        }
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.rating = rating;
        this.maxNoOfOrders = maxNoOfOrders;
        this.menu = new HashMap<>();
        this.activeOrders = new HashMap<>();
        if (initialMenu != null) {
            initialMenu.forEach((itemName, price) -> this.menu.put(itemName, new MenuItem(itemName, price)));
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public int getMaxNoOfOrders() {
        return maxNoOfOrders;
    }

    public Map<String, MenuItem> getMenu() {
        return menu;
    }

    public Map<String, Order> getActiveOrders() {
        return activeOrders;
    }

    public boolean canAcceptOrder() {
        return activeOrders.size() < maxNoOfOrders;
    }

    public boolean hasAllItems(Map<String, Integer> items) {
        return items.keySet().stream().allMatch(menu::containsKey);
    }

    public double calculateOrderCost(Map<String, Integer> items) {
        return items.entrySet().stream()
                .mapToDouble(entry -> menu.get(entry.getKey()).getPrice() * entry.getValue())
                .sum();
    }

    public void updateMenu(String operation, String itemName, double price) {
        // Only ADD and UPDATE operations are allowed
        if ("ADD".equalsIgnoreCase(operation)) {
            if (menu.containsKey(itemName)) {
                throw new IllegalArgumentException("Item '" + itemName + "' already exists in the menu. Use UPDATE to change the price.");
            }
            menu.put(itemName, new MenuItem(itemName, price));
        } else if ("UPDATE".equalsIgnoreCase(operation)) {
            if (!menu.containsKey(itemName)) {
                throw new IllegalArgumentException("Item '" + itemName + "' does not exist in the menu.");
            }
            menu.get(itemName).setPrice(price);
        } else {
            throw new IllegalArgumentException("Invalid menu update operation: " + operation + ". Allowed operations are ADD and UPDATE.");
        }
    }

    public void markOrderCompleted(String orderId) {
        if (!activeOrders.containsKey(orderId)) {
            throw new IllegalArgumentException("Order with ID '" + orderId + "' is not currently active in this restaurant.");
        }
        activeOrders.remove(orderId);
    }

    public void addActiveOrder(Order order) {
        if (activeOrders.size() >= maxNoOfOrders) {
            throw new IllegalStateException("Restaurant '" + name + "' has reached its maximum order capacity.");
        }
        activeOrders.put(order.getId(), order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", maxNoOfOrders=" + maxNoOfOrders +
                ", menu=" + menu +
                ", activeOrders=" + activeOrders.keySet() +
                '}';
    }
}
