package org.example.springcloud.gateway.dynamic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway")
public class GatewayRoutesController {
    @Autowired
    private RefreshRouteService refreshRouteService;

    @GetMapping("/refreshRoutes")
    public void refreshRoutes(){
        refreshRouteService.refreshRoutes();
    }
}
