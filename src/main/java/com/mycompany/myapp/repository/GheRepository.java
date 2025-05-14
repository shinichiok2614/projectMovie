package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Ghe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ghe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GheRepository extends JpaRepository<Ghe, Long> {}
