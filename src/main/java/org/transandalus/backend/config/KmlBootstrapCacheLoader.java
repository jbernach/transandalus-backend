package org.transandalus.backend.config;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.bootstrap.BootstrapCacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.transandalus.backend.service.KmlService;

import javax.inject.Inject;

@Component
@Profile(Constants.SPRING_PROFILE_PRODUCTION)
public class KmlBootstrapCacheLoader implements BootstrapCacheLoader {
    private final Logger log = LoggerFactory.getLogger(KmlBootstrapCacheLoader.class);

    @Inject
    private KmlService kmlService;

    @Override
    @Transactional
    public void load(Ehcache ehcache) throws CacheException {
        int i = 1, total = 12;

        log.info("Caching KML {}/{}...", i++,  total);
        ehcache.put(new Element(new SimpleKey("track", false, false), kmlService.getAllStagesKml("track", false, false), true));
        log.info("Caching KML {}/{}...", i++,  total);
        ehcache.put(new Element(new SimpleKey("track", false, true), kmlService.getAllStagesKml("track", false, true), true));
        log.info("Caching KML {}/{}...", i++,  total);
        ehcache.put(new Element(new SimpleKey("track", true, false), kmlService.getAllStagesKml("track", true, true), true));
        log.info("Caching KML {}/{}...", i++,  total);
        ehcache.put(new Element(new SimpleKey("track", true, true), kmlService.getAllStagesKml("track", true, true), true));

        log.info("Caching KML {}/{}...", i++,  total);
        ehcache.put(new Element(new SimpleKey("marcadores", false, false), kmlService.getAllStagesKml("marcadores", false, false), true));
        log.info("Caching KML {}/{}...", i++,  total);
        ehcache.put(new Element(new SimpleKey("marcadores", false, true), kmlService.getAllStagesKml("marcadores", false, true), true));
        log.info("Caching KML {}/{}...", i++,  total);
        ehcache.put(new Element(new SimpleKey("marcadores", true, false), kmlService.getAllStagesKml("marcadores", true, false), true));
        log.info("Caching KML {}/{}...", i++,  total);
        ehcache.put(new Element(new SimpleKey("marcadores", true, true), kmlService.getAllStagesKml("marcadores", true, true), true));

        log.info("Caching KML {}/{}...", i++,  total);
        ehcache.put(new Element(new SimpleKey("servicios", false, false), kmlService.getAllStagesKml("servicios", false, false), true));
        log.info("Caching KML {}/{}...", i++,  total);
        ehcache.put(new Element(new SimpleKey("servicios", false, true), kmlService.getAllStagesKml("servicios", false, true), true));
        log.info("Caching KML {}/{}...", i++,  total);
        ehcache.put(new Element(new SimpleKey("servicios", true, false), kmlService.getAllStagesKml("servicios", true, false), true));
        log.info("Caching KML {}/{}...", i++,  total);
        ehcache.put(new Element(new SimpleKey("servicios", true, true), kmlService.getAllStagesKml("servicios", true, true), true));
        log.info("Caching done!");
    }

    @Override
    public boolean isAsynchronous() {
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }
}
