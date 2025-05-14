package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Phim;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Phim entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhimRepository extends JpaRepository<Phim, Long> {}
