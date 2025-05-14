package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CumRapTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CumRap getCumRapSample1() {
        return new CumRap().id(1L).tenCumRap("tenCumRap1");
    }

    public static CumRap getCumRapSample2() {
        return new CumRap().id(2L).tenCumRap("tenCumRap2");
    }

    public static CumRap getCumRapRandomSampleGenerator() {
        return new CumRap().id(longCount.incrementAndGet()).tenCumRap(UUID.randomUUID().toString());
    }
}
