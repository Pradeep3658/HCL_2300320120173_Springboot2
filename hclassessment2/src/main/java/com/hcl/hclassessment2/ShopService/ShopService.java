package com.hcl.hclassassessment2;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShopService {

    private final List<Product> products = new ArrayList<>();
    private final List<CartItem> currentCart = new ArrayList<>();
    private final List<Order> orderHistory = new ArrayList<>();
    private long nextOrderId = 1;

    public ShopService() {
        products.add(new Product(1L, "Wireless Mouse", "Ergonomic 2.4GHz mouse", 29.99, 10));
        products.add(new Product(2L, "Mechanical Keyboard", "RGB Backlit blue switches", 89.99, 5));
        products.add(new Product(3L, "4K Monitor", "27-inch Ultra HD display", 349.99, 3));
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Product getProductById(Long id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public List<CartItem> getCart() {
        return currentCart;
    }

    public String addToCart(Long productId, int quantity) {
        Product product = getProductById(productId);
        if (product == null) return "Product not found.";
        if (product.getStock() < quantity) return "Insufficient stock available.";

        for (CartItem item : currentCart) {
            if (item.getProductId().equals(productId)) {
                if (product.getStock() < (item.getQuantity() + quantity)) {
                    return "Cannot add more. Total exceeds available stock.";
                }
                item.setQuantity(item.getQuantity() + quantity);
                return "Cart updated successfully.";
            }
        }

        currentCart.add(new CartItem(product.getId(), product.getName(), quantity, product.getPrice()));
        return "Product added to cart.";
    }

    public String removeFromCart(Long productId) {
        boolean removed = currentCart.removeIf(item -> item.getProductId().equals(productId));
        return removed ? "Product removed from selection." : "Item was not found in cart.";
    }

    public Order placeOrder() {
        if (currentCart.isEmpty()) return null;

        double total = 0;
        for (CartItem item : currentCart) {
            Product product = getProductById(item.getProductId());
            if (product == null || product.getStock() < item.getQuantity()) {
                return null;
            }
            total += item.getPrice() * item.getQuantity();
        }

        for (CartItem item : currentCart) {
            Product product = getProductById(item.getProductId());
            product.setStock(product.getStock() - item.getQuantity());
        }

        Order order = new Order(nextOrderId++, new ArrayList<>(currentCart), total, "Pending");
        orderHistory.add(order);
        currentCart.clear();
        return order;
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public String cancelOrder(Long orderId) {
        for (Order order : orderHistory) {
            if (order.getOrderId().equals(orderId)) {
                if (!"Pending".equals(order.getStatus())) {
                    return "Order cannot be cancelled. Status: " + order.getStatus();
                }

                for (CartItem item : order.getItems()) {
                    Product product = getProductById(item.getProductId());
                    if (product != null) {
                        product.setStock(product.getStock() + item.getQuantity());
                    }
                }
                order.setStatus("Cancelled");
                return "Order cancelled successfully.";
            }
        }
        return "Order not found.";
    }
}
