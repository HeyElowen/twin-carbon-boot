package com.test.twincarbonboot.calculator;

import com.test.twincarbonboot.exception.MonitoringException;
import com.test.twincarbonboot.properties.CarbonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class CarbonCalculator {


    @Autowired
    private CarbonProperties carbonProperties;

    private static final BigDecimal EF_ELEC = new BigDecimal("0.5827");
    private static final BigDecimal EF_GAS = new BigDecimal("1.864");
    private static final BigDecimal EF_DIESEL = new BigDecimal("3.15");
    private static final BigDecimal RES_HEAT_LOAD = new BigDecimal("15");  // 住宅区采暖负荷
    private static final BigDecimal EDU_GAS_INTENSITY = new BigDecimal("3.0");  // 教育区燃气强度
    private static final BigDecimal DIV_1000 = new BigDecimal("1000");
    private static final BigDecimal IND_GAS_ESTIMATE = new BigDecimal("2.0");

    // ========== 主入口 ==========
    public BigDecimal calculate(String category, Map<String, Object> params) {
        switch (category) {
            case "住宅区": return calculateResidential(params);
            case "商业区": return calculateCommercial(params);
            case "工业区": return calculateIndustrial(params);
            case "农业区": return calculateAgricultural(params);
            case "教育区": return calculateEducational(params);
            default: throw new MonitoringException("未知用地类型: " + category);
        }
    }


    // ========== 5 个私有计算方法 ==========
    private BigDecimal calculateResidential(Map<String, Object> params) {
        BigDecimal area = (BigDecimal) params.get("area");
        String year = (String) params.get("year");
        Integer population = (Integer) params.get("population");
        String heatDevice = (String) params.get("heatDevice");
        int q = (Integer) params.get("quarter");

        // 从配置查表
        BigDecimal elecIntensity = carbonProperties.getResidential().getElecIntensity().get(year);
        BigDecimal gasIntensity = carbonProperties.getResidential().getGasIntensity().get(year);
        BigDecimal heatEF = carbonProperties.getResidential().getHeatEf().get(heatDevice);
        BigDecimal elecFactor = carbonProperties.getResidential().getQuarterFactors().getElec().get(q);
        BigDecimal gasFactor = carbonProperties.getResidential().getQuarterFactors().getGas().get(q);
        BigDecimal heatFactor = carbonProperties.getResidential().getQuarterFactors().getHeat().get(q);

        // 计算（注意 BigDecimal 的用法）
        BigDecimal elecEmission=area.multiply(elecIntensity).multiply(elecFactor).multiply(EF_ELEC).divide(DIV_1000,4, RoundingMode.HALF_UP);
        BigDecimal gasEmission =area.multiply(gasFactor).multiply(gasIntensity).multiply(EF_GAS).divide(DIV_1000,4, RoundingMode.HALF_UP);
        BigDecimal heatEmission=area.multiply(heatEF).multiply(heatFactor).multiply(RES_HEAT_LOAD).divide(DIV_1000,4, RoundingMode.HALF_UP);
        BigDecimal wasteEmission= new BigDecimal(population.toString())
                .multiply(new BigDecimal("0.0495"));

        // 汇总
        return elecEmission.add(gasEmission).add(heatEmission).add(wasteEmission);
    }
    private BigDecimal calculateCommercial(Map<String, Object> params) {

        BigDecimal area=(BigDecimal) params.get("area");
        String subType = (String) params.get("subType");
        Integer q = (Integer) params.get("quarter");

        // 从 carbonProperties 获取：
        BigDecimal elecIntensity = carbonProperties.getCommercial().getElecIntensity().get(subType);
        BigDecimal gasIntensity = carbonProperties.getCommercial().getGasIntensity().get(subType);
        BigDecimal elecFactor = carbonProperties.getCommercial().getQuarterFactors().getElec().get(q);
        BigDecimal gasFactor = carbonProperties.getCommercial().getQuarterFactors().getGas().get(q);

        //计算排放
        BigDecimal elecEmission=area.multiply(elecIntensity).multiply(elecFactor).multiply(EF_ELEC).divide(DIV_1000,4, RoundingMode.HALF_UP);
        BigDecimal gasEmission =area.multiply(gasFactor).multiply(gasIntensity).multiply(EF_GAS).divide(DIV_1000,4, RoundingMode.HALF_UP);

        //返回计算结果
        return elecEmission.add(gasEmission);

    }
    private BigDecimal calculateIndustrial(Map<String, Object> params) {
        //从params提取参数
        BigDecimal area = (BigDecimal) params.get("area");
        String industryType = (String) params.get("industryType");
        BigDecimal gasConsumption = (BigDecimal) params.get("gasConsumption");  // 可能为 null
        int q = (Integer) params.get("quarter");

        //查配置
        BigDecimal elecIntensity = carbonProperties.getIndustrial().getElecIntensity().get(industryType);
        BigDecimal elecFactor = carbonProperties.getIndustrial().getQuarterFactors().getElec().get(q);

        //根据条件采用计算公式
        BigDecimal gasEmission;
        if (gasConsumption != null) {
            // 用户填了实际消耗量：G × 1.864 / 1000
            gasEmission = gasConsumption.multiply(EF_GAS).divide(DIV_1000, 4, RoundingMode.HALF_UP);
        } else {
            // 没填，按文档估算：A × 2.0 × elecFactor[q] × 1.864 / 1000
            gasEmission = area.multiply(IND_GAS_ESTIMATE).multiply(elecFactor).multiply(EF_GAS).divide(DIV_1000, 4, RoundingMode.HALF_UP);
        }
        BigDecimal elecEmission = area.multiply(elecIntensity).multiply(elecFactor).multiply(EF_ELEC).divide(DIV_1000, 4, RoundingMode.HALF_UP);
        return elecEmission.add(gasEmission);

    }
    private BigDecimal calculateAgricultural(Map<String, Object> params) {
        //第 1 步：从 params 取参数
        BigDecimal area = (BigDecimal) params.get("area");              // 水稻种植面积（ha）
        BigDecimal mechArea = (BigDecimal) params.get("mechArea");      // 该季度机械作业面积（ha）
        BigDecimal fertilizer = (BigDecimal) params.get("fertilizer");  // 该季度化肥施用量（kg N）
        BigDecimal irrigationElec = (BigDecimal) params.get("irrigationElec");  // 该季度灌溉用电量（kWh）
        String waterMode = (String) params.get("waterMode");            // continuous / intermittent / dry
        int q = (Integer) params.get("quarter");
        //第 2 步：查配置
        BigDecimal ch4EF = carbonProperties.getAgricultural().getCh4Ef().get(waterMode);
        BigDecimal riceDays = carbonProperties.getAgricultural().getRiceDays();        // 135
        BigDecimal dieselPerHa = carbonProperties.getAgricultural().getDieselPerHa();  // 12
        BigDecimal fertilizerN2o = carbonProperties.getAgricultural().getFertilizerN2o(); // 0.0018

        BigDecimal ch4Factor = carbonProperties.getAgricultural().getQuarterFactors().getCh4().get(q);
        // machineryFactor / irrigationFactor / fertilizerFactor 这里不需要！原因见下方
        //第 3 步：按公式计算 4 个子项
        // 1) 稻田甲烷(吨CO₂e) = A × ch4EF × 135 × ch4Factor[q] × 28 / 1000
        //    注意：area 是全年种植面积，所以要乘 ch4Factor 拆分到该季度
        BigDecimal ch4Emission = area.multiply(ch4EF).multiply(riceDays).multiply(ch4Factor)
                .multiply(new BigDecimal("28")).divide(DIV_1000, 4, RoundingMode.HALF_UP);

        // 2) 化肥N₂O(吨CO₂e) = F × 0.0018
        //    注意：fertilizer 已经是"该季度化肥施用量"，不需要再乘 fertilizerFactor！
        BigDecimal fertilizerEmission = fertilizer.multiply(fertilizerN2o);

        // 3) 机械柴油(吨CO₂) = M × 12 × 3.15 / 1000
        //    注意：mechArea 已经是"该季度机械作业面积"，不需要再乘 machineryFactor！
        BigDecimal machineryEmission = mechArea.multiply(dieselPerHa).multiply(EF_DIESEL)
                .divide(DIV_1000, 4, RoundingMode.HALF_UP);

        // 4) 灌溉电力(吨CO₂) = W × 0.5827 / 1000
        //    注意：irrigationElec 已经是"该季度灌溉用电量"，不需要再乘 irrigationFactor！
        BigDecimal irrigationEmission = irrigationElec.multiply(EF_ELEC)
                .divide(DIV_1000, 4, RoundingMode.HALF_UP);
        //第 4 步：汇总返回
        return ch4Emission.add(fertilizerEmission).add(machineryEmission).add(irrigationEmission);

    }
    private BigDecimal calculateEducational(Map<String, Object> params) {

        //从 params 取参数
        BigDecimal area = (BigDecimal) params.get("area");
        String schoolType = (String) params.get("schoolType");    // primary / middle / high / vocational / university
        Integer studentCount = (Integer) params.get("studentCount");
        Integer teacherCount = (Integer) params.get("teacherCount");
        String dormitory = (String) params.get("dormitory");      // "yes" 或 "no"
        int q = (Integer) params.get("quarter");

        //查配置
        BigDecimal elecIntensity = carbonProperties.getEducational().getElecIntensity().get(schoolType);
        BigDecimal perCapitaFactor = carbonProperties.getEducational().getPerCapita().get(schoolType);
        BigDecimal elecFactor = carbonProperties.getEducational().getQuarterFactors().getElec().get(q);
        BigDecimal gasFactor = carbonProperties.getEducational().getQuarterFactors().getGas().get(q);

        //计算

        // 基础电力排放 = A × elecIntensity × elecFactor[q] × 0.5827 / 1000
        BigDecimal elecEmission = area.multiply(elecIntensity).multiply(elecFactor).multiply(EF_ELEC).divide(DIV_1000, 4, RoundingMode.HALF_UP);

        // 基础燃气排放 = A × 3.0 × gasFactor[q] × 1.864 / 1000
        // 类里已经有常量 EDU_GAS_INTENSITY = 3.0，直接用
        BigDecimal gasEmission = area.multiply(EDU_GAS_INTENSITY).multiply(gasFactor).multiply(EF_GAS).divide(DIV_1000, 4, RoundingMode.HALF_UP);

        // 人员排放 = (Stu + Tea × 2) × perCapitaFactor × elecFactor[q]
        BigDecimal personCount = new BigDecimal(studentCount.toString())
                .add(new BigDecimal(teacherCount.toString()).multiply(new BigDecimal("2")));
        BigDecimal personEmission = personCount.multiply(perCapitaFactor).multiply(elecFactor);

        // 住宿附加 = D=="yes" ? (Stu + Tea) × 0.10 × elecFactor[q] : 0
        BigDecimal dormitoryEmission = BigDecimal.ZERO;
        if ("yes".equals(dormitory)) {
            BigDecimal totalPeople = new BigDecimal(studentCount.toString())
                    .add(new BigDecimal(teacherCount.toString()));
            dormitoryEmission = totalPeople.multiply(new BigDecimal("0.10")).multiply(elecFactor);
        }

        // 总排放
        return elecEmission.add(gasEmission).add(personEmission).add(dormitoryEmission);
    }

}
