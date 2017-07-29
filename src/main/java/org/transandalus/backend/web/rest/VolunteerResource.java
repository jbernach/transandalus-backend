package org.transandalus.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.transandalus.backend.domain.I18n;
import org.transandalus.backend.domain.Volunteer;
import org.transandalus.backend.repository.VolunteerRepository;
import org.transandalus.backend.web.rest.util.HeaderUtil;
import org.transandalus.backend.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Volunteer.
 */
@RestController
@RequestMapping("/api")
public class VolunteerResource {

    private final Logger log = LoggerFactory.getLogger(VolunteerResource.class);

    @Inject
    private VolunteerRepository volunteerRepository;

    /**
     * POST  /volunteers : Create a new volunteer.
     *
     * @param volunteer the volunteer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new volunteer, or with status 400 (Bad Request) if the volunteer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/volunteers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Volunteer> createVolunteer(@Valid @RequestBody Volunteer volunteer) throws URISyntaxException {
        log.debug("REST request to save Volunteer : {}", volunteer);
        if (volunteer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("volunteer", "idexists", "A new volunteer cannot already have an ID")).body(null);
        }

        volunteer.setI18nText(I18n.setTranslationText(volunteer.getI18nText(), volunteer.getText()));

        Volunteer result = volunteerRepository.save(volunteer);
        return ResponseEntity.created(new URI("/api/volunteers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("volunteer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /volunteers : Updates an existing volunteer.
     *
     * @param volunteer the volunteer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated volunteer,
     * or with status 400 (Bad Request) if the volunteer is not valid,
     * or with status 500 (Internal Server Error) if the volunteer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/volunteers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Volunteer> updateVolunteer(@Valid @RequestBody Volunteer volunteer) throws URISyntaxException {
        log.debug("REST request to update Volunteer : {}", volunteer);
        if (volunteer.getId() == null) {
            return createVolunteer(volunteer);
        }

        Volunteer result = volunteerRepository.findOne(volunteer.getId());

        result.setName(volunteer.getName());
        result.setI18nText(I18n.setTranslationText(result.getI18nText(), volunteer.getText()));
        result.setImage(volunteer.getImage());

        result = volunteerRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("volunteer", volunteer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /volunteers : get all the volunteers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of volunteers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/volunteers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Volunteer>> getAllVolunteers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Volunteers");
        Page<Volunteer> page = volunteerRepository.findAll(pageable);
        page.getContent().stream().forEach(p -> {
            p.resolveTraduction();
        });

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/volunteers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /volunteers/:id : get the "id" volunteer.
     *
     * @param id the id of the volunteer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the volunteer, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/volunteers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Volunteer> getVolunteer(@PathVariable Long id) {
        log.debug("REST request to get Volunteer : {}", id);
        Volunteer volunteer = volunteerRepository.findOne(id);
        if(volunteer != null){
            volunteer.resolveTraduction();
        }
        return Optional.ofNullable(volunteer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /volunteers/:id : delete the "id" volunteer.
     *
     * @param id the id of the volunteer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/volunteers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVolunteer(@PathVariable Long id) {
        log.debug("REST request to delete Volunteer : {}", id);
        volunteerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("volunteer", id.toString())).build();
    }

}
