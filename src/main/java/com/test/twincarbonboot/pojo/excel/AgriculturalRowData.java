package com.test.twincarbonboot.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AgriculturalRowData {
    @ExcelProperty("地块名称") private String name;
    @ExcelProperty("经度") private BigDecimal lon;
    @ExcelProperty("纬度") private BigDecimal lat;
    @ExcelProperty("年度") private Integer year;
    @ExcelProperty("季度") private String quarter;
    @ExcelProperty("水稻种植面积(ha)") private BigDecimal area;
    @ExcelProperty("该季度机械作业面积(ha)") private BigDecimal mechArea;
    @ExcelProperty("该季度化肥施用量(kg N)") private BigDecimal fertilizer;
    @ExcelProperty("该季度灌溉用电量(kWh)") private BigDecimal irrigationElec;
    @ExcelProperty("水分管理模式") private String waterMode;


}
