package api.controller;

import api.model.Product;
import api.repository.ProductRepository;
import api.util.TimestampHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.validation.annotation.Validated;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Min;

import java.util.List;
import java.util.Map;
import java.security.Timestamp;
import java.util.HashMap;

@RestController
@Validated
@RequestMapping("/v1/products")
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    /**
     * Handles GET requests at /v1/products. If a category to search on is not specified,
     * all items from the database are retrieved.
     * @param category
     * @return JSON response detailing products from specified category (all if not specified)
     *         in order from newest to oldest.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> findAllByCategory(
            @RequestParam(name="category", defaultValue="all") @Valid @Pattern(regexp="[^0-9]*") String category,
            @RequestParam(name="page", defaultValue="1") @Valid @Min(1) Integer pageNum,
            @RequestParam(name="max", defaultValue="10") @Valid @Min(0) Integer maxEntries) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Product> products;

            // For the client's convenience, have the pageNum query parameter
            // start at 1, since this is more intuitive to the user.
            // An offset of -1 is used for correct pagination according to the Spring Data API.
            Pageable productPage = PageRequest.of(
                pageNum-1, 
                maxEntries, 
                Sort.by("createdAt").descending()
            );
            
            Page<Product> p;
            if (category.equals("all")) {
                p = productRepository.findAll(productPage);
            } else {
                p = productRepository.findAllByCategory(category, productPage);
            }
            products = p.getContent();

            response.put("status", HttpStatus.OK);
            response.put("data", products);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("error", e.getClass().getName());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles POST requests at /v1/products
     * @param product JSON object for a Product from RequestBody
     * @return JSON response detailing data inserted and HTTP response status
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
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}