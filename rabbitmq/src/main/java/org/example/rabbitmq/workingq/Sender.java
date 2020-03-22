package org.example.rabbitmq.workingq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Sender {
    private static final String QUEUE_NAME = "workingq";

    public static void main(String[] args) {
        Sender sender = new Sender();
        sender.send(QUEUE_NAME);
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
            channel.queueDeclare(queue, false, false, false, null);
            String message = "hello working queues";
            String temp = "";
            for (int i = 0; i < 50; i++) {
                temp = message + i;
                channel.basicPublish("", queue, null, temp.getBytes());
                System.out.println("[x] sent '" + temp + "'");
            }

            Thread.sleep(1000); // to avoid AlreadyClosedException, the last one may not send due to connection closed by below code

//            channel.close();
//            connection.close();
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
