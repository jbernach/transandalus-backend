package org.transandalus.backend.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.transandalus.backend.domain.Track;
import org.transandalus.backend.repository.TrackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.transaction.Transactional;

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
