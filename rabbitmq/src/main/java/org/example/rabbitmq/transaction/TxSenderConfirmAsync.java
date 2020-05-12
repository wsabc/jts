package org.example.rabbitmq.transaction;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

public class TxSenderConfirmAsync {
    private static final String QUEUE_NAME = "helloConfirm";

    public static void main(String[] args) {
        TxSenderConfirmAsync sender = new TxSenderConfirmAsync();
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
            SortedSet<Long> unconfirmedSet = Collections.synchronizedSortedSet(new TreeSet<>());
            channel.addConfirmListener((deliveryTag, multiple) -> {
                if (multiple) {
                    System.out.println("ack multiple ----");
                    unconfirmedSet.headSet(deliveryTag + 1).clear();
                } else {
                    System.out.println("ack multiple false----");
                    unconfirmedSet.remove(deliveryTag);
                }
            }, (deliveryTag, multiple) -> {
                if (multiple) {
                    System.out.println("nack multiple ---- will retry");
                    unconfirmedSet.headSet(deliveryTag + 1).clear();
                } else {
                    System.out.println("anck multiple false---- will retry");
                    unconfirmedSet.remove(deliveryTag);
                }
            });

            // send in batch
            String message = "hello confirm";
            for (int i = 0; i < 10; i++) {
                long nextPublishSeqNo = channel.getNextPublishSeqNo();
                channel.basicPublish("", queue, null, message.getBytes());
                System.out.println("[x] sent '" + message + "'" + i);
                unconfirmedSet.add(nextPublishSeqNo);
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
