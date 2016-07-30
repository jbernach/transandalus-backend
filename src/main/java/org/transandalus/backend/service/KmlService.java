package org.transandalus.backend.service;

import java.io.StringWriter;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.transandalus.backend.domain.Stage;
import org.transandalus.backend.domain.Track;
import org.transandalus.backend.repository.StageRepository;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;

/**
 * Service class for managing KML information.
 * @author JoseMaria
 *
 */
@Service
public class KmlService {
	@Inject 
    private StageRepository stageRepository;
    
	/**
	 * Assembles a KML putting together all the stage KMLs of the specified province.
	 * Note that this is a slow operation as BBDD may be accesed and the KML for each stage has to be parsed and marshalled.
	 * @param provinceId Province Id we want to assemble the KML.
	 * @return String with the KML content of all the stages of the specified province merged.
	 */
	public String generateProvinceKML(Long provinceId){
		// Master DOM
		Kml provinceKml = new Kml();
    	Document document = provinceKml.createAndSetDocument();
    	
    	// Recover all the stages of the province
		Page<Stage> stages = stageRepository.findByProvinceId(new PageRequest(0, Integer.MAX_VALUE), provinceId);
    	
    	stages.getContent().stream().forEach(stage ->{
    		Track ts = stage.getTrack();
    		
    		if(ts != null && ts.getContent() != null){
    			String kmlString = ts.getContent();
  
    			// Fix Google kml namespace (for old files)
    			kmlString = kmlString.replace("xmlns=\"http://earth.google.com/kml/2.2\"", "xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\"" );
    			
    			// From String to DOM
    			Kml stageKml = Kml.unmarshal(kmlString);
    			
    			if(stageKml != null){
        			Document stageDocument = (de.micromata.opengis.kml.v_2_2_0.Document)stageKml.getFeature();
        			stageDocument.getFeature().stream().forEach(f -> document.addToFeature(f));
        			stageDocument.getStyleSelector().stream().forEach(st -> document.addToStyleSelector(st));        			
    			}
    		}
    	});
    	
    	StringWriter writer = new StringWriter();
    	provinceKml.marshal(writer);
    	
    	return writer.toString();
	}
}
