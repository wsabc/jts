package org.example.rabbitmq.workingq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class Receiver2 {
    private static final String QUEUE_NAME = "workingq";
    public static void main(String[] args) throws Exception {
        Receiver2 receiver = new Receiver2();
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

        channel.basicQos(1);

        System.out.println("[222*] waiting for the message, to exit press Ctrl+C");
        DeliverCallback dc = (consumerTag, message) -> {
            final String body = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("[*] received222 '" + body + "'");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);

        };
        channel.basicConsume(queue, false, dc, consumerTag -> {});
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
