package com.example.bytebuy.Services;

import com.example.bytebuy.DTOs.CartItemResponse;
import com.example.bytebuy.DTOs.CartResponse;
import com.example.bytebuy.Entities.Cart;
import com.example.bytebuy.Entities.CartItem;
import com.example.bytebuy.Entities.Products;
import com.example.bytebuy.Entities.Users;
import com.example.bytebuy.Repositories.CartRepository;
import com.example.bytebuy.Repositories.ProductRepository;
import com.example.bytebuy.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public CartResponse addToCart(Long userId, Long productId, Integer quantity){
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return newCart;
        });

        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPriceAtThatTime(product.getPrice());
            cart.getCartItems().add(newItem);
        }

        double total = cart.getCartItems().stream()
                .mapToDouble(i -> i.getQuantity() * i.getPriceAtThatTime())
                .sum();
        cart.setTotalPrice(total);

        cartRepository.save(cart);

        return mapToResponse(cart,userId);
    }

    // ✅ Get cart for a user
    public CartResponse getCart(Long userId) {
        Cart cart = getCartEntity(userId);
        return mapToResponse(cart,userId);
    }

    // ✅ Remove product from cart
    public CartResponse removeFromCart(Long userId, Long productId) {
        Cart cart = getCartEntity(userId);

        cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));

        double total = cart.getCartItems().stream()
                .mapToDouble(i -> i.getQuantity() * i.getPriceAtThatTime())
                .sum();
        cart.setTotalPrice(total);

        cartRepository.save(cart);

        return mapToResponse(cart,userId);
    }

    // ✅ Helper: get cart entity for user
    private Cart getCartEntity(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
    }

    // ✅ Helper: map entity -> DTO
    private CartResponse mapToResponse(Cart cart,Long userId) {
        List<CartItemResponse> items = cart.getCartItems().stream()
                .map(item -> new CartItemResponse(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPriceAtThatTime()
                ))
                .toList();
        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        return new CartResponse(cart.getId(), cart.getTotalPrice(), items, user);
    }

}
