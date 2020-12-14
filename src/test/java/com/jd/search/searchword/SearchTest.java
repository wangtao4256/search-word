package com.jd.search.searchword;

import com.jd.search.searchword.entity.Product;
import com.jd.search.searchword.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class SearchTest {
    @Autowired
    ProductService productService;

    @Test
    public void addData() throws IOException {
        System.out.println(new Date());
        productService.addProducts("键盘", 100);
        System.out.println(new Date());
    }
}
