package com.example.Order_Service.messageBrocker;
import com.example.Order_Service.Events.UserDeleteEvent;
import com.example.Order_Service.messageBrocker.OrderCreateEvent;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
    public static final String ORDER_EXCHANGE     = "order.exchange";
    public static final String ORDER_QUEUE        = "order.create.queue";
    public static final String ORDER_ROUTING_KEY  = "order.create";

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE).build();
    }

    @Bean
    public Binding orderBinding(@Qualifier("orderQueue")Queue orderQueue, @Qualifier("orderExchange")TopicExchange orderExchange) {
        return BindingBuilder.bind(orderQueue)
                .to(orderExchange)
                .with(ORDER_ROUTING_KEY);
    }

    public static final String PRODUCT_EXCHANGE    = "product.exchange";
    public static final String PRODUCT_QUEUE       = "product.update.queue";
    public static final String PRODUCT_ROUTING_KEY = "product.update";

    @Bean
    TopicExchange productExchange() {
        return new TopicExchange(PRODUCT_EXCHANGE);
    }
    @Bean
    Queue productQueue() {
        return QueueBuilder.durable(PRODUCT_QUEUE).build();
    }
    @Bean
    Binding productBinding(@Qualifier("productQueue") Queue q, @Qualifier("productExchange") TopicExchange ex) {
        return BindingBuilder.bind(q).to(ex).with(PRODUCT_ROUTING_KEY);
    }

    public static final String USER_EXCHANGE       = "user.exchange";
    public static final String USER_DELETE_QUEUE   = "user.delete.queue";
    public static final String USER_DELETE_ROUTING = "user.deleted";

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    public Queue userDeleteQueue() {
        return QueueBuilder.durable(USER_DELETE_QUEUE).build();
    }

    @Bean
    public Binding userDeleteBinding(
            @Qualifier("userDeleteQueue") Queue queue,
            @Qualifier("userExchange") TopicExchange exchange
    ) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(USER_DELETE_ROUTING);
    }




    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper mapper = new DefaultJackson2JavaTypeMapper();

        // 1) Trust the package where the published event lives
        mapper.setTrustedPackages(
                "com.example.Payment.Service.Events",
                "com.example.Order_Service.events",
                "com.example.Product_Service.events",
                "com.example.User_Service.rabbitMQ"    // <-- include this!
        );

        // 2) Map the producer’s FQCN → your local class
        mapper.setIdClassMapping(Map.of(
                "com.example.Payment.Service.Events.OrderCreateEvent",
                OrderCreateEvent.class,
                "com.example.Order_Service.events.ProductUpdateEvent",
                ProductUpdateEvent.class,
                "com.example.User_Service.rabbitMQ.UserDeleteEvent",  // <-- exact header value
                UserDeleteEvent.class
        ));

        mapper.setTypePrecedence(DefaultJackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        converter.setJavaTypeMapper(mapper);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory cf,
            Jackson2JsonMessageConverter converter
    ) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(converter);
        return tpl;
    }
}
