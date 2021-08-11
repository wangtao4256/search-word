package com.jd.search.searchword.controller;

import com.jd.search.searchword.entity.Product;
import com.jd.search.searchword.mq.RabbitConfig;
import com.jd.search.searchword.service.ProductService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/search/{keyword}")
    public List<Product> search(@PathVariable("keyword") String keyword) throws IOException {
        return productService.searchProducts(keyword);
    }

    @RequestMapping("/send")
    public String send() {
        for (int i = 0; i < 20; i++) {
            String a = "test" + i;
            rabbitTemplate.convertAndSend(RabbitConfig.exchange, RabbitConfig.routing, a);
        }
        return "";
    }
}
