package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SeatControl;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SeatControl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeatControlRepository extends JpaRepository<SeatControl, Long> {}
