package org.transandalus.backend.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.transandalus.backend.domain.Category;
import org.transandalus.backend.domain.I18n;
import org.transandalus.backend.repository.CategoryRepository;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Category.
 */
@RestController
@RequestMapping("/api")
public class CategoryResource {

    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);
        
    @Inject
    private CategoryRepository categoryRepository;
    
    /**
     * POST  /categories -> Create a new category.
     */
    @RequestMapping(value = "/categories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Category> createCategory(@RequestBody Category category) throws URISyntaxException {
        log.debug("REST request to save Category : {}", category);
        if (category.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("category", "idexists", "A new category cannot already have an ID")).body(null);
        }
        
        category.setI18nName(I18n.setTranslationText(category.getI18nName(), category.getName()));
        
        Category result = categoryRepository.save(category);
        return ResponseEntity.created(new URI("/api/categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("category", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /categories -> Updates an existing category.
     */
    @RequestMapping(value = "/categories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) throws URISyntaxException {
        log.debug("REST request to update Category : {}", category);
        if (category.getId() == null) {
            return createCategory(category);
        }
        
        Category result = categoryRepository.findOne(category.getId());
        result.setI18nName(I18n.setTranslationText(result.getI18nName(), category.getName()));
        result.setName(category.getName());
        
        result = categoryRepository.save(result);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("category", category.getId().toString()))
            .body(result);
    }

    /**
     * GET  /categories -> get all the categories.
     */
    @RequestMapping(value = "/categories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Category>> getAllCategories(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of cn");
        Page<Category> page = categoryRepository.findAll(pageable);
        page.getContent().stream().forEach(p -> {
        	p.resolveTraduction();
        });
        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /categories/:id -> get the "id" category.
     */
    @RequestMapping(value = "/categories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        log.debug("REST request to get Category : {}", id);
        Category category = categoryRepository.findOne(id);
        if(category != null){
        	category.resolveTraduction();
        }
        
        return Optional.ofNullable(category)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /categories/:id -> delete the "id" category.
     */
    @RequestMapping(value = "/categories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.debug("REST request to delete Category : {}", id);
        categoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("category", id.toString())).build();
    }
}
