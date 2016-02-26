package org.transandalus.backend.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.transandalus.backend.domain.I18n;
import org.transandalus.backend.domain.MenuItem;
import org.transandalus.backend.repository.MenuItemRepository;
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
 * REST controller for managing MenuItem.
 */
@RestController
@RequestMapping("/api")
public class MenuItemResource {

    private final Logger log = LoggerFactory.getLogger(MenuItemResource.class);
        
    @Inject
    private MenuItemRepository menuItemRepository;
    
    /**
     * POST  /menuItems -> Create a new menuItem.
     */
    @RequestMapping(value = "/menuItems",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MenuItem> createMenuItem(@Valid @RequestBody MenuItem menuItem) throws URISyntaxException {
        log.debug("REST request to save MenuItem : {}", menuItem);
        if (menuItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("menuItem", "idexists", "A new menuItem cannot already have an ID")).body(null);
        }
        
        menuItem.setI18nText(I18n.setTranslationText(menuItem.getI18nText(), menuItem.getText()));
        
        MenuItem result = menuItemRepository.save(menuItem);
        return ResponseEntity.created(new URI("/api/menuItems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("menuItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /menuItems -> Updates an existing menuItem.
     */
    @RequestMapping(value = "/menuItems",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MenuItem> updateMenuItem(@Valid @RequestBody MenuItem menuItem) throws URISyntaxException {
        log.debug("REST request to update MenuItem : {}", menuItem);
        if (menuItem.getId() == null) {
            return createMenuItem(menuItem);
        }
        
        MenuItem result = menuItemRepository.findOne(menuItem.getId());
        
        result.setI18nText(I18n.setTranslationText(result.getI18nText(), menuItem.getText()));
        result.setText(menuItem.getText());
        result.setUrl(menuItem.getUrl());
        result.setMenu(menuItem.getMenu());
        result.setOrder(menuItem.getOrder());
        
        result = menuItemRepository.save(result);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("menuItem", menuItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /menuItems -> get all the menuItems.
     */
    @RequestMapping(value = "/menuItems",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MenuItem>> getAllMenuItems(Pageable pageable, @RequestParam(value="menu", required = false) Long menu)
        throws URISyntaxException {
        log.debug("REST request to get a page of MenuItems");
        Page<MenuItem> page = (menu != null)?menuItemRepository.findByMenuId(pageable, menu):menuItemRepository.findAll(pageable);
        page.getContent().stream().forEach(p -> {
        	p.resolveTraduction();
        });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/menuItems");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /menuItems/:id -> get the "id" menuItem.
     */
    @RequestMapping(value = "/menuItems/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MenuItem> getMenuItem(@PathVariable Long id) {
        log.debug("REST request to get MenuItem : {}", id);
        MenuItem menuItem = menuItemRepository.findOne(id);
        if(menuItem != null){
        	menuItem.resolveTraduction();
        }
        return Optional.ofNullable(menuItem)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /menuItems/:id -> delete the "id" menuItem.
     */
    @RequestMapping(value = "/menuItems/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        log.debug("REST request to delete MenuItem : {}", id);
        menuItemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("menuItem", id.toString())).build();
    }
}
