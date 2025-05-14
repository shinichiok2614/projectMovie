package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RapTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Rap getRapSample1() {
        return new Rap().id(1L).tenRap("tenRap1").diaChi("diaChi1").thanhPho("thanhPho1");
    }

    public static Rap getRapSample2() {
        return new Rap().id(2L).tenRap("tenRap2").diaChi("diaChi2").thanhPho("thanhPho2");
    }

    public static Rap getRapRandomSampleGenerator() {
        return new Rap()
            .id(longCount.incrementAndGet())
            .tenRap(UUID.randomUUID().toString())
            .diaChi(UUID.randomUUID().toString())
            .thanhPho(UUID.randomUUID().toString());
    }
}
