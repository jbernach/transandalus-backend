package org.transandalus.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.transandalus.backend.domain.I18n;
import org.transandalus.backend.domain.Traveler;
import org.transandalus.backend.repository.TravelerRepository;
import org.transandalus.backend.web.rest.util.HeaderUtil;
import org.transandalus.backend.web.rest.util.PaginationUtil;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Traveler.
 */
@RestController
@RequestMapping("/api")
public class TravelerResource {

    private final Logger log = LoggerFactory.getLogger(TravelerResource.class);

    @Inject
    private TravelerRepository travelerRepository;

    /**
     * POST  /travelers : Create a new traveler.
     *
     * @param traveler the traveler to create
     * @return the ResponseEntity with status 201 (Created) and with body the new traveler, or with status 400 (Bad Request) if the traveler has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/travelers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Traveler> createTraveler(@Valid @RequestBody Traveler traveler) throws URISyntaxException {
        log.debug("REST request to save Traveler : {}", traveler);
        if (traveler.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("traveler", "idexists", "A new traveler cannot already have an ID")).body(null);
        }

        traveler.setI18nText(I18n.setTranslationText(traveler.getI18nText(), traveler.getText()));

        Traveler result = travelerRepository.save(traveler);
        return ResponseEntity.created(new URI("/api/travelers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("traveler", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /travelers : Updates an existing traveler.
     *
     * @param traveler the traveler to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated traveler,
     * or with status 400 (Bad Request) if the traveler is not valid,
     * or with status 500 (Internal Server Error) if the traveler couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/travelers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Traveler> updateTraveler(@Valid @RequestBody Traveler traveler) throws URISyntaxException {
        log.debug("REST request to update Traveler : {}", traveler);
        if (traveler.getId() == null) {
            return createTraveler(traveler);
        }

        Traveler result = travelerRepository.findOne(traveler.getId());

        result.setName(traveler.getName());
        result.setFrom(traveler.getFrom());
        result.setI18nText(I18n.setTranslationText(result.getI18nText(), traveler.getText()));
        result.setImage(traveler.getImage());

        result = travelerRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("traveler", traveler.getId().toString()))
            .body(result);
    }

    /**
     * GET  /travelers : get all the travelers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of travelers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/travelers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Traveler>> getAllTravelers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Travelers");
        Page<Traveler> page = travelerRepository.findAll(pageable);
        page.getContent().stream().forEach(p -> {
            p.resolveTraduction();
        });

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/travelers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /travelers/:id : get the "id" traveler.
     *
     * @param id the id of the traveler to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the traveler, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/travelers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Traveler> getTraveler(@PathVariable Long id) {
        log.debug("REST request to get Traveler : {}", id);
        Traveler traveler = travelerRepository.findOne(id);
        if(traveler != null){
            traveler.resolveTraduction();
        }
        return Optional.ofNullable(traveler)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /travelers/:id : delete the "id" traveler.
     *
     * @param id the id of the traveler to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/travelers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTraveler(@PathVariable Long id) {
        log.debug("REST request to delete Traveler : {}", id);
        travelerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("traveler", id.toString())).build();
    }

}
