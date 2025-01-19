package com.zosh.e_commerce.Controller;

import com.zosh.e_commerce.Exception.ProductNotFoundException;
import com.zosh.e_commerce.Model.Product;
import com.zosh.e_commerce.ServiceInterface.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {

        this.productService = productService;

    }

    @GetMapping("/")
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(@RequestParam String category,@RequestParam List<String> colors,
                                                                      @RequestParam List<String> size,@RequestParam Integer minPrice,
                                                                      @RequestParam Integer maxPrice,@RequestParam Integer minDiscount,
                                                                      @RequestParam String sort,@RequestParam String stock,
                                                                      @RequestParam Integer pageNumber,@RequestParam Integer pageSize) {

          Page<Product> res = productService.getAllProductS(category,colors,size,minPrice,maxPrice,minDiscount,sort,stock,pageNumber,pageSize);

          return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @GetMapping("/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId )  throws ProductNotFoundException {

        Product product = productService.findProductById(productId);

        return new ResponseEntity<>(product,HttpStatus.ACCEPTED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductHandler(@RequestParam String q){

          List<Product> products = productService.findAllProductByCategory(q);

          return new ResponseEntity<>(products,HttpStatus.OK);
    }
}
