package com.jd.search.searchword;

import com.google.common.collect.Lists;
import com.jd.search.searchword.config.EsConfig;
import com.jd.search.searchword.config.MongoConfig;
import com.jd.search.searchword.entity.UserDO;
import com.jd.search.searchword.mq.RabbitConfig;
import com.jd.search.searchword.service.ProductService;
import org.apache.commons.validator.routines.EmailValidator;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Autowired
    private EsConfig esConfig;

    @Autowired
    private RabbitTemplate searchTemplate;

    @Test
    public void addData() throws IOException {

        Criteria criteria = Criteria.where("name").is("hanyamin");
        mongoTemplate = mongoConfig.getSpecialMongoTemplate("seal");
        Query query = new Query(criteria);
        query.fields().include("name");
        List<UserDO> su = mongoTemplate.find(query, UserDO.class);
        System.out.println(esConfig.getHostname());
    }

    @Test
    public void teachPlanBatchDelete() {
        List<String> allName = Lists.newArrayList();
        List<String> emailList = Lists.newArrayList();
        List<String> chineseList = Lists.newArrayList();
        EmailValidator emailValidator = EmailValidator.getInstance();
        for (String name : allName) {
            if (null == name) {
                continue;
            }
            boolean result = emailValidator.isValid(name) ? emailList.add(name) : chineseList.add(name);
        }
        System.out.println(1);
    }

    @Test
    public void test() {
        String a = "test";
        searchTemplate.convertAndSend(RabbitConfig.exchange, RabbitConfig.routing, a);

    }
}
