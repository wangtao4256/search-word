package com.jd.search.searchword.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.jd.search.searchword.config.EsConfig;
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
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service
public class ProductService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    EsConfig esConfig;

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

        MatchQueryBuilder matchQuery = new MatchQueryBuilder("productName", keyword);
        searchSourceBuilder.query(matchQuery);
        searchSourceBuilder.query(QueryBuilders.regexpQuery("productName", ".*" + keyword + ".*"));

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("productName");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

        request.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();

        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit product = iterator.next();

            Product search = JSON.parseObject(product.getSourceAsString(), Product.class);
            Map<String, HighlightField> highlightFields = product.getHighlightFields();
            HighlightField productNameHigh = highlightFields.get("productName");
            StringBuilder builder = new StringBuilder();
            if (null != productNameHigh) {
                Text[] fragments = productNameHigh.fragments();
                for (Text fragment : fragments) {
                    builder.append(fragment);
                }
            }
            search.setProductName(builder.toString());
            products.add(search);
        }
        return products;
    }
}
