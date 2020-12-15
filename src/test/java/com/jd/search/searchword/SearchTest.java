package com.jd.search.searchword;

import com.jd.search.searchword.config.MongoConfig;
import com.jd.search.searchword.entity.UserDO;
import com.jd.search.searchword.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class SearchTest {
    @Autowired
    ProductService productService;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private MongoConfig mongoConfig;

    @Test
    public void addData() throws IOException {

        Criteria criteria = Criteria.where("name").is("hanyamin");
        mongoTemplate = mongoConfig.getSpecialMongoTemplate("seal");
        Query query = new Query(criteria);
        query.fields().include("name");
        List<UserDO> su = mongoTemplate.find(query, UserDO.class);
        System.out.println(1);
    }
}
