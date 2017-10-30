package org.transandalus.backend.service;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.transandalus.backend.domain.Stage;
import org.transandalus.backend.domain.enumeration.StageType;
import org.transandalus.backend.repository.StageRepository;
import org.transandalus.backend.service.util.KmlUtil;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.stream.Stream;

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

    @Inject
    private KmlUtil kmlUtil;

    /**
     * Assembles a KML putting together all the stage KMLs of the specified province.
     * Note that this is a slow operation as BBDD may be accesed and the KML for each stage has to be parsed and marshalled.
     *
     * @param provinceId Province Id we want to assemble the KML.
     * @return String with the KML content of all the stages of the specified province merged.
     */
    @Timed
    @Transactional
    public String generateProvinceKML(Long provinceId) {
        log.debug("Generating province KML for province Id {}.", provinceId);

        // Recover all the stages of the province
        Page<Stage> stages = stageRepository.findByProvinceId(new PageRequest(0, Integer.MAX_VALUE), provinceId);

        return kmlUtil.mergeTracksKml(stages.getContent().stream().map(stage -> stage.getTrack()), null);
    }

    /**
     * Returns the merged KML for all stages.
     * This method is cached.
     *
     * @param folder Folder name. The merged KML will only collect elements inside that folder name if folderFilter is not null. ("track" | "marcadores" | "servicios")
     * @return
     */
    @Cacheable(cacheNames = "kml")
    @Timed
    @Transactional
    public String getAllStagesKml(String folder, boolean includeAlternatives, boolean includeLinks) {
        log.info("Generating KML for all stages with folder {}.", folder);

        // Recover all the stages
        Stream<Stage> stages = stageRepository.findAll(new PageRequest(0, Integer.MAX_VALUE)).getContent().stream();

        return kmlUtil.mergeTracksKml(stages.filter(stage -> stage.getStageType() == StageType.REGULAR ||
            (includeAlternatives && stage.getStageType() == StageType.ALTERNATIVE) ||
            (includeLinks && stage.getStageType() == StageType.LINK)).map(stage -> stage.getTrack()), folder);
    }

    /**
     * Reset the global KML cache used in getAllStagesKml
     */
    @CacheEvict(cacheNames = "kml", allEntries = true)
    public void resetKmlCache() {
        log.debug("Reseting KML cache.");
    }
}

