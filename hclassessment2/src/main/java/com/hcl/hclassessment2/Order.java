package com.hcl.hclassassessment2;

import java.util.List;

public class Order {
    private Long orderId;
    private List<CartItem> items;
    private double totalAmount;
    private String status; // e.g., "Pending", "Cancelled"

    public Order(Long orderId, List<CartItem> items, double totalAmount, String status) {
        this.orderId = orderId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
