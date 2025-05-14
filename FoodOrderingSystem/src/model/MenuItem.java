package model;

import java.util.* ;

public class MenuItem {
    private String name ;
    private double price ;

    public MenuItem(String name, double price){
        if(name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Menu item name cannot be null or empty.") ;
        }
        if(price <= 0){
            throw new IllegalArgumentException("Menu item price must be greater than zero.") ;
        }
        this.name = name ;
        this.price = price ;
    }

    public String getName() {
        return name ;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Menu item price must be greater than zero.");
        }
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return Objects.equals(name, menuItem.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + ": INR " + price;
    }
}
