package org.transandalus.backend.config;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.bootstrap.BootstrapCacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("!" + Constants.SPRING_PROFILE_PRODUCTION)
public class EmptyBootstrapCacheLoader implements BootstrapCacheLoader {
    private final Logger log = LoggerFactory.getLogger(EmptyBootstrapCacheLoader.class);

    @Override
    @Transactional
    public void load(Ehcache ehcache) throws CacheException {
        log.info("No cache bootstrap for this profile.");
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
