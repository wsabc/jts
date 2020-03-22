package org.example.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class TopicConsumer {
    private static final String EXCHANGE_NAME = "ex_topic";
    private static final String QUEUE_NAME = "ex_topic_queue";
    public static void main(String[] args) throws Exception {
        TopicConsumer receiver = new TopicConsumer();
        receiver.recv(QUEUE_NAME);
    }
    public void recv(String queue) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("/demo");
        factory.setUsername("user");
        factory.setPassword("123456");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        channel.queueDeclare(queue, false, false, false, null);
        channel.basicQos(1);
        String routingKey = "goods.#";
        channel.queueBind(queue, EXCHANGE_NAME, routingKey);
        // if the queue binds to multiple routing key, do it like below
//        channel.queueBind(queue, EXCHANGE_NAME, "error");
//        channel.queueBind(queue, EXCHANGE_NAME, "warn");

        System.out.println("[*] waiting for the message, to exit press Ctrl+C");
        DeliverCallback dc = (consumerTag, message) -> {
            final String body = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("[*] received '" + body + "'");
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        channel.basicConsume(queue, false, dc, consumerTag -> {});
    }
}
