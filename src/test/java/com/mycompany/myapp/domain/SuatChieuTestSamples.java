package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SuatChieuTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SuatChieu getSuatChieuSample1() {
        return new SuatChieu().id(1L).gioChieu("gioChieu1");
    }

    public static SuatChieu getSuatChieuSample2() {
        return new SuatChieu().id(2L).gioChieu("gioChieu2");
    }

    public static SuatChieu getSuatChieuRandomSampleGenerator() {
        return new SuatChieu().id(longCount.incrementAndGet()).gioChieu(UUID.randomUUID().toString());
    }
}
