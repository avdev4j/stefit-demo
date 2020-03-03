package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Convoi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Convoi}.
 */
public interface ConvoiService {

    /**
     * Save a convoi.
     *
     * @param convoi the entity to save.
     * @return the persisted entity.
     */
    Convoi save(Convoi convoi);

    /**
     * Get all the convois.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Convoi> findAll(Pageable pageable);

    /**
     * Get the "id" convoi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Convoi> findOne(Long id);

    /**
     * Delete the "id" convoi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
