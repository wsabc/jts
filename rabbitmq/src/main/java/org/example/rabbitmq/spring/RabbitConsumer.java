package org.example.rabbitmq.spring;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RabbitListener(queues = "sd-q")
public class RabbitConsumer {

    @RabbitHandler
    public void directHandlerManualAck(String messageStruct, Message message, Channel channel) {
        final long deliverTag = message.getMessageProperties().getDeliveryTag();
        try {
            System.out.println("====>" + messageStruct);
            channel.basicAck(deliverTag, false);
        } catch (IOException e) {
            try {
                channel.basicRecover();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
