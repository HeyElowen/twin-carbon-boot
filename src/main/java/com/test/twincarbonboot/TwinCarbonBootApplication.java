package com.test.twincarbonboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.test.twincarbonboot.properties.CarbonProperties;

@SpringBootApplication
@EnableConfigurationProperties(CarbonProperties.class)
public class TwinCarbonBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwinCarbonBootApplication.class, args);
    }

}
