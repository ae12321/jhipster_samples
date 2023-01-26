package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReservedSeat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReservedSeat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Long> {}
