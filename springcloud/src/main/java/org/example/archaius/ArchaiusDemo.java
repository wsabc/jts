package org.example.archaius;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

public class ArchaiusDemo {
    private static DynamicStringProperty dynamicStringProperty =
            DynamicPropertyFactory.getInstance().getStringProperty("good", "good");
    public static void main(String[] args) {
        System.out.println(dynamicStringProperty.get());
    }
}
