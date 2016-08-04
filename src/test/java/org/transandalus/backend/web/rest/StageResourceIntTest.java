package org.transandalus.backend.web.rest;

import org.transandalus.backend.Application;
import org.transandalus.backend.domain.Stage;
import org.transandalus.backend.repository.StageRepository;
import org.transandalus.backend.service.KmlService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.hasItem;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.transandalus.backend.domain.enumeration.Difficulty;
import org.transandalus.backend.domain.enumeration.Difficulty;

/**
 * Test class for the StageResource REST controller.
 *
 * @see StageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StageResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Float DEFAULT_DISTANCE_TOTAL = 1F;
    private static final Float UPDATED_DISTANCE_TOTAL = 2F;

    private static final Float DEFAULT_DISTANCE_ROAD = 1F;
    private static final Float UPDATED_DISTANCE_ROAD = 2F;

    private static final Integer DEFAULT_ESTIMATED_TIME = 1;
    private static final Integer UPDATED_ESTIMATED_TIME = 2;

    private static final Integer DEFAULT_ELEVATION = 1;
    private static final Integer UPDATED_ELEVATION = 2;
    
    private static final Difficulty DEFAULT_DIFFICULTY_PHYS = Difficulty.VERY_LOW;
    private static final Difficulty UPDATED_DIFFICULTY_PHYS = Difficulty.LOW;
    
    private static final Difficulty DEFAULT_DIFFICULTY_TECH = Difficulty.VERY_LOW;
    private static final Difficulty UPDATED_DIFFICULTY_TECH = Difficulty.LOW;
    private static final String DEFAULT_GALLERY_URL = "AAAAA";
    private static final String UPDATED_GALLERY_URL = "BBBBB";

    @Inject
    private StageRepository stageRepository;

    @Inject
    private KmlService kmlService;
    
    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStageMockMvc;

    private Stage stage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StageResource stageResource = new StageResource();
        ReflectionTestUtils.setField(stageResource, "stageRepository", stageRepository);
        ReflectionTestUtils.setField(stageResource, "kmlService", kmlService);
        this.restStageMockMvc = MockMvcBuilders.standaloneSetup(stageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stage = new Stage();
        stage.setName(DEFAULT_NAME);
        stage.setDescription(DEFAULT_DESCRIPTION);
        stage.setDistanceTotal(DEFAULT_DISTANCE_TOTAL);
        stage.setDistanceRoad(DEFAULT_DISTANCE_ROAD);
        stage.setEstimatedTime(DEFAULT_ESTIMATED_TIME);
        stage.setElevation(DEFAULT_ELEVATION);
        stage.setDifficultyPhys(DEFAULT_DIFFICULTY_PHYS);
        stage.setDifficultyTech(DEFAULT_DIFFICULTY_TECH);
        stage.setGalleryURL(DEFAULT_GALLERY_URL);
    }

    @Test
    @Transactional
    public void createStage() throws Exception {
        int databaseSizeBeforeCreate = stageRepository.findAll().size();

        // Create the Stage

        restStageMockMvc.perform(post("/api/stages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stage)))
                .andExpect(status().isCreated());

        // Validate the Stage in the database
        List<Stage> stages = stageRepository.findAll();
        assertThat(stages).hasSize(databaseSizeBeforeCreate + 1);
        Stage testStage = stages.get(stages.size() - 1);
        assertThat(testStage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStage.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStage.getDistanceTotal()).isEqualTo(DEFAULT_DISTANCE_TOTAL);
        assertThat(testStage.getDistanceRoad()).isEqualTo(DEFAULT_DISTANCE_ROAD);
        assertThat(testStage.getEstimatedTime()).isEqualTo(DEFAULT_ESTIMATED_TIME);
        assertThat(testStage.getElevation()).isEqualTo(DEFAULT_ELEVATION);
        assertThat(testStage.getDifficultyPhys()).isEqualTo(DEFAULT_DIFFICULTY_PHYS);
        assertThat(testStage.getDifficultyTech()).isEqualTo(DEFAULT_DIFFICULTY_TECH);
        assertThat(testStage.getGalleryURL()).isEqualTo(DEFAULT_GALLERY_URL);
    }

   /* @Test
    @Transactional
    public void getAllStages() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stages
        restStageMockMvc.perform(get("/api/stages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stage.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].distanceTotal").value(hasItem(DEFAULT_DISTANCE_TOTAL.doubleValue())))
                .andExpect(jsonPath("$.[*].distanceRoad").value(hasItem(DEFAULT_DISTANCE_ROAD.doubleValue())))
                .andExpect(jsonPath("$.[*].estimatedTime").value(hasItem(DEFAULT_ESTIMATED_TIME)))
                .andExpect(jsonPath("$.[*].elevation").value(hasItem(DEFAULT_ELEVATION)))
                .andExpect(jsonPath("$.[*].difficultyPhys").value(hasItem(DEFAULT_DIFFICULTY_PHYS.toString())))
                .andExpect(jsonPath("$.[*].difficultyTech").value(hasItem(DEFAULT_DIFFICULTY_TECH.toString())))
                .andExpect(jsonPath("$.[*].galleryURL").value(hasItem(DEFAULT_GALLERY_URL.toString())));
    }*/

    /*@Test
    @Transactional
    public void getStage() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get the stage
        restStageMockMvc.perform(get("/api/stages/{id}", stage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.distanceTotal").value(DEFAULT_DISTANCE_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.distanceRoad").value(DEFAULT_DISTANCE_ROAD.doubleValue()))
            .andExpect(jsonPath("$.estimatedTime").value(DEFAULT_ESTIMATED_TIME))
            .andExpect(jsonPath("$.elevation").value(DEFAULT_ELEVATION))
            .andExpect(jsonPath("$.difficultyPhys").value(DEFAULT_DIFFICULTY_PHYS.toString()))
            .andExpect(jsonPath("$.difficultyTech").value(DEFAULT_DIFFICULTY_TECH.toString()))
            .andExpect(jsonPath("$.galleryURL").value(DEFAULT_GALLERY_URL.toString()));
    }
*/
    @Test
    @Transactional
    public void getNonExistingStage() throws Exception {
        // Get the stage
        restStageMockMvc.perform(get("/api/stages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

   /* @Test
    @Transactional
    public void updateStage() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

		int databaseSizeBeforeUpdate = stageRepository.findAll().size();

        // Update the stage
        stage.setName(UPDATED_NAME);
        stage.setDescription(UPDATED_DESCRIPTION);
        stage.setDistanceTotal(UPDATED_DISTANCE_TOTAL);
        stage.setDistanceRoad(UPDATED_DISTANCE_ROAD);
        stage.setEstimatedTime(UPDATED_ESTIMATED_TIME);
        stage.setElevation(UPDATED_ELEVATION);
        stage.setDifficultyPhys(UPDATED_DIFFICULTY_PHYS);
        stage.setDifficultyTech(UPDATED_DIFFICULTY_TECH);
        stage.setGalleryURL(UPDATED_GALLERY_URL);

        restStageMockMvc.perform(put("/api/stages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stage)))
                .andExpect(status().isOk());

        // Validate the Stage in the database
        List<Stage> stages = stageRepository.findAll();
        assertThat(stages).hasSize(databaseSizeBeforeUpdate);
        Stage testStage = stages.get(stages.size() - 1);
        assertThat(testStage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStage.getDistanceTotal()).isEqualTo(UPDATED_DISTANCE_TOTAL);
        assertThat(testStage.getDistanceRoad()).isEqualTo(UPDATED_DISTANCE_ROAD);
        assertThat(testStage.getEstimatedTime()).isEqualTo(UPDATED_ESTIMATED_TIME);
        assertThat(testStage.getElevation()).isEqualTo(UPDATED_ELEVATION);
        assertThat(testStage.getDifficultyPhys()).isEqualTo(UPDATED_DIFFICULTY_PHYS);
        assertThat(testStage.getDifficultyTech()).isEqualTo(UPDATED_DIFFICULTY_TECH);
        assertThat(testStage.getGalleryURL()).isEqualTo(UPDATED_GALLERY_URL);
    }

    @Test
    @Transactional
    public void deleteStage() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

		int databaseSizeBeforeDelete = stageRepository.findAll().size();

        // Get the stage
        restStageMockMvc.perform(delete("/api/stages/{id}", stage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Stage> stages = stageRepository.findAll();
        assertThat(stages).hasSize(databaseSizeBeforeDelete - 1);
    }*/
}
