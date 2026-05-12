package com.test.twincarbonboot.mapper;

import com.test.twincarbonboot.pojo.CarbonCustomPoint;
import com.test.twincarbonboot.pojo.CarbonEmissionPoint;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MonitoringMapper {


    //系统数据
    List<CarbonEmissionPoint> selectByYearAndQuarter(int year, String quarter);

    List<CarbonEmissionPoint> selectByYearAggregate(int year);


    //自定义数据
    List<CarbonCustomPoint> CustomselectByYearAndQuarter(Integer year, String quarter, Integer userId);

    List<CarbonCustomPoint> CustomselectByYearAggregate(Integer year, Integer userId);

    List selectCategoryRatio(Integer year, String quarter);

    List selectTrend(Integer yearStart, Integer yearEnd, String category);

    List selectByName( String name, Integer year, String quarter);

    List selectCustomCategoryRatio(Integer year, String quarter, Integer userId);

    List selectCustomTrend(Integer yearStart, Integer yearEnd, String category, Integer userId);

    void batchInsertCustomPoints(List<Map<String, Object>> rows);
}
