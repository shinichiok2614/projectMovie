package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DanhSachGheTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DanhSachGhe getDanhSachGheSample1() {
        return new DanhSachGhe().id(1L).soDienThoai("soDienThoai1").tenGhe("tenGhe1");
    }

    public static DanhSachGhe getDanhSachGheSample2() {
        return new DanhSachGhe().id(2L).soDienThoai("soDienThoai2").tenGhe("tenGhe2");
    }

    public static DanhSachGhe getDanhSachGheRandomSampleGenerator() {
        return new DanhSachGhe()
            .id(longCount.incrementAndGet())
            .soDienThoai(UUID.randomUUID().toString())
            .tenGhe(UUID.randomUUID().toString());
    }
}
