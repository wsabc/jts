package org.example.archaius.spring;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArchaiusController {

    DynamicStringProperty dynamicStringProperty = DynamicPropertyFactory.getInstance().getStringProperty("good", "good1");

    @GetMapping("/archaius")
    public String getConfig() {
        String name  = dynamicStringProperty.getName();
        String value = dynamicStringProperty.get();
        return name + ":" + value;
    }

}
