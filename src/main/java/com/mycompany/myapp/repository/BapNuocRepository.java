package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BapNuoc;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BapNuoc entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BapNuocRepository extends JpaRepository<BapNuoc, Long> {}
