package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PhongTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Phong getPhongSample1() {
        return new Phong().id(1L).tenPhong("tenPhong1");
    }

    public static Phong getPhongSample2() {
        return new Phong().id(2L).tenPhong("tenPhong2");
    }

    public static Phong getPhongRandomSampleGenerator() {
        return new Phong().id(longCount.incrementAndGet()).tenPhong(UUID.randomUUID().toString());
    }
}
