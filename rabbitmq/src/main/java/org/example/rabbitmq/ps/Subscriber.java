package org.example.rabbitmq.ps;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class Subscriber {
    private static final String EXCHANGE_NAME = "ex_ps_queue";
    private static final String QUEUE_NAME = "ps_queue";
    public static void main(String[] args) throws Exception {
        Subscriber receiver = new Subscriber();
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
        /*
        channel.queueDeclare(queue, false, false, false, null);

        channel.queueBind(queue, EXCHANGE_NAME, "");
         */
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String tempQ = channel.queueDeclare().getQueue();
        channel.queueBind(tempQ, EXCHANGE_NAME, "");

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
