package org.transandalus.backend.service;

import java.io.StringWriter;
import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.transandalus.backend.domain.Stage;
import org.transandalus.backend.domain.Track;
import org.transandalus.backend.repository.StageRepository;

import com.codahale.metrics.annotation.Timed;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Folder;

/**
 * Service class for managing KML information.
 * @author JoseMaria
 *
 */
@Service
public class KmlService {
	private final Logger log = LoggerFactory.getLogger(KmlService.class);
	
	@Inject 
    private StageRepository stageRepository;
    
	/**
	 * Assembles a KML putting together all the stage KMLs of the specified province.
	 * Note that this is a slow operation as BBDD may be accesed and the KML for each stage has to be parsed and marshalled.
	 * @param provinceId Province Id we want to assemble the KML.
	 * @return String with the KML content of all the stages of the specified province merged.
	 */
	@Timed
	public String generateProvinceKML(Long provinceId){
		log.debug("Generating province KML for province Id {}.", provinceId);
		
    	// Recover all the stages of the province
		Page<Stage> stages = stageRepository.findByProvinceId(new PageRequest(0, Integer.MAX_VALUE), provinceId);
    	
		return mergeTracksKml(stages.getContent().stream().map(stage -> stage.getTrack()), null);
	}
	
	/**
	 * Returns the merged KML for all stages.
	 * @param folder Folder name. The merged KML will only collect elements inside that folder name if folderFilter is not null.
	 * @return
	 */
	@Cacheable(cacheNames = "kml")
	@Timed
	public String getAllStagesKml(String folder){
		log.debug("Generating KML for all stages with folder {}.", folder);
		
    	// Recover all the stages
		Page<Stage> stages = stageRepository.findAll(new PageRequest(0, Integer.MAX_VALUE));
    	
		return mergeTracksKml(stages.getContent().stream().map(stage -> stage.getTrack()), folder);
	}
	
	@CacheEvict(cacheNames = "kml", allEntries = true)
	public void resetKmlCache(){
		log.debug("Reseting KML cache.");
	}
	
	/**
	 * Return a String containing the combined KML from specified tracks.
	 * The returned kml doesn't duplicate the folders, instead it combines it.
	 * @param tracks List of Kml tracks to merge
	 * @param folderFilter Folder name. The merged KML will only collect elements inside that folder name if folderFilter is not null.
	 * @return String with the merged KML.
	 */
	public String mergeTracksKml(Stream<Track> tracks, String folderFilter){
		Kml mergedKml = new Kml();
    	Document mergedDocument = mergedKml.createAndSetDocument();
    	
    	tracks.forEach(track ->{
    		if(track != null && track.getContent() != null){
    			String kmlString = track.getContent();
  
    			// Fix Google kml namespace (for old files)
    			kmlString = kmlString.replace("xmlns=\"http://earth.google.com/kml/2.2\"", "xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\"" );
    			
    			// From String to DOM
    			Kml trackKml = Kml.unmarshal(kmlString);
    			
    			if(trackKml != null){
        			Document trackDocument = (Document)trackKml.getFeature();
        			trackDocument.getFeature().stream().forEach(f -> {
        				// Check if we're adding a folder that exists in the composed document (dont want duplicated forlders)
        				if(f instanceof Folder){
        					if(folderFilter == null || folderFilter.equalsIgnoreCase(f.getName())){
        						Optional<Feature> existingFolder = getFolder(mergedDocument, f.getName());
                				
            					if(existingFolder.isPresent()){
            						// Only add the contents of the folder to existing one
            						((Folder) f).getFeature().stream().forEach(inFolderFeat -> {
            							((Folder)existingFolder.get()).addToFeature(inFolderFeat);
            						});
            					}else{
            						mergedDocument.addToFeature(f); // Add the folder and contents to merged kml
            					}
        					}
        				}else{
        					mergedDocument.addToFeature(f); // Add the feature to merged kml
        				}
        			});
        			
        			trackDocument.getStyleSelector().stream().forEach(st -> mergedDocument.addToStyleSelector(st));        			
    			}
    		}
    	});
    	
    	// String serialization
    	StringWriter writer = new StringWriter();
    	mergedKml.marshal(writer);
    	
    	return writer.toString();
	}
	
	/**
	 * Return the Folder feature inside the Kml document.
	 * @param document Kml document
	 * @param name Folder name to search
	 * @return Folder feature
	 */
	private Optional<Feature> getFolder(Document document, String name){
		return document.getFeature().stream().filter(f -> {
			
			if(f instanceof Folder && ObjectUtils.nullSafeToString(f.getName()).equalsIgnoreCase(ObjectUtils.nullSafeToString(name))){
				return true;
			}
			return false;
		}).findAny();
	}
}
