package org.transandalus.backend.web.rest;

import org.transandalus.backend.Application;
import org.transandalus.backend.domain.Province;
import org.transandalus.backend.repository.ProvinceRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ProvinceResource REST controller.
 *
 * @see ProvinceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ProvinceResourceIntTest {

    private static final String DEFAULT_CODE = "AA";
    private static final String UPDATED_CODE = "BB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Inject
    private ProvinceRepository provinceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProvinceMockMvc;

    private Province province;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProvinceResource provinceResource = new ProvinceResource();
        ReflectionTestUtils.setField(provinceResource, "provinceRepository", provinceRepository);
        this.restProvinceMockMvc = MockMvcBuilders.standaloneSetup(provinceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        province = new Province();
        province.setCode(DEFAULT_CODE);
        province.setName(DEFAULT_NAME);
        province.setDescription(DEFAULT_DESCRIPTION);
        province.setImage(DEFAULT_IMAGE);
        province.setImageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createProvince() throws Exception {
        int databaseSizeBeforeCreate = provinceRepository.findAll().size();

        // Create the Province

        restProvinceMockMvc.perform(post("/api/provinces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(province)))
                .andExpect(status().isCreated());

        // Validate the Province in the database
        List<Province> provinces = provinceRepository.findAll();
        assertThat(provinces).hasSize(databaseSizeBeforeCreate + 1);
        Province testProvince = provinces.get(provinces.size() - 1);
        assertThat(testProvince.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProvince.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProvince.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProvince.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProvince.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = provinceRepository.findAll().size();
        // set the field null
        province.setCode(null);

        // Create the Province, which fails.

        restProvinceMockMvc.perform(post("/api/provinces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(province)))
                .andExpect(status().isBadRequest());

        List<Province> provinces = provinceRepository.findAll();
        assertThat(provinces).hasSize(databaseSizeBeforeTest);
    }

    /*@Test
    @Transactional
    public void getAllProvinces() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinces
        restProvinceMockMvc.perform(get("/api/provinces?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(province.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }*/

    /*@Test
    @Transactional
    public void getProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get the province
        restProvinceMockMvc.perform(get("/api/provinces/{id}", province.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(province.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }*/

    @Test
    @Transactional
    public void getNonExistingProvince() throws Exception {
        // Get the province
        restProvinceMockMvc.perform(get("/api/provinces/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    /*@Test
    @Transactional
    public void updateProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

		int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province
        province.setCode(UPDATED_CODE);
        province.setName(UPDATED_NAME);
        province.setDescription(UPDATED_DESCRIPTION);
        province.setImage(UPDATED_IMAGE);
        province.setImageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restProvinceMockMvc.perform(put("/api/provinces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(province)))
                .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinces = provinceRepository.findAll();
        assertThat(provinces).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinces.get(provinces.size() - 1);
        assertThat(testProvince.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProvince.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProvince.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProvince.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProvince.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }*/

    /*@Test
    @Transactional
    public void deleteProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

		int databaseSizeBeforeDelete = provinceRepository.findAll().size();

        // Get the province
        restProvinceMockMvc.perform(delete("/api/provinces/{id}", province.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Province> provinces = provinceRepository.findAll();
        assertThat(provinces).hasSize(databaseSizeBeforeDelete - 1);
    }*/
}
