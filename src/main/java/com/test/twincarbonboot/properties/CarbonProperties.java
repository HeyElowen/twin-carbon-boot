package com.test.twincarbonboot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "carbon")
public class CarbonProperties {

    private String sceneUrl;
    private EmissionFactors emissionFactors;
    private Residential residential;
    private Commercial commercial;
    private Industrial industrial;
    private Agricultural agricultural;
    private Educational educational;

    @Data
    public static class EmissionFactors {
        private BigDecimal electricity;
        private BigDecimal gas;
        private BigDecimal diesel;
    }

    @Data
    public static class Residential {
        private Map<String, BigDecimal> elecIntensity;   // key: "before-1980"
        private Map<String, BigDecimal> gasIntensity;
        private Map<String, BigDecimal> heatEf;          // key: "air-pump"
        private QuarterFactors quarterFactors;
        private Waste waste;
    }

    @Data
    public static class QuarterFactors {
        private List<BigDecimal> elec;   // [Q1, Q2, Q3, Q4]
        private List<BigDecimal> gas;
        private List<BigDecimal> heat;
    }

    @Data
    public static class Waste {
        private BigDecimal perCapitaDaily;
        private BigDecimal emissionFactor;
    }

    @Data
    public static class Commercial {
        private Map<String, BigDecimal> elecIntensity;
        private Map<String, BigDecimal> gasIntensity;
        private QuarterFactors quarterFactors;
    }

    @Data
    public static class Industrial {

        private Map<String, BigDecimal> elecIntensity;
        private QuarterFactors quarterFactors;

    }

    @Data
    public static class Agricultural {

        private Map<String, BigDecimal> ch4Ef;
        private BigDecimal  riceDays;
        private BigDecimal  dieselPerHa;
        private  BigDecimal fertilizerN2o;
        private AgriQuarterFactors quarterFactors;
    }

    @Data
    public static class AgriQuarterFactors {
        private List<BigDecimal> ch4;
        private List<BigDecimal> fertilizer;
        private List<BigDecimal> machinery;
        private List<BigDecimal> irrigation;
    }

    @Data
    public static class Educational {
        private Map<String, BigDecimal> elecIntensity;
        private Map<String, BigDecimal> perCapita;
        private QuarterFactors quarterFactors;
    }


}
