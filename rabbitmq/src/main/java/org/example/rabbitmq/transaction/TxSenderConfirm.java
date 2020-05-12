package org.example.rabbitmq.transaction;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TxSenderConfirm {
    private static final String QUEUE_NAME = "helloConfirm";

    public static void main(String[] args) {
        TxSenderConfirm sender = new TxSenderConfirm();
        sender.send(QUEUE_NAME);
    }

    public void send(String queue) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("/demo");
        factory.setUsername("user");
        factory.setPassword("123456");
        Channel channel = null;
        try (Connection connection = factory.newConnection()) {
            channel = connection.createChannel();
            channel.queueDeclare(queue, false, false, false, null);
            // set confirm mode
            channel.confirmSelect();
            // send in batch
            String message = "hello confirm";
            for (int i = 0; i < 10; i++) {
                channel.basicPublish("", queue, null, message.getBytes());
                System.out.println("[x] sent '" + message + "'" + i);
            }
            if (!channel.waitForConfirms()) {
                System.out.println("NG-------");
            } else {
                System.out.println("OK-----------");
            }
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
