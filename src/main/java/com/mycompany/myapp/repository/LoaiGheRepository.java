package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LoaiGhe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LoaiGhe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoaiGheRepository extends JpaRepository<LoaiGhe, Long> {}
