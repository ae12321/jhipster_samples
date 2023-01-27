package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Representative;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Representative entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepresentativeRepository extends JpaRepository<Representative, Long> {}
