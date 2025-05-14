package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DanhSachBapNuocTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DanhSachBapNuoc getDanhSachBapNuocSample1() {
        return new DanhSachBapNuoc().id(1L).soDienThoai("soDienThoai1").tenBapNuoc("tenBapNuoc1");
    }

    public static DanhSachBapNuoc getDanhSachBapNuocSample2() {
        return new DanhSachBapNuoc().id(2L).soDienThoai("soDienThoai2").tenBapNuoc("tenBapNuoc2");
    }

    public static DanhSachBapNuoc getDanhSachBapNuocRandomSampleGenerator() {
        return new DanhSachBapNuoc()
            .id(longCount.incrementAndGet())
            .soDienThoai(UUID.randomUUID().toString())
            .tenBapNuoc(UUID.randomUUID().toString());
    }
}
