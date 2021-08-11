package com.jd.search.searchword.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class EsConfig {
    private String hostname;
}
