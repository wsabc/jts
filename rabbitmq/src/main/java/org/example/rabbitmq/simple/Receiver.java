package org.example.rabbitmq.simple;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Receiver {
    private static final String QUEUE_NAME = "hello";
    public static void main(String[] args) throws Exception {
        Receiver receiver = new Receiver();
        receiver.recv(QUEUE_NAME);
    }
    public void recv(String queue) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("/demo");
        factory.setUsername("user");
        factory.setPassword("123456");
        // don't use try-with-resource, we need consumer keep running, not move on, close and exit
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queue, false, false, false, null);
        System.out.println("[*] waiting for the message, to exit press Ctrl+C");
        DeliverCallback dc = (consumerTag, message) -> {
            final String body = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("[*] received '" + body + "'");
        };
        channel.basicConsume(queue, true, dc, consumerTag -> {});
        // old api
        /*
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                final String bodyStr = new String(body, StandardCharsets.UTF_8);
                System.out.println("[*] received '" + bodyStr + "'");
            }
        };
        channel.basicConsume(queue, true, consumer);
         */
    }
}
