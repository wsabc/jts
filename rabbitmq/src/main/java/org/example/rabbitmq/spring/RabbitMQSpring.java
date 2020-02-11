package org.example.rabbitmq.spring;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RabbitMQSpring {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMQSpring.class);
    }

    @Autowired
    RabbitTemplate template;

    @GetMapping("/send")
    public void send() {
        template.convertAndSend("sd-test", "sd.good.test", "this is a good test");
    }

    @GetMapping("/get")
    public void get() {

    }
}
