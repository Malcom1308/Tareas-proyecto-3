package com.tarea.tarea1.demo.rest.product;


import com.tarea.tarea1.demo.logic.entity.category.Category;
import com.tarea.tarea1.demo.logic.entity.category.CategoryRepository;
import com.tarea.tarea1.demo.logic.entity.http.GlobalResponseHandler;
import com.tarea.tarea1.demo.logic.entity.product.Product;
import com.tarea.tarea1.demo.logic.entity.product.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        Category category = categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody Product product, HttpServletRequest request){
        Optional<Product> foundOrder = productRepository.findById(productId);
        if(foundOrder.isPresent()){
            product.setId(foundOrder.get().getId());
            productRepository.save(product);
            return new GlobalResponseHandler().handleResponse(
                    "Product update successfully",
                    product,
                    HttpStatus.OK,
                    request);
        } else{
            return new GlobalResponseHandler().handleResponse(
                    "Product id " + productId + " not found ",
                            HttpStatus.NOT_FOUND,
                            request);
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public void deleteProduct(@PathVariable Long id){
        productRepository.deleteById(id);
    }
}
