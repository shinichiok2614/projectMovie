package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GheTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ghe getGheSample1() {
        return new Ghe().id(1L).tenGhe("tenGhe1");
    }

    public static Ghe getGheSample2() {
        return new Ghe().id(2L).tenGhe("tenGhe2");
    }

    public static Ghe getGheRandomSampleGenerator() {
        return new Ghe().id(longCount.incrementAndGet()).tenGhe(UUID.randomUUID().toString());
    }
}
