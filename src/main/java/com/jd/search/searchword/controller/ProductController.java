package com.jd.search.searchword.controller;

import com.jd.search.searchword.entity.Product;
import com.jd.search.searchword.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
public class ProductController {

    private ProductService productService;

    @GetMapping("/search/{keyword}")
    public List<Product> search(@PathVariable("keyword") String keyword) throws IOException {
        return productService.searchProducts(keyword);
    }
}
