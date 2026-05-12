package com.test.twincarbonboot;

import com.test.twincarbonboot.cache.PreviewCache;
import com.test.twincarbonboot.calculator.CarbonCalculator;
import com.test.twincarbonboot.exception.MonitoringException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CarbonCalculatorTest {

    @Autowired
    private CarbonCalculator carbonCalculator;

    // ==================== 住宅区 ====================
    @Test
    public void testResidentialQ1() {
        Map<String, Object> params = new HashMap<>();
        params.put("area", new BigDecimal("100"));
        params.put("year", "2000-2010");
        params.put("population", 3);
        params.put("heatDevice", "air-pump");
        params.put("quarter", 0);  // Q1

        BigDecimal result = carbonCalculator.calculate("住宅区", params);
        BigDecimal expected = new BigDecimal("1.1551");
        assertEquals(0, result.compareTo(expected));
        System.out.println("住宅区 Q1 排放: " + result);
    }

    @Test
    public void testResidentialQ3() {
        Map<String, Object> params = new HashMap<>();
        params.put("area", new BigDecimal("100"));
        params.put("year", "2000-2010");
        params.put("population", 3);
        params.put("heatDevice", "air-pump");
        params.put("quarter", 2);  // Q3

        BigDecimal result = carbonCalculator.calculate("住宅区", params);
        // Q3 heatFactor=0, 所以无采暖排放
        BigDecimal expected = new BigDecimal("0.9042");
        assertEquals(0, result.compareTo(expected));
        System.out.println("住宅区 Q3 排放: " + result);
    }

    // ==================== 商业区 ====================
    @Test
    public void testCommercialQ1() {
        Map<String, Object> params = new HashMap<>();
        params.put("area", new BigDecimal("5000"));
        params.put("subType", "mall");
        params.put("quarter", 0);  // Q1

        BigDecimal result = carbonCalculator.calculate("商业区", params);
        BigDecimal expected = new BigDecimal("115.8899");
        assertEquals(0, result.compareTo(expected));
        System.out.println("商业区 Q1 排放: " + result);
    }

    // ==================== 工业区 ====================
    @Test
    public void testIndustrialWithGas() {
        Map<String, Object> params = new HashMap<>();
        params.put("area", new BigDecimal("8000"));
        params.put("industryType", "machinery");
        params.put("gasConsumption", new BigDecimal("1200.5"));
        params.put("quarter", 0);  // Q1

        BigDecimal result = carbonCalculator.calculate("工业区", params);
        BigDecimal expected = new BigDecimal("184.0401");
        assertEquals(0, result.compareTo(expected));
        System.out.println("工业区(有燃气) Q1 排放: " + result);
    }

    @Test
    public void testIndustrialWithoutGas() {
        Map<String, Object> params = new HashMap<>();
        params.put("area", new BigDecimal("8000"));
        params.put("industryType", "machinery");
        // gasConsumption 不传，走估算
        params.put("quarter", 1);  // Q2

        BigDecimal result = carbonCalculator.calculate("工业区", params);
        BigDecimal expected = new BigDecimal("283.9363");
        assertEquals(0, result.compareTo(expected));
        System.out.println("工业区(无燃气估算) Q2 排放: " + result);
    }

    // ==================== 农业区 ====================
    @Test
    public void testAgriculturalQ2() {
        Map<String, Object> params = new HashMap<>();
        params.put("area", new BigDecimal("10"));           // 水稻种植面积
        params.put("mechArea", new BigDecimal("10"));       // 机械作业面积
        params.put("fertilizer", new BigDecimal("50"));     // 化肥施用量
        params.put("irrigationElec", new BigDecimal("2000")); // 灌溉用电
        params.put("waterMode", "intermittent");
        params.put("quarter", 1);  // Q2

        BigDecimal result = carbonCalculator.calculate("农业区", params);
        BigDecimal expected = new BigDecimal("8.2484");
        assertEquals(0, result.compareTo(expected));
        System.out.println("农业区 Q2 排放: " + result);
    }

    // ==================== 教育区 ====================
    @Test
    public void testEducationalQ1WithDormitory() {
        Map<String, Object> params = new HashMap<>();
        params.put("area", new BigDecimal("15000"));
        params.put("schoolType", "high");
        params.put("studentCount", 1200);
        params.put("teacherCount", 150);
        params.put("dormitory", "yes");
        params.put("quarter", 0);  // Q1

        BigDecimal result = carbonCalculator.calculate("教育区", params);
        BigDecimal expected = new BigDecimal("325.5864");
        assertEquals(0, result.compareTo(expected));
        System.out.println("教育区(住宿制) Q1 排放: " + result);
    }

    @Test
    public void testEducationalQ1WithoutDormitory() {
        Map<String, Object> params = new HashMap<>();
        params.put("area", new BigDecimal("15000"));
        params.put("schoolType", "high");
        params.put("studentCount", 1200);
        params.put("teacherCount", 150);
        params.put("dormitory", "no");
        params.put("quarter", 0);  // Q1

        BigDecimal result = carbonCalculator.calculate("教育区", params);
        // 比住宿制少 dormitoryEmission = 29.7000
        BigDecimal expected = new BigDecimal("295.8864");
        assertEquals(0, result.compareTo(expected));
        System.out.println("教育区(非住宿制) Q1 排放: " + result);
    }

    // ==================== 异常场景 ====================
    @Test
    public void testUnknownCategory() {
        Map<String, Object> params = new HashMap<>();
        assertThrows(MonitoringException.class, () -> {
            carbonCalculator.calculate("未知类型", params);
        });
    }


}
