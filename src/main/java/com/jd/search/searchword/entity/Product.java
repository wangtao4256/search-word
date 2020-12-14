package com.jd.search.searchword.entity;

import lombok.Data;

@Data
public class Product {
    private String productName;
    private String price;
    private String img;
    //店铺
    private String shop;
}
