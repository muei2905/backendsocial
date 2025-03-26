package com.example.BackEndSocial.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "notification.exchange";
    public static final String LIKE_QUEUE = "notification.queue";
    public static final String FRIENDSHIP_QUEUE = "notification.friendship.queue";
    public static final String COMMENT_QUEUE = "notification.comment.queue";
    public static final String LIKE_ROUTING_KEY = "notification.like";
    public static final String COMMENT_ROUTING_KEY = "notification.comment";
    public static final String FRIENDSHIP_ROUTING_KEY = "notification.friendship";
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }


    @Bean
    public Queue friendshipQueue() {
        return new Queue(FRIENDSHIP_QUEUE);
    }

    @Bean
    public Binding friendshipBinding(Queue friendshipQueue, TopicExchange exchange) {
        return BindingBuilder.bind(friendshipQueue).to(exchange).with(FRIENDSHIP_ROUTING_KEY);
    }

    @Bean
    public Queue likeQueue() {
        return new Queue(LIKE_QUEUE);
    }

    @Bean
    public Queue commentQueue() {
        return new Queue(COMMENT_QUEUE);
    }

    @Bean
    public Binding likeBinding(Queue likeQueue, TopicExchange exchange) {
        return BindingBuilder.bind(likeQueue).to(exchange).with(LIKE_ROUTING_KEY);
    }

    @Bean
    public Binding commentBinding(Queue commentQueue, TopicExchange exchange) {
        return BindingBuilder.bind(commentQueue).to(exchange).with(COMMENT_ROUTING_KEY);
    }
}


