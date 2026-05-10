package com.test.twincarbonboot.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CarbonEmissionPoint {

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
     * 工业区 / 农业区 / 商业区 / 住宅区 / 教育区
     */
    private String category;

    /**
     * 面积（平方米）
     */
    private BigDecimal area;

    /**
     * 碳排放量（吨）
     */
    private BigDecimal emission;

    /**
     * 建筑高度（米）
     */
    private BigDecimal height;

    /**
     * 年度
     */
    private Integer year;

    /**
     * 季度（Q1/Q2/Q3/Q4）
     */
    private String quarter;

    /**
     * 空间坐标（WKT格式）
     * 示例：POINT(116.397 39.916)
     */
    private String geom;

    /**
     * 经度（从geom中提取，查询时计算）
     */
    private BigDecimal lon;

    /**
     * 纬度（从geom中提取，查询时计算）
     */
    private BigDecimal lat;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
