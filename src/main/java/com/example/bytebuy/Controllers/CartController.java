package com.example.bytebuy.Controllers;


import com.example.bytebuy.DTOs.CartResponse;
import com.example.bytebuy.Entities.Cart;
import com.example.bytebuy.Services.CartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Add product to cart (Customer only)
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/add")
    public CartResponse addToCart(@RequestParam Long userId,
                                  @RequestParam Long productId,
                                  @RequestParam int quantity) {
        return cartService.addToCart(userId, productId, quantity);
    }

    // View cart
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public CartResponse viewCart(@RequestParam Long userId) {
        return cartService.getCart(userId);
    }

    // Remove product from cart
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/remove")
    public CartResponse removeFromCart(@RequestParam Long userId,
                                   @RequestParam Long productId) {
        return cartService.removeFromCart(userId, productId);
    }
}