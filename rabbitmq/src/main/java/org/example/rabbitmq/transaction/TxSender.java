package org.example.rabbitmq.transaction;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TxSender {
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        TxSender sender = new TxSender();
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
            String message = "hello world";
            channel.txSelect(); //begin tx
            channel.basicPublish("", queue, null, message.getBytes());
            // make an exception to rollback
            int i = 1/0;
            channel.txCommit();
            System.out.println("[x] sent '" + message + "'");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            if (channel != null) {
                try {
                    channel.txRollback();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
