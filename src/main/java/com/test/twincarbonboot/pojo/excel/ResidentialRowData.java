package com.test.twincarbonboot.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResidentialRowData {

    @ExcelProperty("地块名称") private String name;
    @ExcelProperty("经度") private BigDecimal lon;
    @ExcelProperty("纬度") private BigDecimal lat;
    @ExcelProperty("年度") private Integer year;
    @ExcelProperty("季度") private String quarter;
    @ExcelProperty("建筑面积(m²)") private BigDecimal area;
    @ExcelProperty("建筑年代") private String buildingYear;
    @ExcelProperty("常住人口(人)") private Integer population;
    @ExcelProperty("采暖设备类型") private String heatDevice;
}
