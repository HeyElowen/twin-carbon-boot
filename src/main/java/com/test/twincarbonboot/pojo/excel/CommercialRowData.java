package com.test.twincarbonboot.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CommercialRowData {
    @ExcelProperty("地块名称") private String name;
    @ExcelProperty("经度") private BigDecimal lon;
    @ExcelProperty("纬度") private BigDecimal lat;
    @ExcelProperty("年度") private Integer year;
    @ExcelProperty("季度") private String quarter;
    @ExcelProperty("建筑面积(m²)") private BigDecimal area;
    @ExcelProperty("具体子类") private String subType;


}
