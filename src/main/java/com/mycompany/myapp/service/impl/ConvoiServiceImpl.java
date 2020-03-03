package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.ConvoiService;
import com.mycompany.myapp.domain.Convoi;
import com.mycompany.myapp.repository.ConvoiRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Convoi}.
 */
@Service
@Transactional
public class ConvoiServiceImpl implements ConvoiService {

    private final Logger log = LoggerFactory.getLogger(ConvoiServiceImpl.class);

    private final ConvoiRepository convoiRepository;

    public ConvoiServiceImpl(ConvoiRepository convoiRepository) {
        this.convoiRepository = convoiRepository;
    }

    /**
     * Save a convoi.
     *
     * @param convoi the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Convoi save(Convoi convoi) {
        log.debug("Request to save Convoi : {}", convoi);
        return convoiRepository.save(convoi);
    }

    /**
     * Get all the convois.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Convoi> findAll(Pageable pageable) {
        log.debug("Request to get all Convois");
        return convoiRepository.findAll(pageable);
    }

    /**
     * Get one convoi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Convoi> findOne(Long id) {
        log.debug("Request to get Convoi : {}", id);
        return convoiRepository.findById(id);
    }

    /**
     * Delete the convoi by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Convoi : {}", id);
        convoiRepository.deleteById(id);
    }
}
