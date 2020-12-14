package com.jd.search.searchword.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.jd.search.searchword.entity.Product;
import com.jd.search.searchword.utils.HtmlParseUtil;
import org.bson.types.ObjectId;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;


@Service
public class ProductService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public Boolean addProducts(String keyword, Integer pageNo) throws IOException {
        List<Product> products = HtmlParseUtil.batchQueryJdProduct(keyword, pageNo);
        BulkRequest request = new BulkRequest();
        request.timeout(TimeValue.timeValueMillis(1000));
        for (Product product : products) {
            IndexRequest jdProduct = new IndexRequest("jd_product");
            jdProduct.id(new ObjectId().toString());
            jdProduct.source(JSON.toJSONString(product), XContentType.JSON);
            request.add(jdProduct);
        }
        BulkResponse response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        //返回是否都正常index到es中
        Boolean isSuccess = !response.hasFailures();
        return isSuccess;
    }


    public List<Product> searchProducts(String keyword) throws IOException {
        SearchRequest request = new SearchRequest("jd_product");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        List<Product> products = Lists.newArrayList();
        TermQueryBuilder termQuery = new TermQueryBuilder("productName", keyword);
        searchSourceBuilder.query(termQuery);

        request.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();

        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit product = iterator.next();

            Product search = JSON.parseObject(product.getSourceAsString(), Product.class);
            products.add(search);
        }
        return products;
    }
}
