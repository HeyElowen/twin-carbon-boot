package com.test.twincarbonboot.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class CarbonCustomPoint {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 地块名称
     */
    private String name;

    /**
     * 用地类型
     */
    private String category;

    /**
     * 年度
     */
    private Integer year;

    /**
     * 季度（Q1/Q2/Q3/Q4）
     */
    private String quarter;

    /**
     * 碳排放量（吨，后端计算结果）
     */
    private BigDecimal emission;

    /**
     * 空间坐标（WKT格式）
     */
    private String geom;

    /**
     * 经度（从geom中提取）
     */
    private BigDecimal lon;

    /**
     * 纬度（从geom中提取）
     */
    private BigDecimal lat;

    /**
     * 原始计算参数（对应数据库 raw_params JSONB）
     * 例如：{"用电量": 10000, "燃煤量": 50}
     */
    private Map<String, Object> rawParams;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
