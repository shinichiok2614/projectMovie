package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Ve getVeSample1() {
        return new Ve().id(1L).soDienThoai("soDienThoai1").email("email1").giaTien(1);
    }

    public static Ve getVeSample2() {
        return new Ve().id(2L).soDienThoai("soDienThoai2").email("email2").giaTien(2);
    }

    public static Ve getVeRandomSampleGenerator() {
        return new Ve()
            .id(longCount.incrementAndGet())
            .soDienThoai(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .giaTien(intCount.incrementAndGet());
    }
}
