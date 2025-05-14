package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.DanhSachGhe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DanhSachGhe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DanhSachGheRepository extends JpaRepository<DanhSachGhe, Long> {}
