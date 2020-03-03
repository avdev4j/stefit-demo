package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Livraison;
import com.mycompany.myapp.service.LivraisonService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Livraison}.
 */
@RestController
@RequestMapping("/api")
public class LivraisonResource {

    private final Logger log = LoggerFactory.getLogger(LivraisonResource.class);

    private static final String ENTITY_NAME = "livraison";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LivraisonService livraisonService;

    public LivraisonResource(LivraisonService livraisonService) {
        this.livraisonService = livraisonService;
    }

    /**
     * {@code POST  /livraisons} : Create a new livraison.
     *
     * @param livraison the livraison to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new livraison, or with status {@code 400 (Bad Request)} if the livraison has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/livraisons")
    public ResponseEntity<Livraison> createLivraison(@RequestBody Livraison livraison) throws URISyntaxException {
        log.debug("REST request to save Livraison : {}", livraison);
        if (livraison.getId() != null) {
            throw new BadRequestAlertException("A new livraison cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Livraison result = livraisonService.save(livraison);
        return ResponseEntity.created(new URI("/api/livraisons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /livraisons} : Updates an existing livraison.
     *
     * @param livraison the livraison to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livraison,
     * or with status {@code 400 (Bad Request)} if the livraison is not valid,
     * or with status {@code 500 (Internal Server Error)} if the livraison couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/livraisons")
    public ResponseEntity<Livraison> updateLivraison(@RequestBody Livraison livraison) throws URISyntaxException {
        log.debug("REST request to update Livraison : {}", livraison);
        if (livraison.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Livraison result = livraisonService.save(livraison);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livraison.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /livraisons} : get all the livraisons.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of livraisons in body.
     */
    @GetMapping("/livraisons")
    public ResponseEntity<List<Livraison>> getAllLivraisons(Pageable pageable) {
        log.debug("REST request to get a page of Livraisons");
        Page<Livraison> page = livraisonService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /livraisons/:id} : get the "id" livraison.
     *
     * @param id the id of the livraison to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the livraison, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/livraisons/{id}")
    public ResponseEntity<Livraison> getLivraison(@PathVariable Long id) {
        log.debug("REST request to get Livraison : {}", id);
        Optional<Livraison> livraison = livraisonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(livraison);
    }

    /**
     * {@code DELETE  /livraisons/:id} : delete the "id" livraison.
     *
     * @param id the id of the livraison to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/livraisons/{id}")
    public ResponseEntity<Void> deleteLivraison(@PathVariable Long id) {
        log.debug("REST request to delete Livraison : {}", id);
        livraisonService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
