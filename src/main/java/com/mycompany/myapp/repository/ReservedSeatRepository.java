package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReservedSeat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReservedSeat entity.
 */
@Repository
public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Long> {
    default Optional<ReservedSeat> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReservedSeat> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReservedSeat> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct reservedSeat from ReservedSeat reservedSeat left join fetch reservedSeat.mainUser",
        countQuery = "select count(distinct reservedSeat) from ReservedSeat reservedSeat"
    )
    Page<ReservedSeat> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct reservedSeat from ReservedSeat reservedSeat left join fetch reservedSeat.mainUser")
    List<ReservedSeat> findAllWithToOneRelationships();

    @Query("select reservedSeat from ReservedSeat reservedSeat left join fetch reservedSeat.mainUser where reservedSeat.id =:id")
    Optional<ReservedSeat> findOneWithToOneRelationships(@Param("id") Long id);
}
