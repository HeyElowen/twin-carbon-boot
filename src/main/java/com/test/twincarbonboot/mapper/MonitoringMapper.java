package com.test.twincarbonboot.mapper;

import com.test.twincarbonboot.pojo.CarbonCustomPoint;
import com.test.twincarbonboot.pojo.CarbonEmissionPoint;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MonitoringMapper {


    //系统数据
    List<CarbonEmissionPoint> selectByYearAndQuarter(int year, String quarter);

    List<CarbonEmissionPoint> selectByYearAggregate(int year);


    //自定义数据
    List<CarbonCustomPoint> CustomselectByYearAndQuarter(Integer year, String quarter, Integer userId);

    List<CarbonCustomPoint> CustomselectByYearAggregate(Integer year, Integer userId);
}
