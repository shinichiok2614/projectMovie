package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LoaiGheTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static LoaiGhe getLoaiGheSample1() {
        return new LoaiGhe().id(1L).tenLoai("tenLoai1").giaTien(1);
    }

    public static LoaiGhe getLoaiGheSample2() {
        return new LoaiGhe().id(2L).tenLoai("tenLoai2").giaTien(2);
    }

    public static LoaiGhe getLoaiGheRandomSampleGenerator() {
        return new LoaiGhe().id(longCount.incrementAndGet()).tenLoai(UUID.randomUUID().toString()).giaTien(intCount.incrementAndGet());
    }
}
