package com.test.twincarbonboot;

import com.test.twincarbonboot.properties.CarbonProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CarbonPropertiesTest {

    @Autowired
    private CarbonProperties carbonProperties;

    @Test
    public void testLoad() {
        System.out.println("sceneUrl: " + carbonProperties.getSceneUrl());
        System.out.println("elec factor: " + carbonProperties.getEmissionFactors().getElectricity());
        System.out.println("res elec intensity: " + carbonProperties.getResidential().getElecIntensity());
        System.out.println("res quarter elec: " + carbonProperties.getResidential().getQuarterFactors().getElec());
        System.out.println("agri ch4 ef: " + carbonProperties.getAgricultural().getCh4Ef());
        System.out.println("agri quarter ch4: " + carbonProperties.getAgricultural().getQuarterFactors().getCh4());
        System.out.println("edu per-capita: " + carbonProperties.getEducational().getPerCapita());
    }
}