package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Convoi;
import com.mycompany.myapp.service.ConvoiService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Convoi}.
 */
@RestController
@RequestMapping("/api")
public class ConvoiResource {

    private final Logger log = LoggerFactory.getLogger(ConvoiResource.class);

    private static final String ENTITY_NAME = "convoi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConvoiService convoiService;

    public ConvoiResource(ConvoiService convoiService) {
        this.convoiService = convoiService;
    }

    /**
     * {@code POST  /convois} : Create a new convoi.
     *
     * @param convoi the convoi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new convoi, or with status {@code 400 (Bad Request)} if the convoi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/convois")
    public ResponseEntity<Convoi> createConvoi(@RequestBody Convoi convoi) throws URISyntaxException {
        log.debug("REST request to save Convoi : {}", convoi);
        if (convoi.getId() != null) {
            throw new BadRequestAlertException("A new convoi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Convoi result = convoiService.save(convoi);
        return ResponseEntity.created(new URI("/api/convois/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /convois} : Updates an existing convoi.
     *
     * @param convoi the convoi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated convoi,
     * or with status {@code 400 (Bad Request)} if the convoi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the convoi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/convois")
    public ResponseEntity<Convoi> updateConvoi(@RequestBody Convoi convoi) throws URISyntaxException {
        log.debug("REST request to update Convoi : {}", convoi);
        if (convoi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Convoi result = convoiService.save(convoi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, convoi.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /convois} : get all the convois.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of convois in body.
     */
    @GetMapping("/convois")
    public ResponseEntity<List<Convoi>> getAllConvois(Pageable pageable) {
        log.debug("REST request to get a page of Convois");
        Page<Convoi> page = convoiService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /convois/:id} : get the "id" convoi.
     *
     * @param id the id of the convoi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the convoi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/convois/{id}")
    public ResponseEntity<Convoi> getConvoi(@PathVariable Long id) {
        log.debug("REST request to get Convoi : {}", id);
        Optional<Convoi> convoi = convoiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(convoi);
    }

    /**
     * {@code DELETE  /convois/:id} : delete the "id" convoi.
     *
     * @param id the id of the convoi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/convois/{id}")
    public ResponseEntity<Void> deleteConvoi(@PathVariable Long id) {
        log.debug("REST request to delete Convoi : {}", id);
        convoiService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
