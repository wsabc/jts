package org.example.rabbitmq.spring;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("sd-test");
    }

    @Bean
    public Queue queue() {
        return new Queue("sd-q", true);
    }

    @Bean
    public Binding binding(Queue q, TopicExchange ex) {
        return BindingBuilder.bind(q).to(ex).with("sd.#.test");
    }

    @Bean
    public RabbitTemplate template(CachingConnectionFactory factory) {
        factory.setPublisherReturns(true);
        factory.setPublisherConfirms(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            String msg = String.format("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
            System.out.println(msg);
        });
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println(String.format("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message));
        });
        return rabbitTemplate;
    }

}
