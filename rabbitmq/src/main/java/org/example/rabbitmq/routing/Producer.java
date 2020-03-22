package org.example.rabbitmq.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    private static final String EXCHANGE_NAME = "ex_direct";
    public static void main(String[] args) {
        Producer sender = new Producer();
        sender.send("");
    }

    public void send(String queue) {
        // very similar to db url: host, port, db, user, password
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("/demo");
        factory.setUsername("user");
        factory.setPassword("123456");
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            String message = "hello direct";
            String routingKey = "info";
            // routingKey and message content are no association
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            System.out.println("[x] sent '" + message + "'");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
