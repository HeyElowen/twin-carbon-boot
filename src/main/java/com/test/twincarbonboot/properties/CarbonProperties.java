package com.test.twincarbonboot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "carbon")
public class CarbonProperties {

    private String sceneUrl;
}
