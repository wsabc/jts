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

