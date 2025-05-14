package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PhimTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Phim getPhimSample1() {
        return new Phim()
            .id(1L)
            .tenPhim("tenPhim1")
            .thoiLuong(1)
            .gioiThieu("gioiThieu1")
            .linkTrailer("linkTrailer1")
            .doTuoi("doTuoi1")
            .dinhDang("dinhDang1");
    }

    public static Phim getPhimSample2() {
        return new Phim()
            .id(2L)
            .tenPhim("tenPhim2")
            .thoiLuong(2)
            .gioiThieu("gioiThieu2")
            .linkTrailer("linkTrailer2")
            .doTuoi("doTuoi2")
            .dinhDang("dinhDang2");
    }

    public static Phim getPhimRandomSampleGenerator() {
        return new Phim()
            .id(longCount.incrementAndGet())
            .tenPhim(UUID.randomUUID().toString())
            .thoiLuong(intCount.incrementAndGet())
            .gioiThieu(UUID.randomUUID().toString())
            .linkTrailer(UUID.randomUUID().toString())
            .doTuoi(UUID.randomUUID().toString())
            .dinhDang(UUID.randomUUID().toString());
    }
}
