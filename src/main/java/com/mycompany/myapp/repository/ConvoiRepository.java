package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Convoi;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Convoi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConvoiRepository extends JpaRepository<Convoi, Long> {

}
