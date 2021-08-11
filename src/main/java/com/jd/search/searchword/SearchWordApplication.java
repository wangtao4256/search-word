package com.jd.search.searchword;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class SearchWordApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchWordApplication.class, args);
    }

}
