package org.example.springcloud.gateway;

@Component
@RestControllerEndpoint(id = "demoGateway")
public class CustomGatewayControllerEndpoint {
    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/refreshRouteConfig")
    public Mono<Void> refreshRoutes() {
        this.publisher.publishEvent(new RouteConfigRefreshEvent(this));
        return Mono.empty();
    }
}
