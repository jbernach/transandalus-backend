package org.transandalus.backend.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.transandalus.backend.domain.I18n;
import org.transandalus.backend.domain.Stage;
import org.transandalus.backend.domain.Track;
import org.transandalus.backend.repository.StageRepository;
import org.transandalus.backend.repository.TrackRepository;
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
import javax.transaction.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Stage.
 */
@RestController
@RequestMapping("/api")
public class TrackResource {
	
    private final Logger log = LoggerFactory.getLogger(TrackResource.class);
        
    @Inject
    private TrackRepository trackRepository;
    
    /**
     * GET  /tracks/:id -> get the "id" track.
     */
    @RequestMapping(value = "/tracks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.ALL_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<String> getTrack(@PathVariable Long id) {
        log.debug("REST request to get Track : {}", id);
        Track track = trackRepository.findOne(id);
        
        return Optional.ofNullable(track)
            .map(result -> new ResponseEntity<>(
                track.getContent(),
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
