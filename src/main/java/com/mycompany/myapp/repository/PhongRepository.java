package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Phong;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Phong entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhongRepository extends JpaRepository<Phong, Long> {}
