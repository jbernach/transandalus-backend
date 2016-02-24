package org.transandalus.backend.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.transandalus.backend.domain.I18n;
import org.transandalus.backend.domain.Stage;
import org.transandalus.backend.repository.StageRepository;
import org.transandalus.backend.web.rest.util.HeaderUtil;
import org.transandalus.backend.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
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
public class StageResource {

    private final Logger log = LoggerFactory.getLogger(StageResource.class);
        
    @Inject
    private StageRepository stageRepository;
    
    /**
     * POST  /stages -> Create a new stage.
     */
    @RequestMapping(value = "/stages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<Stage> createStage(@RequestBody Stage stage) throws URISyntaxException {
        log.debug("REST request to save Stage : {}", stage);
        if (stage.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stage", "idexists", "A new stage cannot already have an ID")).body(null);
        }
        
        stage.setI18nName(I18n.setTranslationText(stage.getI18nName(), stage.getName()));
        stage.setI18nDescription(I18n.setTranslationText(stage.getI18nDescription(), stage.getDescription()));
        
        Stage result = stageRepository.save(stage);
        return ResponseEntity.created(new URI("/api/stages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("stage", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stages -> Updates an existing stage.
     */
    @RequestMapping(value = "/stages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<Stage> updateStage(@RequestBody Stage stage) throws URISyntaxException {
        log.debug("REST request to update Stage : {}", stage);
        if (stage.getId() == null) {
            return createStage(stage);
        }
        

        Stage result = stageRepository.findOne(stage.getId());
        
        result.setI18nName(I18n.setTranslationText(result.getI18nName(), stage.getName()));
        result.setI18nDescription(I18n.setTranslationText(result.getI18nDescription(), stage.getDescription()));
        result.setName(stage.getName());
        result.setDescription(stage.getDescription());
        result.setTrack(stage.getTrack());
        result.setImageUrl(stage.getImageUrl());
        result.setDistanceTotal(stage.getDistanceTotal());
        result.setDistanceRoad(stage.getDistanceRoad());
        result.setEstimatedTime(stage.getEstimatedTime());
        result.setElevation(stage.getElevation());
        result.setDifficultyPhys(stage.getDifficultyPhys());
        result.setDifficultyTech(stage.getDifficultyTech());
        result.setGalleryURL(stage.getGalleryURL());
        result.setProvince(stage.getProvince());
        result.setNextStage(stage.getNextStage());
        result.setNextAltStage(stage.getNextAltStage());
        result.setPrevStage(stage.getPrevStage());
        result.setPrevAltStage(stage.getPrevAltStage());
        
        result = stageRepository.save(result);
        
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("stage", stage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stages -> get all the stages.
     */
    @RequestMapping(value = "/stages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Stage>> getAllStages(Pageable pageable, @RequestParam(value="filter",defaultValue = "") String filter)
        throws URISyntaxException {
        log.debug("REST request to get a page of Stages");
        
        Page<Stage> page = (filter.length() == 0)?stageRepository.findAll(pageable):stageRepository.findByFilter(pageable, filter, LocaleContextHolder.getLocale().getLanguage());
        page.getContent().stream().forEach(s -> {
        	s.resolveTraduction();
        	s.getProvince().resolveTraduction();
        });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stages/:id -> get the "id" stage.
     */
    @RequestMapping(value = "/stages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<Stage> getStage(@PathVariable Long id) {
        log.debug("REST request to get Stage : {}", id);
        Stage stage = stageRepository.findOne(id);
        if(stage != null){
        	stage.resolveTraduction();
        	stage.getProvince().resolveTraduction();
        	stage.getTrack().getContentType(); // Lazy
        	
        	// Resolve name and desc of next, prev stages and lazy loading
        	for(Stage s : new Stage[]{stage.getNextStage(),stage.getNextAltStage(),stage.getPrevStage(),stage.getPrevAltStage()}){
        		if(s != null) s.resolveTraduction();
        	}
        }
        return Optional.ofNullable(stage)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stages/:id -> delete the "id" stage.
     */
    @RequestMapping(value = "/stages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStage(@PathVariable Long id) {
        log.debug("REST request to delete Stage : {}", id);
        stageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stage", id.toString())).build();
    }
}
