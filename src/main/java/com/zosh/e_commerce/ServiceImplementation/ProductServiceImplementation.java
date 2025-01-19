package com.zosh.e_commerce.ServiceImplementation;

import com.zosh.e_commerce.Exception.ProductNotFoundException;
import com.zosh.e_commerce.Model.Category;
import com.zosh.e_commerce.Model.Product;
import com.zosh.e_commerce.Repository.CategoryRepository;
import com.zosh.e_commerce.Repository.ProductRepository;
import com.zosh.e_commerce.Request.ProductRequest;
import com.zosh.e_commerce.ServiceInterface.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements ProductService {

    private CategoryRepository categoryRepository;

    private ProductRepository productRepository;

    public ProductServiceImplementation(CategoryRepository categoryRepository, ProductRepository productRepository) {

        this.categoryRepository = categoryRepository;

        this.productRepository = productRepository;

    }

    @Override
    public Product createProduct(ProductRequest productRequest) {

        Category topLevel = categoryRepository.findByName(productRequest.getTopLavelCategory());

        if(topLevel == null) {

            Category topLevelCategory = new Category();

            topLevelCategory.setName(productRequest.getTopLavelCategory());

            topLevelCategory.setLevel(1);

            topLevel = categoryRepository.save(topLevelCategory);
        }

        Category secondLevel = categoryRepository.findByNameAndParent(productRequest.getSecondLavelCategory(),topLevel);

        if(secondLevel == null) {

            Category secondLevelCategory = new Category();

            secondLevelCategory.setName(productRequest.getSecondLavelCategory());

            secondLevelCategory.setLevel(2);

            secondLevel = categoryRepository.save(secondLevelCategory);

        }

        Category thirdLevel = categoryRepository.findByNameAndParent(productRequest.getThirdLavelCategory(), secondLevel);

        if(thirdLevel == null) {

            Category thirdLevelCategory = new Category();

            thirdLevelCategory.setName(productRequest.getThirdLavelCategory());

            thirdLevelCategory.setLevel(3);

            thirdLevel = categoryRepository.save(thirdLevelCategory);
        }

        Product product = new Product();

        product.setDescription(productRequest.getDescription());

        product.setPrice(productRequest.getPrice());

        product.setSize(productRequest.getSize());

        product.setQuantity(productRequest.getQuantity());

        product.setTitle(productRequest.getTitle());

        product.setColor(productRequest.getColor());

        product.setCreatedAt(LocalDateTime.now());

        product.setCategory(thirdLevel);

        product.getCategory().setParentCategory(topLevel);

        product.setBrand(productRequest.getBrand());

        product.setDiscountedPrice((productRequest.getDiscountedPrice()));

        product.setDiscountPercentage(productRequest.getDiscountPersent());

        product.setImageUrl(productRequest.getImageUrl());

        Product savedProduct = productRepository.save(product);

        return savedProduct;
    }

    @Override
    public Product findProductById(Long productId) throws ProductNotFoundException {

        Product product = productRepository.findById(productId).get();

        if(product == null) throw new ProductNotFoundException("Product not found with id " + productId);

        return product;
    }

    @Override
    public Page<Product> getAllProductS(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<Product> productList = productRepository.filterProducts(category,minPrice,maxPrice,minDiscount,sort);

        if(colors.size() > 0){

            productList = productList.stream().filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor()))).collect(Collectors.toList());
        }

        if(stock != null){

            if(stock.equals("In Stock")){
                productList = productList.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
            }

            else if(stock.equals("Out of Stock")){
                productList = productList.stream().filter(p -> p.getQuantity() == 0).collect(Collectors.toList());
            }
        }

        int startIndex = (int)pageable.getOffset();

        int endIndex = Math.min(startIndex+pageable.getPageSize(), productList.size());

        List<Product> pageContent = productList.subList(startIndex, endIndex);

        Page<Product> filteredProducts = new PageImpl<>(pageContent, pageable, productList.size());

        return filteredProducts;
    }

    @Override
    public Product updateProduct(Long productId, Product productRequest) throws ProductNotFoundException {

        Product product = findProductById(productId);

        if(productRequest.getQuantity() != 0) product.setQuantity(productRequest.getQuantity());

        productRepository.save(product);

        return product;
    }

    @Override
    public String deleteProduct(Long productId) throws ProductNotFoundException {

        Product product = findProductById(productId);

        product.getSize().clear();

        productRepository.deleteById(productId);

        return "Product is deleted succesfully";
    }

    @Override
    public List<Product> findAllProductByCategory(String category) {

        return productRepository.findByCategory(category);

    }

}
