package api.controller;

import api.model.Product;
import api.repository.ProductRepository;
import api.util.TimestampHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.security.Timestamp;
import java.util.HashMap;

@RestController
@RequestMapping("/v1/products")
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    /**
     * Handles GET requests at /v1/products. If a category to search on is not specified,
     * all items from the database are retrieved.
     * @param category
     * @return JSON response detailing products from specified category (all if not specified)
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> findByCategory(
            @RequestParam(name="category", defaultValue="all") String category) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Product> products;
            
            if (category.equals("all")) {
                products = productRepository.findAll();
            } else {
                products = null;
            }
            
            response.put("status", HttpStatus.OK);
            response.put("data", products);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("error", e.getClass().getName());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles POST requests at /v1/products
     * @param product JSON object for a Product from RequestBody
     * @return JSON response detailing data inserted and HTTP status
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> insertProduct(@RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();
        try {
            product.setCreatedAt(TimestampHelper.getCurrentDate());
            Product newProduct = productRepository.save(product);

            response.put("status", HttpStatus.CREATED);
            response.put("data", newProduct);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("error", e.getClass().getName());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}