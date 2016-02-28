package org.transandalus.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.transandalus.backend.domain.Menu;
import org.transandalus.backend.repository.MenuRepository;
import org.transandalus.backend.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Menu.
 */
@RestController
@RequestMapping("/api")
public class MenuResource {

    private final Logger log = LoggerFactory.getLogger(MenuResource.class);
        
    @Inject
    private MenuRepository menuRepository;
    
    /**
     * POST  /menus -> Create a new menu.
     */
    @RequestMapping(value = "/menus",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Menu> createMenu(@Valid @RequestBody Menu menu) throws URISyntaxException {
        log.debug("REST request to save Menu : {}", menu);
        if (menu.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("menu", "idexists", "A new menu cannot already have an ID")).body(null);
        }
        Menu result = menuRepository.save(menu);
        return ResponseEntity.created(new URI("/api/menus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("menu", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /menus -> Updates an existing menu.
     */
    @RequestMapping(value = "/menus",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Menu> updateMenu(@Valid @RequestBody Menu menu) throws URISyntaxException {
        log.debug("REST request to update Menu : {}", menu);
        if (menu.getId() == null) {
            return createMenu(menu);
        }
        Menu result = menuRepository.save(menu);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("menu", menu.getId().toString()))
            .body(result);
    }

    /**
     * GET  /menus -> get all the menus.
     */
    @RequestMapping(value = "/menus",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Menu> getAllMenus() {
        log.debug("REST request to get all Menus");
        return menuRepository.findAll();
            }

    /**
     * GET  /menus/:id -> get the "id" menu.
     */
    @RequestMapping(value = "/menus/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Menu> getMenu(@PathVariable Long id) {
        log.debug("REST request to get Menu : {}", id);
        Menu menu = menuRepository.findOne(id);
        return Optional.ofNullable(menu)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /menus/:id -> delete the "id" menu.
     */
    @RequestMapping(value = "/menus/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        log.debug("REST request to delete Menu : {}", id);
        menuRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("menu", id.toString())).build();
    }
}
