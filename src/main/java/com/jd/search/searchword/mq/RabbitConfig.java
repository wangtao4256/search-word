package com.jd.search.searchword.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String exchange = "search";

    public static final String routing = "search-routing";

    public final static String queue = "search-queue";
    public static final String VHost = "search-word";

    @Bean
    public DirectExchange topicExchange() {
        return new DirectExchange(RabbitConfig.exchange);
    }

    @Bean
    public Queue queue() {
        // 创建一个持久化的队列
        return new Queue(RabbitConfig.queue, true);
    }

    @Bean
    public Binding binding(DirectExchange topicExchange, Queue queue) {
        return BindingBuilder.bind(queue).to(topicExchange).with(RabbitConfig.routing);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
