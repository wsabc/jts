package org.example.rabbitmq.ps;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Publisher {
    private static final String EXCHANGE_NAME = "ex_ps_queue";
    public static void main(String[] args) {
        Publisher sender = new Publisher();
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
        try (
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            // idempotent operation, only created if doesn't exist already
            // tell mq which queue to connect
            // channel.queueDeclare(queue, false, false, false, null);
            // no queue need, just face to the exchange
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String message = "hello world";
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes()); // send to exchange not queue
            System.out.println("[x] sent '" + message + "'");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            channel.close();
//            connection.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
