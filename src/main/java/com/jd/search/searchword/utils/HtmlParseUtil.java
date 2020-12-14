package com.jd.search.searchword.utils;

import com.google.common.collect.Lists;
import com.jd.search.searchword.entity.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class HtmlParseUtil {
    /**
     * 批量查询京东商品
     *
     * @param keyword
     * @param pageNo
     * @throws IOException
     */
    public static List<Product> batchQueryJdProduct(String keyword, Integer pageNo) throws IOException {
        List<Product> pageProduct = Lists.newArrayList();
        for (int a = 1; a <= pageNo; a++) {
            pageProduct.addAll(getJdProductInfo(keyword, a));
        }
        return pageProduct;
    }


    private static List<Product> getJdProductInfo(String keyword, Integer page) throws IOException {
        //https://search.jd.com/Search?keyword=%E7%BA%B8%E5%B0%BF%E8%A3%A4&qrst=1&stock=1&page=7&s=176&click=1


        String url = "https://search.jd.com/Search?keyword=" + keyword + "&page=" + page;
        Document document = Jsoup.parse(new URL(url), 30000);

        Element element = document.getElementById("J_goodsList");
        Elements elementList = element.getElementsByTag("li");

        List<Product> productList = Lists.newArrayList();
        for (Element element1 : elementList) {
            Product product = new Product();
            String price = element1.getElementsByClass("p-price").eq(0).text();
            String productName = element1.getElementsByClass("p-name").eq(0).text();
            product.setPrice(price);
            product.setProductName(productName);
            productList.add(product);
        }
        return productList;
    }
}
