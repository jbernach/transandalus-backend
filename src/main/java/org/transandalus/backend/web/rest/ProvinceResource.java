package org.transandalus.backend.web.rest;

import com.codahale.metrics.annotation.Timed;

import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.StyleSelector;

import org.transandalus.backend.domain.I18n;
import org.transandalus.backend.domain.Province;
import org.transandalus.backend.domain.Stage;
import org.transandalus.backend.domain.Track;
import org.transandalus.backend.repository.ProvinceRepository;
import org.transandalus.backend.repository.StageRepository;
import org.transandalus.backend.web.rest.util.HeaderUtil;
import org.transandalus.backend.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;

import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Province.
 */
@RestController
@RequestMapping("/api")
public class ProvinceResource {

    private final Logger log = LoggerFactory.getLogger(ProvinceResource.class);
        
    @Inject
    private ProvinceRepository provinceRepository;
    
    @Inject 
    private StageRepository stageRepository;
    
    /**
     * POST  /provinces -> Create a new province.
     */
    @RequestMapping(value = "/provinces",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<Province> createProvince(@Valid @RequestBody Province province) throws URISyntaxException {
        log.debug("REST request to save Province : {}", province);
        if (province.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("province", "idexists", "A new province cannot already have an ID")).body(null);
        }

        province.setI18nName(I18n.setTranslationText(province.getI18nName(), province.getName()));
        province.setI18nDescription(I18n.setTranslationText(province.getI18nDescription(), province.getDescription()));
        
        Province result = provinceRepository.save(province);
        return ResponseEntity.created(new URI("/api/provinces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("province", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /provinces -> Updates an existing province.
     */
    @RequestMapping(value = "/provinces",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<Province> updateProvince(@Valid @RequestBody Province province) throws URISyntaxException {
        log.debug("REST request to update Province : {}", province);
        if (province.getId() == null) {
            return createProvince(province);
        }
        
        Province result = provinceRepository.findOne(province.getId());
        
        result.setCode(province.getCode());
        result.setI18nName(I18n.setTranslationText(result.getI18nName(), province.getName()));
        result.setI18nDescription(I18n.setTranslationText(result.getI18nDescription(), province.getDescription()));
        result.setName(province.getName());
        result.setDescription(province.getDescription());
        result.setTrack(province.getTrack());
        result.setImageUrl(province.getImageUrl());
        
        result = provinceRepository.save(result);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("province", province.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /provinces/generate_kml/:id -> generate the kml for "id" province.
     */
    @RequestMapping(value = "/provinces/generate_kml/{id}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<Province> updateProvinceKML(@PathVariable Long id) {
        log.debug("PUT request to generate the KML for Province : {}", id);
        Province province = provinceRepository.findOne(id);
        if(province != null){
        	province.resolveTraduction();
        	Track track  = province.getTrack();
        	
        	Page<Stage> stages = stageRepository.findByProvinceId(new PageRequest(0, 1000), id);
        	
        	track.setContentType("application/vnd.google-earth.kml+xml");
        	Kml provinceKml = new Kml();
        	Document document = provinceKml.createAndSetDocument();
        	
        	stages.getContent().stream().forEach(s ->{
        		Track ts = s.getTrack();
        		
        		if(ts != null && ts.getContent() != null){
        			String kmlString = ts.getContent();
      
        			// Fix Google kml namespace (for old files)
        			kmlString = kmlString.replace("xmlns=\"http://earth.google.com/kml/2.2\"", "xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\"" );
        			Kml stageKml = Kml.unmarshal(kmlString);
        			
        			if(stageKml != null){
            			Document stageDocument = (de.micromata.opengis.kml.v_2_2_0.Document)stageKml.getFeature();
            			for(Feature feat:stageDocument.getFeature()){
            				document.addToFeature(feat);
            			}
            			
            			for(StyleSelector style:stageDocument.getStyleSelector()){
            				document.addToStyleSelector(style);
            			}
            			
        			}
        		}
        	});
        	
        	StringWriter writer = new StringWriter();
        	provinceKml.marshal(writer);
        	track.setContent(writer.toString());
        	
        	province = provinceRepository.save(province);
        }
        
        return Optional.ofNullable(province)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * GET  /provinces -> get all the provinces.
     */
    @RequestMapping(value = "/provinces",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Province>> getAllProvinces(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Provinces");
        Page<Province> page = provinceRepository.findAll(pageable); 
        page.getContent().stream().forEach(p -> {
        	p.resolveTraduction();
        });
        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/provinces");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /provinces/:id -> get the "id" province.
     */
    @RequestMapping(value = "/provinces/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<Province> getProvince(@PathVariable Long id) {
        log.debug("REST request to get Province : {}", id);
        Province province = provinceRepository.findOne(id);
        if(province != null){
        	province.resolveTraduction();
        	String contentType  = province.getTrack().getContentType(); // Lazy
        }
        return Optional.ofNullable(province)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /provinces/:id -> delete the "id" province.
     */
    @RequestMapping(value = "/provinces/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProvince(@PathVariable Long id) {
        log.debug("REST request to delete Province : {}", id);
        provinceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("province", id.toString())).build();
    }
}
