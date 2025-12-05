package com.hnguyen703.tp3observability.controllers;

import com.hnguyen703.tp3observability.models.Product;
import com.hnguyen703.tp3observability.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts(@RequestHeader("X-User-ID") String userId) {
        return productService.getAllProducts(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id, @RequestHeader("X-User-ID") String userId) {
        try {
            return ResponseEntity.ok(productService.getProductById(id, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Product product, @RequestHeader("X-User-ID") String userId) {
        try {
            return ResponseEntity.ok(productService.addProduct(product, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id, @RequestHeader("X-User-ID") String userId) {
        try {
            productService.deleteProduct(id, userId);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody Product product, @RequestHeader("X-User-ID") String userId) {
        try {
            return ResponseEntity.ok(productService.updateProduct(id, product, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
