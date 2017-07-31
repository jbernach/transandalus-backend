package org.transandalus.backend.web.rest;

import org.transandalus.backend.Application;
import org.transandalus.backend.domain.Volunteer;
import org.transandalus.backend.repository.VolunteerRepository;

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


/**
 * Test class for the VolunteerResource REST controller.
 *
 * @see VolunteerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class VolunteerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";
    private static final String DEFAULT_IMAGE = "AAAAA";
    private static final String UPDATED_IMAGE = "BBBBB";

    @Inject
    private VolunteerRepository volunteerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVolunteerMockMvc;

    private Volunteer volunteer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VolunteerResource volunteerResource = new VolunteerResource();
        ReflectionTestUtils.setField(volunteerResource, "volunteerRepository", volunteerRepository);
        this.restVolunteerMockMvc = MockMvcBuilders.standaloneSetup(volunteerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        volunteer = new Volunteer();
        volunteer.setName(DEFAULT_NAME);
        volunteer.setText(DEFAULT_TEXT);
        volunteer.setImage(DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    public void createVolunteer() throws Exception {
        int databaseSizeBeforeCreate = volunteerRepository.findAll().size();

        // Create the Volunteer

        restVolunteerMockMvc.perform(post("/api/volunteers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(volunteer)))
                .andExpect(status().isCreated());

        // Validate the Volunteer in the database
        List<Volunteer> volunteers = volunteerRepository.findAll();
        assertThat(volunteers).hasSize(databaseSizeBeforeCreate + 1);
        Volunteer testVolunteer = volunteers.get(volunteers.size() - 1);
        assertThat(testVolunteer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVolunteer.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testVolunteer.getImage()).isEqualTo(DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = volunteerRepository.findAll().size();
        // set the field null
        volunteer.setName(null);

        // Create the Volunteer, which fails.

        restVolunteerMockMvc.perform(post("/api/volunteers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(volunteer)))
                .andExpect(status().isBadRequest());

        List<Volunteer> volunteers = volunteerRepository.findAll();
        assertThat(volunteers).hasSize(databaseSizeBeforeTest);
    }

    //@Test
    @Transactional
    public void getAllVolunteers() throws Exception {
        // Initialize the database
        volunteerRepository.saveAndFlush(volunteer);

        // Get all the volunteers
        restVolunteerMockMvc.perform(get("/api/volunteers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(volunteer.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
                .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())));
    }

    //@Test
    @Transactional
    public void getVolunteer() throws Exception {
        // Initialize the database
        volunteerRepository.saveAndFlush(volunteer);

        // Get the volunteer
        restVolunteerMockMvc.perform(get("/api/volunteers/{id}", volunteer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(volunteer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVolunteer() throws Exception {
        // Get the volunteer
        restVolunteerMockMvc.perform(get("/api/volunteers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    //@Test
    @Transactional
    public void updateVolunteer() throws Exception {
        // Initialize the database
        volunteerRepository.saveAndFlush(volunteer);

		int databaseSizeBeforeUpdate = volunteerRepository.findAll().size();

        // Update the volunteer
        Volunteer updatedVolunteer = new Volunteer();
        updatedVolunteer.setId(volunteer.getId());
        updatedVolunteer.setName(UPDATED_NAME);
        updatedVolunteer.setText(UPDATED_TEXT);
        updatedVolunteer.setImage(UPDATED_IMAGE);

        restVolunteerMockMvc.perform(put("/api/volunteers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedVolunteer)))
                .andExpect(status().isOk());

        // Validate the Volunteer in the database
        List<Volunteer> volunteers = volunteerRepository.findAll();
        assertThat(volunteers).hasSize(databaseSizeBeforeUpdate);
        Volunteer testVolunteer = volunteers.get(volunteers.size() - 1);
        assertThat(testVolunteer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVolunteer.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testVolunteer.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    //@Test
    @Transactional
    public void deleteVolunteer() throws Exception {
        // Initialize the database
        volunteerRepository.saveAndFlush(volunteer);

		int databaseSizeBeforeDelete = volunteerRepository.findAll().size();

        // Get the volunteer
        restVolunteerMockMvc.perform(delete("/api/volunteers/{id}", volunteer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Volunteer> volunteers = volunteerRepository.findAll();
        assertThat(volunteers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
