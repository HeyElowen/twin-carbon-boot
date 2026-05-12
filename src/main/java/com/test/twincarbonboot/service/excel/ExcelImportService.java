package com.test.twincarbonboot.service.excel;

import com.alibaba.excel.EasyExcel;
import com.test.twincarbonboot.calculator.CarbonCalculator;
import com.test.twincarbonboot.pojo.excel.*;
import com.test.twincarbonboot.pojo.preview.ImportError;
import com.test.twincarbonboot.pojo.preview.PreviewPoint;
import com.test.twincarbonboot.pojo.preview.PreviewResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ExcelImportService {

    @Autowired
    private CarbonCalculator carbonCalculator;

    public PreviewResult parse(MultipartFile file) throws IOException {
        List<PreviewPoint> previewPoints = new ArrayList<>();
        List<ImportError> errors = new ArrayList<>();
        List<Map<String, Object>> rawDataList = new ArrayList<>();

        int totalCount = 0;


        // ========== 住宅区 ==========
        try {
            List<ResidentialRowData> rows = EasyExcel.read(file.getInputStream())
                    .sheet("住宅区")
                    .head(ResidentialRowData.class)
                    .doReadSync();
            for (int i = 0; i < rows.size(); i++) {
                totalCount++;
                processResidential(rows.get(i), i + 2, previewPoints, errors, rawDataList);
            }
        } catch (Exception e) {
            errors.add(createError(0, "住宅区", "Sheet解析失败: " + e.getMessage()));
        }

        // ========== 商业区 ==========
        try {
            List<CommercialRowData> rows = EasyExcel.read(file.getInputStream())
                    .sheet("商业区")
                    .head(CommercialRowData.class)
                    .doReadSync();
            for (int i = 0; i < rows.size(); i++) {
                totalCount++;
                processCommercial(rows.get(i), i + 2, previewPoints, errors,rawDataList);
            }
        } catch (Exception e) {
            errors.add(createError(0, "商业区", "Sheet解析失败: " + e.getMessage()));
        }

        // ========== 工业区 ==========
        try {
            List<IndustrialRowData> rows = EasyExcel.read(file.getInputStream())
                    .sheet("工业区")
                    .head(IndustrialRowData.class)
                    .doReadSync();
            for (int i = 0; i < rows.size(); i++) {
                totalCount++;
                processIndustrial(rows.get(i), i + 2, previewPoints, errors,rawDataList);
            }
        } catch (Exception e) {
            errors.add(createError(0, "工业区", "Sheet解析失败: " + e.getMessage()));
        }

        // ========== 农业区 ==========
        try {
            List<AgriculturalRowData> rows = EasyExcel.read(file.getInputStream())
                    .sheet("农业区")
                    .head(AgriculturalRowData.class)
                    .doReadSync();
            for (int i = 0; i < rows.size(); i++) {
                totalCount++;
                processAgricultural(rows.get(i), i + 2, previewPoints, errors,rawDataList);
            }
        } catch (Exception e) {
            errors.add(createError(0, "农业区", "Sheet解析失败: " + e.getMessage()));
        }

        // ========== 教育区 ==========
        try {
            List<EducationalRowData> rows = EasyExcel.read(file.getInputStream())
                    .sheet("教育区")
                    .head(EducationalRowData.class)
                    .doReadSync();
            for (int i = 0; i < rows.size(); i++) {
                totalCount++;
                processEducational(rows.get(i), i + 2, previewPoints, errors,rawDataList);
            }
        } catch (Exception e) {
            errors.add(createError(0, "教育区", "Sheet解析失败: " + e.getMessage()));
        }

        // ========== 组装结果 ==========
        PreviewResult result = new PreviewResult();
        result.setBatchId(UUID.randomUUID().toString());
        result.setTotalCount(totalCount);
        result.setValidCount(previewPoints.size());
        result.setInvalidCount(errors.size());
        result.setPreviewPoints(previewPoints);
        result.setRawDataList(rawDataList);
        result.setErrors(errors);
        return result;
    }

    // ==================== 住宅区处理 ====================
    private void processResidential(ResidentialRowData row, int rowIndex,
                                    List<PreviewPoint> points, List<ImportError> errors,
                                    List<Map<String, Object>> rawDataList) {
        if (isBlank(row.getName())) {
            errors.add(createError(rowIndex, "地块名称", "不能为空")); return;
        }
        if (!validLon(row.getLon())) {
            errors.add(createError(rowIndex, "经度", "超出范围[-180, 180]")); return;
        }
        if (!validLat(row.getLat())) {
            errors.add(createError(rowIndex, "纬度", "超出范围[-90, 90]")); return;
        }
        if (!validYear(row.getYear())) {
            errors.add(createError(rowIndex, "年度", "超出范围[2000, 2100]")); return;
        }
        Integer q = quarterToIndex(row.getQuarter());
        if (q == null) {
            errors.add(createError(rowIndex, "季度", "只能是Q1/Q2/Q3/Q4")); return;
        }
        if (row.getArea() == null || row.getArea().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(createError(rowIndex, "建筑面积", "必须大于0")); return;
        }
        Set<String> validYears = Set.of("before-1980", "1980-2000", "2000-2010", "after-2010");
        if (!validYears.contains(row.getBuildingYear())) {
            errors.add(createError(rowIndex, "建筑年代", "非法值: " + row.getBuildingYear())); return;
        }
        Set<String> validDevices = Set.of("air-pump", "gas-boiler", "electric-heater", "heat-pump");
        if (!validDevices.contains(row.getHeatDevice())) {
            errors.add(createError(rowIndex, "采暖设备", "非法值: " + row.getHeatDevice())); return;
        }
        if (row.getPopulation() == null || row.getPopulation() < 0) {
            errors.add(createError(rowIndex, "常住人口", "不能为空或负数")); return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("area", row.getArea());
        params.put("year", row.getBuildingYear());
        params.put("population", row.getPopulation());
        params.put("heatDevice", row.getHeatDevice());
        params.put("quarter", q);

        BigDecimal emission = carbonCalculator.calculate("住宅区", params);
        points.add(buildPoint(row.getName(), "住宅区", emission, row.getLon(), row.getLat()));

        Map<String, Object> rawItem = new HashMap<>(params);
        rawItem.put("name", row.getName());
        rawItem.put("category", "住宅区");
        rawItem.put("year", row.getYear());
        rawItem.put("quarter", row.getQuarter());
        rawItem.put("lon", row.getLon());
        rawItem.put("lat", row.getLat());
        rawItem.put("emission", emission);
        rawDataList.add(rawItem);




    }

    // ==================== 商业区处理 ====================
    private void processCommercial(CommercialRowData row, int rowIndex,
                                   List<PreviewPoint> points, List<ImportError> errors,
                                   List<Map<String, Object>> rawDataList) {
        if (isBlank(row.getName())) {
            errors.add(createError(rowIndex, "地块名称", "不能为空")); return;
        }
        if (!validLon(row.getLon())) {
            errors.add(createError(rowIndex, "经度", "超出范围[-180, 180]")); return;
        }
        if (!validLat(row.getLat())) {
            errors.add(createError(rowIndex, "纬度", "超出范围[-90, 90]")); return;
        }
        if (!validYear(row.getYear())) {
            errors.add(createError(rowIndex, "年度", "超出范围[2000, 2100]")); return;
        }
        Integer q = quarterToIndex(row.getQuarter());
        if (q == null) {
            errors.add(createError(rowIndex, "季度", "只能是Q1/Q2/Q3/Q4")); return;
        }
        if (row.getArea() == null || row.getArea().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(createError(rowIndex, "建筑面积", "必须大于0")); return;
        }
        Set<String> validSubs = Set.of("mall", "supermarket", "office", "restaurant", "hotel", "fast-food");
        if (!validSubs.contains(row.getSubType())) {
            errors.add(createError(rowIndex, "具体子类", "非法值: " + row.getSubType())); return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("area", row.getArea());
        params.put("subType", row.getSubType());
        params.put("quarter", q);

        BigDecimal emission = carbonCalculator.calculate("商业区", params);
        points.add(buildPoint(row.getName(), "商业区", emission, row.getLon(), row.getLat()));

        Map<String, Object> rawItem = new HashMap<>(params);
        rawItem.put("name", row.getName());
        rawItem.put("category", "商业区");
        rawItem.put("year", row.getYear());
        rawItem.put("quarter", row.getQuarter());
        rawItem.put("lon", row.getLon());
        rawItem.put("lat", row.getLat());
        rawItem.put("emission", emission);
        rawDataList.add(rawItem);
    }

    // ==================== 工业区处理 ====================
    private void processIndustrial(IndustrialRowData row, int rowIndex,
                                   List<PreviewPoint> points, List<ImportError> errors,
                                   List<Map<String, Object>> rawDataList) {
        if (isBlank(row.getName())) {
            errors.add(createError(rowIndex, "地块名称", "不能为空")); return;
        }
        if (!validLon(row.getLon())) {
            errors.add(createError(rowIndex, "经度", "超出范围[-180, 180]")); return;
        }
        if (!validLat(row.getLat())) {
            errors.add(createError(rowIndex, "纬度", "超出范围[-90, 90]")); return;
        }
        if (!validYear(row.getYear())) {
            errors.add(createError(rowIndex, "年度", "超出范围[2000, 2100]")); return;
        }
        Integer q = quarterToIndex(row.getQuarter());
        if (q == null) {
            errors.add(createError(rowIndex, "季度", "只能是Q1/Q2/Q3/Q4")); return;
        }
        if (row.getArea() == null || row.getArea().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(createError(rowIndex, "工业建筑面积", "必须大于0")); return;
        }
        Set<String> validTypes = Set.of("electronic", "machinery", "textile", "food", "logistics");
        if (!validTypes.contains(row.getIndustryType())) {
            errors.add(createError(rowIndex, "行业类型", "非法值: " + row.getIndustryType())); return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("area", row.getArea());
        params.put("industryType", row.getIndustryType());
        params.put("gasConsumption", row.getGasConsumption());
        params.put("quarter", q);

        BigDecimal emission = carbonCalculator.calculate("工业区", params);
        points.add(buildPoint(row.getName(), "工业区", emission, row.getLon(), row.getLat()));

        Map<String, Object> rawItem = new HashMap<>(params);
        rawItem.put("name", row.getName());
        rawItem.put("category", "工业区");
        rawItem.put("year", row.getYear());
        rawItem.put("quarter", row.getQuarter());
        rawItem.put("lon", row.getLon());
        rawItem.put("lat", row.getLat());
        rawItem.put("emission", emission);
        rawDataList.add(rawItem);
    }

    // ==================== 农业区处理 ====================
    private void processAgricultural(AgriculturalRowData row, int rowIndex,
                                     List<PreviewPoint> points, List<ImportError> errors,
                                     List<Map<String, Object>> rawDataList) {
        if (isBlank(row.getName())) {
            errors.add(createError(rowIndex, "地块名称", "不能为空")); return;
        }
        if (!validLon(row.getLon())) {
            errors.add(createError(rowIndex, "经度", "超出范围[-180, 180]")); return;
        }
        if (!validLat(row.getLat())) {
            errors.add(createError(rowIndex, "纬度", "超出范围[-90, 90]")); return;
        }
        if (!validYear(row.getYear())) {
            errors.add(createError(rowIndex, "年度", "超出范围[2000, 2100]")); return;
        }
        Integer q = quarterToIndex(row.getQuarter());
        if (q == null) {
            errors.add(createError(rowIndex, "季度", "只能是Q1/Q2/Q3/Q4")); return;
        }
        if (row.getArea() == null || row.getArea().compareTo(BigDecimal.ZERO) < 0) {
            errors.add(createError(rowIndex, "水稻种植面积", "不能为负数")); return;
        }
        Set<String> validModes = Set.of("continuous", "intermittent", "dry");
        if (!validModes.contains(row.getWaterMode())) {
            errors.add(createError(rowIndex, "水分管理模式", "非法值: " + row.getWaterMode())); return;
        }
        if (row.getMechArea() == null || row.getMechArea().compareTo(BigDecimal.ZERO) < 0) {
            errors.add(createError(rowIndex, "机械作业面积", "不能为空或负数")); return;
        }
        if (row.getFertilizer() == null || row.getFertilizer().compareTo(BigDecimal.ZERO) < 0) {
            errors.add(createError(rowIndex, "化肥施用量", "不能为空或负数")); return;
        }
        if (row.getIrrigationElec() == null || row.getIrrigationElec().compareTo(BigDecimal.ZERO) < 0) {
            errors.add(createError(rowIndex, "灌溉用电量", "不能为空或负数")); return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("area", row.getArea());
        params.put("mechArea", row.getMechArea());
        params.put("fertilizer", row.getFertilizer());
        params.put("irrigationElec", row.getIrrigationElec());
        params.put("waterMode", row.getWaterMode());
        params.put("quarter", q);

        BigDecimal emission = carbonCalculator.calculate("农业区", params);
        points.add(buildPoint(row.getName(), "农业区", emission, row.getLon(), row.getLat()));

        Map<String, Object> rawItem = new HashMap<>(params);
        rawItem.put("name", row.getName());
        rawItem.put("category", "农业区");
        rawItem.put("year", row.getYear());
        rawItem.put("quarter", row.getQuarter());
        rawItem.put("lon", row.getLon());
        rawItem.put("lat", row.getLat());
        rawItem.put("emission", emission);
        rawDataList.add(rawItem);
    }

    // ==================== 教育区处理 ====================
    private void processEducational(EducationalRowData row, int rowIndex,
                                    List<PreviewPoint> points, List<ImportError> errors,
                                    List<Map<String, Object>> rawDataList) {
        if (isBlank(row.getName())) {
            errors.add(createError(rowIndex, "地块名称", "不能为空")); return;
        }
        if (!validLon(row.getLon())) {
            errors.add(createError(rowIndex, "经度", "超出范围[-180, 180]")); return;
        }
        if (!validLat(row.getLat())) {
            errors.add(createError(rowIndex, "纬度", "超出范围[-90, 90]")); return;
        }
        if (!validYear(row.getYear())) {
            errors.add(createError(rowIndex, "年度", "超出范围[2000, 2100]")); return;
        }
        Integer q = quarterToIndex(row.getQuarter());
        if (q == null) {
            errors.add(createError(rowIndex, "季度", "只能是Q1/Q2/Q3/Q4")); return;
        }
        if (row.getArea() == null || row.getArea().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(createError(rowIndex, "建筑面积", "必须大于0")); return;
        }
        Set<String> validTypes = Set.of("primary", "middle", "high", "vocational", "university");
        if (!validTypes.contains(row.getSchoolType())) {
            errors.add(createError(rowIndex, "学校类型", "非法值: " + row.getSchoolType())); return;
        }
        if (!"yes".equals(row.getDormitory()) && !"no".equals(row.getDormitory())) {
            errors.add(createError(rowIndex, "是否住宿制", "只能是yes/no")); return;
        }
        if (row.getStudentCount() == null || row.getStudentCount() < 0) {
            errors.add(createError(rowIndex, "在校学生数", "不能为空或负数")); return;
        }
        if (row.getTeacherCount() == null || row.getTeacherCount() < 0) {
            errors.add(createError(rowIndex, "教职工数", "不能为空或负数")); return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("area", row.getArea());
        params.put("schoolType", row.getSchoolType());
        params.put("studentCount", row.getStudentCount());
        params.put("teacherCount", row.getTeacherCount());
        params.put("dormitory", row.getDormitory());
        params.put("quarter", q);

        BigDecimal emission = carbonCalculator.calculate("教育区", params);
        points.add(buildPoint(row.getName(), "教育区", emission, row.getLon(), row.getLat()));

        Map<String, Object> rawItem = new HashMap<>(params);
        rawItem.put("name", row.getName());
        rawItem.put("category", "教育区");
        rawItem.put("year", row.getYear());
        rawItem.put("quarter", row.getQuarter());
        rawItem.put("lon", row.getLon());
        rawItem.put("lat", row.getLat());
        rawItem.put("emission", emission);
        rawDataList.add(rawItem);
    }

    // ==================== 工具方法 ====================
    private ImportError createError(int row, String field, String message) {
        ImportError error = new ImportError();
        error.setRow(row);
        error.setField(field);
        error.setMessage(message);
        return error;
    }

    private PreviewPoint buildPoint(String name, String category, BigDecimal emission,
                                    BigDecimal lon, BigDecimal lat) {
        PreviewPoint point = new PreviewPoint();
        point.setName(name);
        point.setCategory(category);
        point.setEmission(emission);
        point.setLon(lon);
        point.setLat(lat);
        return point;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean validLon(BigDecimal lon) {
        return lon != null && lon.compareTo(new BigDecimal("-180")) >= 0
                && lon.compareTo(new BigDecimal("180")) <= 0;
    }

    private boolean validLat(BigDecimal lat) {
        return lat != null && lat.compareTo(new BigDecimal("-90")) >= 0
                && lat.compareTo(new BigDecimal("90")) <= 0;
    }

    private boolean validYear(Integer year) {
        return year != null && year >= 2000 && year <= 2100;
    }

    private Integer quarterToIndex(String quarter) {
        return switch (quarter) {
            case "Q1" -> 0;
            case "Q2" -> 1;
            case "Q3" -> 2;
            case "Q4" -> 3;
            default -> null;
        };
    }
}
