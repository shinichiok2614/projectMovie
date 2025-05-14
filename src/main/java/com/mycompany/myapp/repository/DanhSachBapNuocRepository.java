package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.DanhSachBapNuoc;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DanhSachBapNuoc entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DanhSachBapNuocRepository extends JpaRepository<DanhSachBapNuoc, Long> {}
