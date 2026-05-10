package com.test.twincarbonboot.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.test.twincarbonboot.mapper.MonitoringMapper;
import com.test.twincarbonboot.pojo.CarbonCustomPoint;
import com.test.twincarbonboot.pojo.CarbonEmissionPoint;
import com.test.twincarbonboot.service.MonitoringService;
import com.test.twincarbonboot.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<CarbonCustomPoint> CustomOselectByYearAndQuarter(Integer year, String quarter) {

        Integer userId = UserContext.getUserId();
        if ("ALL".equals(quarter)|| StringUtils.isEmpty(quarter)) {
            return monitoringMapper.CustomselectByYearAggregate(year,userId);
        }
        return monitoringMapper.CustomselectByYearAndQuarter(year, quarter,userId);
    }
}
