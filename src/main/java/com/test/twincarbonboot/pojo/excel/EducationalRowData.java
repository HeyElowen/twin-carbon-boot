package com.test.twincarbonboot.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EducationalRowData {
    @ExcelProperty("地块名称") private String name;
    @ExcelProperty("经度") private BigDecimal lon;
    @ExcelProperty("纬度") private BigDecimal lat;
    @ExcelProperty("年度") private Integer year;
    @ExcelProperty("季度") private String quarter;
    @ExcelProperty("建筑面积(m²)") private BigDecimal area;
    @ExcelProperty("学校类型") private String schoolType;
    @ExcelProperty("在校学生数") private Integer studentCount;
    @ExcelProperty("教职工数") private Integer teacherCount;
    @ExcelProperty("是否住宿制") private String dormitory;

}
