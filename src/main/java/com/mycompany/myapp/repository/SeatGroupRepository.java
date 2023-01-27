package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SeatGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SeatGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeatGroupRepository extends JpaRepository<SeatGroup, Long> {}
