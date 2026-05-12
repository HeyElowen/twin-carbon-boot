package com.test.twincarbonboot.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IndustrialRowData {
    @ExcelProperty("地块名称") private String name;
    @ExcelProperty("经度") private BigDecimal lon;
    @ExcelProperty("纬度") private BigDecimal lat;
    @ExcelProperty("年度") private Integer year;
    @ExcelProperty("季度") private String quarter;
    @ExcelProperty("工业建筑面积(m²)") private BigDecimal area;
    @ExcelProperty("行业类型") private String industryType  ;
    @ExcelProperty("天然气消耗量(m³)") private BigDecimal gasConsumption;

}
