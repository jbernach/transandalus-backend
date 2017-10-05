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
import org.transandalus.backend.domain.Sponsor;
import org.transandalus.backend.repository.SponsorRepository;
import org.transandalus.backend.web.rest.util.HeaderUtil;
import org.transandalus.backend.web.rest.util.PaginationUtil;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Sponsor.
 */
@RestController
@RequestMapping("/api")
public class SponsorResource {

    private final Logger log = LoggerFactory.getLogger(SponsorResource.class);

    @Inject
    private SponsorRepository sponsorRepository;

    /**
     * POST  /sponsors : Create a new sponsor.
     *
     * @param sponsor the sponsor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sponsor, or with status 400 (Bad Request) if the sponsor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sponsors",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sponsor> createSponsor(@Valid @RequestBody Sponsor sponsor) throws URISyntaxException {
        log.debug("REST request to save Sponsor : {}", sponsor);
        if (sponsor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("sponsor", "idexists", "A new sponsor cannot already have an ID")).body(null);
        }

        sponsor.setI18nText(I18n.setTranslationText(sponsor.getI18nText(), sponsor.getText()));

        Sponsor result = sponsorRepository.save(sponsor);
        return ResponseEntity.created(new URI("/api/sponsors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sponsor", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sponsors : Updates an existing sponsor.
     *
     * @param sponsor the sponsor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sponsor,
     * or with status 400 (Bad Request) if the sponsor is not valid,
     * or with status 500 (Internal Server Error) if the sponsor couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sponsors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sponsor> updateSponsor(@Valid @RequestBody Sponsor sponsor) throws URISyntaxException {
        log.debug("REST request to update Sponsor : {}", sponsor);
        if (sponsor.getId() == null) {
            return createSponsor(sponsor);
        }

        Sponsor result = sponsorRepository.findOne(sponsor.getId());

        result.setName(sponsor.getName());
        result.setLink(sponsor.getLink());
        result.setFromDate(sponsor.getFromDate());
        result.setToDate(sponsor.getToDate());
        result.setI18nText(I18n.setTranslationText(result.getI18nText(), sponsor.getText()));
        result.setImage(sponsor.getImage());

        result = sponsorRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sponsor", sponsor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sponsors : get all the sponsors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sponsors in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/sponsors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Sponsor>> getAllSponsors(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Sponsors");
        Page<Sponsor> page = sponsorRepository.findAll(pageable);
        page.getContent().stream().forEach(p -> {
            p.resolveTraduction();
        });

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sponsors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sponsors/:id : get the "id" sponsor.
     *
     * @param id the id of the sponsor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sponsor, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/sponsors/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sponsor> getSponsor(@PathVariable Long id) {
        log.debug("REST request to get Sponsor : {}", id);
        Sponsor sponsor = sponsorRepository.findOne(id);
        if(sponsor != null){
            sponsor.resolveTraduction();
        }
        return Optional.ofNullable(sponsor)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sponsors/:id : delete the "id" sponsor.
     *
     * @param id the id of the sponsor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/sponsors/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSponsor(@PathVariable Long id) {
        log.debug("REST request to delete Sponsor : {}", id);
        sponsorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sponsor", id.toString())).build();
    }

}
