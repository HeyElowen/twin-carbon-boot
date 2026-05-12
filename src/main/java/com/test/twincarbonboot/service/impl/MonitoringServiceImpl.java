package com.test.twincarbonboot.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.test.twincarbonboot.mapper.MonitoringMapper;
import com.test.twincarbonboot.pojo.CarbonCustomPoint;
import com.test.twincarbonboot.pojo.CarbonEmissionPoint;
import com.test.twincarbonboot.service.MonitoringService;
import com.test.twincarbonboot.utils.UserContext;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.test.twincarbonboot.cache.PreviewCache;
import com.test.twincarbonboot.exception.MonitoringException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MonitoringServiceImpl implements MonitoringService {

    @Autowired
    private MonitoringMapper monitoringMapper;

    @Autowired
    private PreviewCache previewCache;

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

    @Override
    public List selectByName(String name, Integer year,String quarter) {
        return monitoringMapper.selectByName(name,year,quarter);
    }

    @Override
    public List selectCustomCategoryRatio(Integer year, String quarter) {

        Integer userId = UserContext.getUserId();

        return monitoringMapper.selectCustomCategoryRatio(year,quarter,userId);

    }

    @Override
    public List selectCustomTrend(Integer yearStart, Integer yearEnd, String category) {
        Integer userId = UserContext.getUserId();


        if (yearEnd != null && yearStart > yearEnd) {
            int temp = yearStart;
            yearStart = yearEnd;
            yearEnd = temp;
        }
        return monitoringMapper.selectCustomTrend(yearStart, yearEnd, category,userId);


    }

    @Override
    public void confirmImport(String batchId) {

        PreviewCache.CacheEntry entry = previewCache.get(batchId);
        if (entry == null) {
            throw new MonitoringException("预览已过期或不存在");
        }
        Integer currentUserId = UserContext.getUserId();
        if (!currentUserId.equals(entry.getUserId())) {
            throw new MonitoringException("无权操作此批次数据");
        }

        List<Map<String, Object>> rawDataList = entry.getData().getRawDataList();
        List<Map<String, Object>> insertRows = new ArrayList<>();

        for (Map<String, Object> item : rawDataList) {
            Map<String, Object> row = new HashMap<>();
            row.put("userId", currentUserId);
            row.put("name", item.get("name"));
            row.put("category", item.get("category"));
            row.put("year", item.get("year"));
            row.put("quarter", item.get("quarter"));
            row.put("emission", item.get("emission"));
            row.put("lon", item.get("lon"));
            row.put("lat", item.get("lat"));
            // raw_params：去掉基础字段，只留计算参数
            Map<String, Object> rawParams = new HashMap<>(item);
            rawParams.remove("name");
            rawParams.remove("category");
            rawParams.remove("year");
            rawParams.remove("quarter");
            rawParams.remove("lon");
            rawParams.remove("lat");
            rawParams.remove("emission");
            row.put("rawParams", rawParams);
            insertRows.add(row);
        }

        if (insertRows.isEmpty()) {
            log.warn("确认入库数据为空: batchId={}", batchId);
            previewCache.remove(batchId);
            return;
        }
        monitoringMapper.batchInsertCustomPoints(insertRows);
        previewCache.remove(batchId);
        log.info("确认入库成功: batchId={}, 入库条数={}", batchId, insertRows.size());

    }

}
