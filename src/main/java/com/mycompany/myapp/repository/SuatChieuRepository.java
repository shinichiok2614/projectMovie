package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SuatChieu;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SuatChieu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuatChieuRepository extends JpaRepository<SuatChieu, Long> {}
