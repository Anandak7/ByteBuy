package com.example.bytebuy.Services;

import com.example.bytebuy.DTOs.ProductDto;
import com.example.bytebuy.Entities.Products;
import com.example.bytebuy.Repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Products> getAll(){
        return productRepository.findAll();
    }

    public Products getById(Long id){
        return productRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Product not found with id: " + id));
    }
    public  Products create(Products products){
        return productRepository.save(products);
    }

    public Products update(Long id, ProductDto product) {
        Products existing = getById(id);
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        return productRepository.save(existing);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
