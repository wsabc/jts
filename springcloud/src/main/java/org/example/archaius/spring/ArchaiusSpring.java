package org.example.archaius.spring;

import com.netflix.config.DynamicConfiguration;
import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.PolledConfigurationSource;
import com.netflix.config.sources.URLConfigurationSource;
import org.apache.commons.configuration.AbstractConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URL;

@SpringBootApplication
public class ArchaiusSpring {
    public static void main(String[] args) {
        SpringApplication.run(ArchaiusSpring.class);
    }

    @Bean
    public AbstractConfiguration addAppProperties() throws IOException {
        URL url = new ClassPathResource("my-conf.properties").getURL();
        PolledConfigurationSource source = new URLConfigurationSource(url);
        return new DynamicConfiguration(source, new FixedDelayPollingScheduler());
    }
}
