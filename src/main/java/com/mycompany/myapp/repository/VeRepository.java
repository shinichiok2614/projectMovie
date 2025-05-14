package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Ve;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ve entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VeRepository extends JpaRepository<Ve, Long> {}
