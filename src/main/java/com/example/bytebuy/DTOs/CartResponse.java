package com.example.bytebuy.DTOs;

import com.example.bytebuy.Entities.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private Long cartId;
    private Double totalPrice;
    private List<CartItemResponse> items;
    private Users user;
}