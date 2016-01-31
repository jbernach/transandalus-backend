package org.transandalus.backend.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.transandalus.backend.domain.I18n;
import org.transandalus.backend.domain.Province;
import org.transandalus.backend.repository.ProvinceRepository;
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
 * REST controller for managing Province.
 */
@RestController
@RequestMapping("/api")
public class ProvinceResource {

    private final Logger log = LoggerFactory.getLogger(ProvinceResource.class);
        
    @Inject
    private ProvinceRepository provinceRepository;
    
    /**
     * POST  /provinces -> Create a new province.
     */
    @RequestMapping(value = "/provinces",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
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
    public ResponseEntity<Province> updateProvince(@Valid @RequestBody Province province) throws URISyntaxException {
        log.debug("REST request to update Province : {}", province);
        if (province.getId() == null) {
            return createProvince(province);
        }
        
        Province result = provinceRepository.findOne(province.getId());
        
        result.setCode(province.getCode());
        result.setI18nName(I18n.setTranslationText(result.getI18nName(), province.getName()));
        result.setI18nDescription(I18n.setTranslationText(result.getI18nDescription(), province.getDescription()));
        result.setImage(province.getImage());
        result.setImageContentType(province.getImageContentType());
        result.setName(province.getName());
        result.setDescription(province.getDescription());
        
        result = provinceRepository.save(result);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("province", province.getId().toString()))
            .body(result);
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
        	p.setName(I18n.getTranslationText(p.getI18nName()));
        	p.setDescription(I18n.getTranslationText(p.getI18nDescription()));
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
    public ResponseEntity<Province> getProvince(@PathVariable Long id) {
        log.debug("REST request to get Province : {}", id);
        Province province = provinceRepository.findOne(id);
        if(province != null){
        	province.setName(I18n.getTranslationText(province.getI18nName()));
        	province.setDescription(I18n.getTranslationText(province.getI18nDescription()));
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
