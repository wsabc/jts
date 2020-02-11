package org.example.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Sender {
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        Sender sender = new Sender();
        sender.send(QUEUE_NAME);
    }

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
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
