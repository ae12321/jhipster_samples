package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MainUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MainUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MainUserRepository extends JpaRepository<MainUser, Long> {}
