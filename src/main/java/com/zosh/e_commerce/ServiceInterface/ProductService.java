package com.zosh.e_commerce.ServiceInterface;

import com.zosh.e_commerce.Exception.ProductNotFoundException;
import com.zosh.e_commerce.Model.Product;
import com.zosh.e_commerce.Request.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    public Product createProduct(ProductRequest productRequest);

    public String deleteProduct(Long productId) throws ProductNotFoundException;

    public Product updateProduct(Long productId, Product product) throws ProductNotFoundException;

    public Product findProductById(Long productId) throws ProductNotFoundException;

    public List<Product> findAllProductByCategory(String category);

    public Page<Product>  getAllProductS(String category,List<String> colors,List<String> sizes,Integer minPrice,Integer maxPrice,
                                         Integer minDiscount,String sort,String stock,
                                         Integer pageNumber,Integer pageSize);

}
