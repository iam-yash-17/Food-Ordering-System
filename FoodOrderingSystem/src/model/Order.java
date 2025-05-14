package model;

import java.util.* ;

public class Order {
    private String id;
    private String userId;
    private Map<String, Integer> items;
    private String restaurantId;
    private OrderStatus status;

    public Order(String userId, Map<String, Integer> items) {
        // Input validations
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }
        if (items.values().stream().anyMatch(quantity -> quantity <= 0)) {
            throw new IllegalArgumentException("Item quantity must be greater than zero.");
        }
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.items = items;
        this.status = OrderStatus.PENDING;
        this.restaurantId = null;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
        this.status = OrderStatus.ACCEPTED;
    }

    public void markCompleted() {
        if (this.status != OrderStatus.ACCEPTED) {
            throw new IllegalStateException("Only ACCEPTED orders can be marked as COMPLETED.");
        }
        this.status = OrderStatus.COMPLETED;
    }

    public enum OrderStatus {
        PENDING,
        ACCEPTED,
        COMPLETED
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", items=" + items +
                ", restaurantId='" + restaurantId + '\'' +
                ", status=" + status +
                '}';
    }

}
