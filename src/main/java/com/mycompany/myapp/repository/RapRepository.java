package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Rap;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Rap entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RapRepository extends JpaRepository<Rap, Long> {}
