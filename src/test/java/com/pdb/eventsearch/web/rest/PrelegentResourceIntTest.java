package com.pdb.eventsearch.web.rest;

import com.pdb.eventsearch.EventsearchApp;

import com.pdb.eventsearch.domain.Prelegent;
import com.pdb.eventsearch.repository.PrelegentRepository;
import com.pdb.eventsearch.repository.search.PrelegentSearchRepository;
import com.pdb.eventsearch.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PrelegentResource REST controller.
 *
 * @see PrelegentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventsearchApp.class)
public class PrelegentResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE_URL = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE_URL = "BBBBBBBBBB";

    @Autowired
    private PrelegentRepository prelegentRepository;

    @Autowired
    private PrelegentSearchRepository prelegentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPrelegentMockMvc;

    private Prelegent prelegent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrelegentResource prelegentResource = new PrelegentResource(prelegentRepository, prelegentSearchRepository);
        this.restPrelegentMockMvc = MockMvcBuilders.standaloneSetup(prelegentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prelegent createEntity(EntityManager em) {
        Prelegent prelegent = new Prelegent()
            .name(DEFAULT_NAME)
            .bio(DEFAULT_BIO)
            .websiteUrl(DEFAULT_WEBSITE_URL);
        return prelegent;
    }

    @Before
    public void initTest() {
        prelegentSearchRepository.deleteAll();
        prelegent = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrelegent() throws Exception {
        int databaseSizeBeforeCreate = prelegentRepository.findAll().size();

        // Create the Prelegent
        restPrelegentMockMvc.perform(post("/api/prelegents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prelegent)))
            .andExpect(status().isCreated());

        // Validate the Prelegent in the database
        List<Prelegent> prelegentList = prelegentRepository.findAll();
        assertThat(prelegentList).hasSize(databaseSizeBeforeCreate + 1);
        Prelegent testPrelegent = prelegentList.get(prelegentList.size() - 1);
        assertThat(testPrelegent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPrelegent.getBio()).isEqualTo(DEFAULT_BIO);
        assertThat(testPrelegent.getWebsiteUrl()).isEqualTo(DEFAULT_WEBSITE_URL);

        // Validate the Prelegent in Elasticsearch
        Prelegent prelegentEs = prelegentSearchRepository.findOne(testPrelegent.getId());
        assertThat(prelegentEs).isEqualToComparingFieldByField(testPrelegent);
    }

    @Test
    @Transactional
    public void createPrelegentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = prelegentRepository.findAll().size();

        // Create the Prelegent with an existing ID
        prelegent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrelegentMockMvc.perform(post("/api/prelegents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prelegent)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Prelegent> prelegentList = prelegentRepository.findAll();
        assertThat(prelegentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = prelegentRepository.findAll().size();
        // set the field null
        prelegent.setName(null);

        // Create the Prelegent, which fails.

        restPrelegentMockMvc.perform(post("/api/prelegents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prelegent)))
            .andExpect(status().isBadRequest());

        List<Prelegent> prelegentList = prelegentRepository.findAll();
        assertThat(prelegentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrelegents() throws Exception {
        // Initialize the database
        prelegentRepository.saveAndFlush(prelegent);

        // Get all the prelegentList
        restPrelegentMockMvc.perform(get("/api/prelegents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prelegent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.toString())))
            .andExpect(jsonPath("$.[*].websiteUrl").value(hasItem(DEFAULT_WEBSITE_URL.toString())));
    }

    @Test
    @Transactional
    public void getPrelegent() throws Exception {
        // Initialize the database
        prelegentRepository.saveAndFlush(prelegent);

        // Get the prelegent
        restPrelegentMockMvc.perform(get("/api/prelegents/{id}", prelegent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(prelegent.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO.toString()))
            .andExpect(jsonPath("$.websiteUrl").value(DEFAULT_WEBSITE_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPrelegent() throws Exception {
        // Get the prelegent
        restPrelegentMockMvc.perform(get("/api/prelegents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrelegent() throws Exception {
        // Initialize the database
        prelegentRepository.saveAndFlush(prelegent);
        prelegentSearchRepository.save(prelegent);
        int databaseSizeBeforeUpdate = prelegentRepository.findAll().size();

        // Update the prelegent
        Prelegent updatedPrelegent = prelegentRepository.findOne(prelegent.getId());
        updatedPrelegent
            .name(UPDATED_NAME)
            .bio(UPDATED_BIO)
            .websiteUrl(UPDATED_WEBSITE_URL);

        restPrelegentMockMvc.perform(put("/api/prelegents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrelegent)))
            .andExpect(status().isOk());

        // Validate the Prelegent in the database
        List<Prelegent> prelegentList = prelegentRepository.findAll();
        assertThat(prelegentList).hasSize(databaseSizeBeforeUpdate);
        Prelegent testPrelegent = prelegentList.get(prelegentList.size() - 1);
        assertThat(testPrelegent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPrelegent.getBio()).isEqualTo(UPDATED_BIO);
        assertThat(testPrelegent.getWebsiteUrl()).isEqualTo(UPDATED_WEBSITE_URL);

        // Validate the Prelegent in Elasticsearch
        Prelegent prelegentEs = prelegentSearchRepository.findOne(testPrelegent.getId());
        assertThat(prelegentEs).isEqualToComparingFieldByField(testPrelegent);
    }

    @Test
    @Transactional
    public void updateNonExistingPrelegent() throws Exception {
        int databaseSizeBeforeUpdate = prelegentRepository.findAll().size();

        // Create the Prelegent

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPrelegentMockMvc.perform(put("/api/prelegents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prelegent)))
            .andExpect(status().isCreated());

        // Validate the Prelegent in the database
        List<Prelegent> prelegentList = prelegentRepository.findAll();
        assertThat(prelegentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePrelegent() throws Exception {
        // Initialize the database
        prelegentRepository.saveAndFlush(prelegent);
        prelegentSearchRepository.save(prelegent);
        int databaseSizeBeforeDelete = prelegentRepository.findAll().size();

        // Get the prelegent
        restPrelegentMockMvc.perform(delete("/api/prelegents/{id}", prelegent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean prelegentExistsInEs = prelegentSearchRepository.exists(prelegent.getId());
        assertThat(prelegentExistsInEs).isFalse();

        // Validate the database is empty
        List<Prelegent> prelegentList = prelegentRepository.findAll();
        assertThat(prelegentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPrelegent() throws Exception {
        // Initialize the database
        prelegentRepository.saveAndFlush(prelegent);
        prelegentSearchRepository.save(prelegent);

        // Search the prelegent
        restPrelegentMockMvc.perform(get("/api/_search/prelegents?query=id:" + prelegent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prelegent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.toString())))
            .andExpect(jsonPath("$.[*].websiteUrl").value(hasItem(DEFAULT_WEBSITE_URL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prelegent.class);
    }
}
