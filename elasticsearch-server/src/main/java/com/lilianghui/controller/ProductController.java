package com.lilianghui.controller;

import com.lilianghui.entity.Product;
import com.lilianghui.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "add")
    public Product add(Product product) {
        return productRepository.save(product);
    }

    @RequestMapping(value = "update/{productId}")
    public Product update(@PathVariable("productId") Long productId, Product product) {
        product.setId(productId);
        product=productRepository.save(product);
        return product;
    }

    @RequestMapping(value = "/{productId}")
    public void delete(@PathVariable("productId") Long productId) {
        productRepository.delete(Product.builder().id(productId).build());
    }

    @RequestMapping(value = "query")
    public List<Product> search(String query) {
        return productRepository.findByNameOrDescription(query);
    }

}
