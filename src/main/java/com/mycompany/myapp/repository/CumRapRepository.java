package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CumRap;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CumRap entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CumRapRepository extends JpaRepository<CumRap, Long> {}
