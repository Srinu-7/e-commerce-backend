package com.zosh.e_commerce.Controller;

import com.zosh.e_commerce.Exception.ProductNotFoundException;
import com.zosh.e_commerce.Model.Product;
import com.zosh.e_commerce.Repository.ProductRepository;
import com.zosh.e_commerce.Request.ProductRequest;
import com.zosh.e_commerce.ServiceInterface.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductService productService;

    private final ProductRepository productRepository;

    public AdminProductController(ProductService productService, ProductRepository productRepository) {

        this.productService = productService;

        this.productRepository = productRepository;

    }

    @PostMapping("/")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest) {

        Product product = productService.createProduct(productRequest);

        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) throws ProductNotFoundException {

        productService.deleteProduct(productId);

        return new ResponseEntity<>("Product deleted", HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> findAllProducts() {

        List<Product> productList = productRepository.findAll();

        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product product1) throws ProductNotFoundException {

        Product product = productService.updateProduct(productId,product1);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/creates")
    public ResponseEntity<String> createMultipleProducts(@RequestBody ProductRequest[] productRequests){

        for(ProductRequest productRequest : productRequests){
            productService.createProduct(productRequest);
        }

        return new ResponseEntity<>("All products created", HttpStatus.OK);
    }
}
