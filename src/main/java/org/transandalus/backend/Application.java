package org.transandalus.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.transandalus.backend.config.Constants;
import org.transandalus.backend.config.JHipsterProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.transandalus.backend.service.KmlService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

@ComponentScan
@EnableAutoConfiguration(exclude = { MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class })
@EnableConfigurationProperties({ JHipsterProperties.class, LiquibaseProperties.class })
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Inject
    private Environment env;

    @Autowired
    private ApplicationContext appContext;

    /**
     * Initializes backend.
     * <p/>
     * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
     * <p/>
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="http://jhipster.github.io/profiles.html">http://jhipster.github.io/profiles.html</a>.
     * </p>
     */
    @PostConstruct
    public void initApplication() throws IOException {
        if (env.getActiveProfiles().length == 0) {
            log.warn("No Spring profile configured, running with default configuration");
        } else {
            log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
            Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
            if (activeProfiles.contains(Constants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION)) {
                log.error("You have misconfigured your application! " +
                    "It should not run with both the 'dev' and 'prod' profiles at the same time.");
            }
            if (activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION) && activeProfiles.contains(Constants.SPRING_PROFILE_FAST)) {
                log.error("You have misconfigured your application! " +
                    "It should not run with both the 'prod' and 'fast' profiles at the same time.");
            }
            if (activeProfiles.contains(Constants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(Constants.SPRING_PROFILE_CLOUD)) {
                log.error("You have misconfigured your application! " +
                    "It should not run with both the 'dev' and 'cloud' profiles at the same time.");
            }

            //if(activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION)){
                firstCacheHits();
            //}
        }
    }

    /**
     * Main method, used to run the application.
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(Application.class);
        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
        addDefaultProfile(app, source);

        Environment env = app.run(args).getEnvironment();

        log.info("Access URLs:\n----------------------------------------------------------\n\t" +
                "Local: \t\thttp://127.0.0.1:{}\n\t" +
                "External: \thttp://{}:{}\n----------------------------------------------------------",
            env.getProperty("server.port"),
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"));

    }

    /**
     * If no profile has been configured, set by default the "dev" profile.
     */
    private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
        if (!source.containsProperty("spring.profiles.active") &&
                !System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {

            app.setAdditionalProfiles(Constants.SPRING_PROFILE_DEVELOPMENT);
        }
    }


    /**
     * Makes some heavy method calls to initialize caches
     */
    private void firstCacheHits(){
        KmlService kmlService = appContext.getBean(KmlService.class);

        int i = 1, total = 12;
        log.info("Caching KML {}/{}...", i++,  total);
        kmlService.getAllStagesKml("track", false, false);
        log.info("Caching KML {}/{}...", i++,  total);
        kmlService.getAllStagesKml("track", false, true);
        log.info("Caching KML {}/{}...", i++,  total);
        kmlService.getAllStagesKml("track", true, false);
        log.info("Caching KML {}/{}...", i++,  total);
        kmlService.getAllStagesKml("track", true, true);

        log.info("Caching KML {}/{}...", i++,  total);
        kmlService.getAllStagesKml("marcadores", false, false);
        log.info("Caching KML {}/{}...", i++,  total);
        kmlService.getAllStagesKml("marcadores", false, true);
        log.info("Caching KML {}/{}...", i++,  total);
        kmlService.getAllStagesKml("marcadores", true, false);
        log.info("Caching KML {}/{}...", i++,  total);
        kmlService.getAllStagesKml("marcadores", true, true);

        log.info("Caching KML {}/{}...", i++,  total);
        kmlService.getAllStagesKml("servicios", false, false);
        log.info("Caching KML {}/{}...", i++,  total);
        kmlService.getAllStagesKml("servicios", false, true);
        log.info("Caching KML {}/{}...", i++,  total);
        kmlService.getAllStagesKml("servicios", true, false);
        log.info("Caching KML {}/{}...", i++,  total);
        kmlService.getAllStagesKml("servicios", true, true);
        log.info("Caching done!");
    }
}
