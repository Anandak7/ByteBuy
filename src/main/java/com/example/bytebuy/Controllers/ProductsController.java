package com.example.bytebuy.Controllers;

import com.example.bytebuy.DTOs.ProductDto;
import com.example.bytebuy.Entities.Products;
import com.example.bytebuy.Services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    ProductService productService;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getProductsById(@PathVariable Long id){
        try {
            Products p = productService.getById(id);
            return ResponseEntity.ok(p);
        }
        catch (EntityNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found with id:" + id);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Products> getProducts(){
        return productService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public Products createProduct(@RequestBody Products products){
        return productService.create(products);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/{id}",method = RequestMethod.PUT)
    public ResponseEntity<?> updateProduct(@PathVariable Long id,@Validated @RequestBody ProductDto products){
        try{
            Products p = productService.getById(id);
            return ResponseEntity.ok(productService.update(id,products));
        }
        catch (EntityNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found with id:" + id);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String deleteProduct(@PathVariable Long id){
        try {
            ResponseEntity.ok(productService.getById(id));
            productService.delete(id);
            return "Product with id: " + id + " was deleted";
        }
        catch (Exception ex) {
            return "" + ex;
        }
    }

}
