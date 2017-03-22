package com.pdb.eventsearch.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pdb.eventsearch.domain.Prelegent;

import com.pdb.eventsearch.repository.PrelegentRepository;
import com.pdb.eventsearch.repository.search.PrelegentSearchRepository;
import com.pdb.eventsearch.web.rest.util.HeaderUtil;
import com.pdb.eventsearch.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Prelegent.
 */
@RestController
@RequestMapping("/api")
public class PrelegentResource {

    private final Logger log = LoggerFactory.getLogger(PrelegentResource.class);

    private static final String ENTITY_NAME = "prelegent";
        
    private final PrelegentRepository prelegentRepository;

    private final PrelegentSearchRepository prelegentSearchRepository;

    public PrelegentResource(PrelegentRepository prelegentRepository, PrelegentSearchRepository prelegentSearchRepository) {
        this.prelegentRepository = prelegentRepository;
        this.prelegentSearchRepository = prelegentSearchRepository;
    }

    /**
     * POST  /prelegents : Create a new prelegent.
     *
     * @param prelegent the prelegent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new prelegent, or with status 400 (Bad Request) if the prelegent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/prelegents")
    @Timed
    public ResponseEntity<Prelegent> createPrelegent(@Valid @RequestBody Prelegent prelegent) throws URISyntaxException {
        log.debug("REST request to save Prelegent : {}", prelegent);
        if (prelegent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new prelegent cannot already have an ID")).body(null);
        }
        Prelegent result = prelegentRepository.save(prelegent);
        prelegentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/prelegents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prelegents : Updates an existing prelegent.
     *
     * @param prelegent the prelegent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated prelegent,
     * or with status 400 (Bad Request) if the prelegent is not valid,
     * or with status 500 (Internal Server Error) if the prelegent couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/prelegents")
    @Timed
    public ResponseEntity<Prelegent> updatePrelegent(@Valid @RequestBody Prelegent prelegent) throws URISyntaxException {
        log.debug("REST request to update Prelegent : {}", prelegent);
        if (prelegent.getId() == null) {
            return createPrelegent(prelegent);
        }
        Prelegent result = prelegentRepository.save(prelegent);
        prelegentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, prelegent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prelegents : get all the prelegents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of prelegents in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/prelegents")
    @Timed
    public ResponseEntity<List<Prelegent>> getAllPrelegents(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Prelegents");
        Page<Prelegent> page = prelegentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prelegents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /prelegents/:id : get the "id" prelegent.
     *
     * @param id the id of the prelegent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the prelegent, or with status 404 (Not Found)
     */
    @GetMapping("/prelegents/{id}")
    @Timed
    public ResponseEntity<Prelegent> getPrelegent(@PathVariable Long id) {
        log.debug("REST request to get Prelegent : {}", id);
        Prelegent prelegent = prelegentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(prelegent));
    }

    /**
     * DELETE  /prelegents/:id : delete the "id" prelegent.
     *
     * @param id the id of the prelegent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/prelegents/{id}")
    @Timed
    public ResponseEntity<Void> deletePrelegent(@PathVariable Long id) {
        log.debug("REST request to delete Prelegent : {}", id);
        prelegentRepository.delete(id);
        prelegentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/prelegents?query=:query : search for the prelegent corresponding
     * to the query.
     *
     * @param query the query of the prelegent search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/prelegents")
    @Timed
    public ResponseEntity<List<Prelegent>> searchPrelegents(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Prelegents for query {}", query);
        Page<Prelegent> page = prelegentSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/prelegents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
