package org.example.springcloud.gateway.dynamic;

import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class GatewayRoutesDefinitionConfig {
    @Bean
    RouteDefinitionLocator routeDefinitionLocator(){
        return new GatewayRoutesRepository();
    }

    @Bean
    @Primary
    RouteDefinitionWriter routeDefinitionWriter(){
        return new GatewayRoutesRepository();
    }
}
