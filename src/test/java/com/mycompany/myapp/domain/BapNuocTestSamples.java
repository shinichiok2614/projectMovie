package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BapNuocTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BapNuoc getBapNuocSample1() {
        return new BapNuoc().id(1L).tenBapNuoc("tenBapNuoc1").giaTien(1);
    }

    public static BapNuoc getBapNuocSample2() {
        return new BapNuoc().id(2L).tenBapNuoc("tenBapNuoc2").giaTien(2);
    }

    public static BapNuoc getBapNuocRandomSampleGenerator() {
        return new BapNuoc().id(longCount.incrementAndGet()).tenBapNuoc(UUID.randomUUID().toString()).giaTien(intCount.incrementAndGet());
    }
}
