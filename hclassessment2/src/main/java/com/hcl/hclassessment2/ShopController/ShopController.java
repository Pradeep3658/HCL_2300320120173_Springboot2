
package com.hcl.hclassassessment2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    // 1. Browse Products
    @GetMapping("/products")
    public List<Product> browseProducts() {
        return shopService.getAllProducts();
    }

    // 2. View Active Cart
    @GetMapping("/cart")
    public List<CartItem> viewCart() {
        return shopService.getCart();
    }

    // 3. Add Item to Product Selection (Cart)
    @PostMapping("/cart")
    public ResponseEntity<String> addItemToCart(@RequestBody Map<String, Object> payload) {
        Long productId = Long.valueOf(payload.get("productId").toString());
        int quantity = Integer.parseInt(payload.get("quantity").toString());

        String response = shopService.addToCart(productId, quantity);
        if (response.contains("successfully") || response.contains("added")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 4. Cancel/Remove Item From Cart Selection
    @DeleteMapping("/cart/{productId}")
    public ResponseEntity<String> removeItemFromCart(@PathVariable Long productId) {
        String response = shopService.removeFromCart(productId);
        return ResponseEntity.ok(response);
    }

    // 5. Place New Order
    @PostMapping("/orders")
    public ResponseEntity<?> placeOrder() {
        Order completedOrder = shopService.placeOrder();
        if (completedOrder == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to place order. Check stock availability or cart layout.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(completedOrder);
    }

    // 6. View Order History Lifecycle
    @GetMapping("/orders")
    public List<Order> viewOrderHistory() {
        return shopService.getOrderHistory();
    }

    // 7. Cancel Finalized Order
    @PostMapping("/orders/{orderId}/cancel")
    public ResponseEntity<String> cancelActiveOrder(@PathVariable Long orderId) {
        String response = shopService.cancelOrder(orderId);
        if (response.contains("successfully")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
