package com.test.twincarbonboot.pojo;

import lombok.Data;

@Data
public class LoginVO {

    private String token;
    private String username;
    private Config config;

    @Data
    public static class Config {
        private String sceneUrl;
    }
}
