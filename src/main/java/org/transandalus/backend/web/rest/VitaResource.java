package org.transandalus.backend.web.rest;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.transandalus.backend.service.KmlService;

import com.codahale.metrics.annotation.Timed;

/**
 * REST Controller for Vita interactive stage viewer
 * @author JoseMaria
 *
 */

@RestController
@RequestMapping("/api")
public class VitaResource {
	private final Logger log = LoggerFactory.getLogger(VitaResource.class);

	@Inject
	private KmlService kmlService;

	/**
	 * GET /layer/{layerName} -> Get the combined KML of all Stages containing only the contents of the folder "layerName" for each KML.
	 * @param layerName "track" | "marcadores" | "servicios"
	 * @return
	 */
	@RequestMapping(value = "/layer/{layerName}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	@Timed
	public ResponseEntity<String> getVitaLayer(@PathVariable String layerName,
			@RequestParam(name = "alt", defaultValue = "false") boolean includeAlternatives,
			@RequestParam(name = "link", defaultValue = "false") boolean includeLinks){

		String layerKml = kmlService.getAllStagesKml(layerName, includeAlternatives, includeLinks);

		return new ResponseEntity<String>(layerKml, HttpStatus.OK);
	}
}
