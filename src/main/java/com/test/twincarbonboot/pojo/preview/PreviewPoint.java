package com.test.twincarbonboot.pojo.preview;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PreviewPoint {
    private String name;
    private String category;
    private BigDecimal emission;
    private BigDecimal lon;
    private BigDecimal lat;
}