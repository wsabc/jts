package org.example.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class HelloWorld {
    private static final String QUEUE_NAME = "hello";
    public static void main(String[] args) throws Exception {
        Sender sender = new Sender();
        sender.send(QUEUE_NAME);
        Receiver receiver = new Receiver();
        receiver.recv(QUEUE_NAME);
    }
}

class Sender {
    public void send(String queue) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            // idempotent operation, only created if doesn't exist already
            channel.queueDeclare(queue, false, false, false, null);
            String message = "hello world";
            channel.basicPublish("", queue, null, message.getBytes());
            System.out.println("[x] sent '" + message + "'");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}

class Receiver {
    public void recv(String queue) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // don't use try-with-resource, we need consumer keep running, not move on, close and exit
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queue, false, false, false, null);
        System.out.println("[*] waiting for the message, to exit press Ctrl+C");
        DeliverCallback dc = (consumerTag, message) -> {
            final String body = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("[*] received '" + message + "'");
        };
        channel.basicConsume(queue, true, dc, consumerTag -> {});
    }
}