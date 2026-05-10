package com.test.twincarbonboot.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.test.twincarbonboot.mapper.MonitoringMapper;
import com.test.twincarbonboot.pojo.CarbonCustomPoint;
import com.test.twincarbonboot.pojo.CarbonEmissionPoint;
import com.test.twincarbonboot.service.MonitoringService;
import com.test.twincarbonboot.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MonitoringServiceImpl implements MonitoringService {

    @Autowired
    private MonitoringMapper monitoringMapper;

    @Override
    public List<CarbonEmissionPoint> selectByYearAndQuarter(int year, String quarter) {
        if ("ALL".equals(quarter)|| StringUtils.isEmpty(quarter)) {
            return monitoringMapper.selectByYearAggregate(year);
        }
        return monitoringMapper.selectByYearAndQuarter(year, quarter);
    }

    @Override
    public List<CarbonCustomPoint> selectCustomByYearAndQuarter(Integer year, String quarter) {

        Integer userId = UserContext.getUserId();
        if ("ALL".equals(quarter)|| StringUtils.isEmpty(quarter)) {
            return monitoringMapper.CustomselectByYearAggregate(year,userId);
        }
        return monitoringMapper.CustomselectByYearAndQuarter(year, quarter,userId);
    }

    @Override
    public List selectCategoryRatio(Integer year, String quarter) {


        return monitoringMapper.selectCategoryRatio(year,quarter);

    }

    @Override
    public List selectTrend(Integer yearStart, Integer yearEnd, String category) {
        if (yearEnd != null && yearStart > yearEnd) {
            int temp = yearStart;
            yearStart = yearEnd;
            yearEnd = temp;
        }
        return monitoringMapper.selectTrend(yearStart, yearEnd, category);
    }

}
