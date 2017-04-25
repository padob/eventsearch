package com.pdb.eventsearch.web.rest;

import com.pdb.eventsearch.EventsearchApp;

import com.pdb.eventsearch.domain.Series;
import com.pdb.eventsearch.repository.SeriesRepository;
import com.pdb.eventsearch.repository.search.SeriesSearchRepository;
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
 * Test class for the SeriesResource REST controller.
 *
 * @see SeriesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventsearchApp.class)
public class SeriesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANIZER = "AAAAAAAAAA";
    private static final String UPDATED_ORGANIZER = "BBBBBBBBBB";

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private SeriesSearchRepository seriesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSeriesMockMvc;

    private Series series;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SeriesResource seriesResource = new SeriesResource(seriesRepository, seriesSearchRepository);
        this.restSeriesMockMvc = MockMvcBuilders.standaloneSetup(seriesResource)
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
    public static Series createEntity(EntityManager em) {
        Series series = new Series()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .organizer(DEFAULT_ORGANIZER);
        return series;
    }

    @Before
    public void initTest() {
        seriesSearchRepository.deleteAll();
        series = createEntity(em);
    }

    @Test
    @Transactional
    public void createSeries() throws Exception {
        int databaseSizeBeforeCreate = seriesRepository.findAll().size();

        // Create the Series
        restSeriesMockMvc.perform(post("/api/series")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(series)))
            .andExpect(status().isCreated());

        // Validate the Series in the database
        List<Series> seriesList = seriesRepository.findAll();
        assertThat(seriesList).hasSize(databaseSizeBeforeCreate + 1);
        Series testSeries = seriesList.get(seriesList.size() - 1);
        assertThat(testSeries.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSeries.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSeries.getOrganizer()).isEqualTo(DEFAULT_ORGANIZER);

        // Validate the Series in Elasticsearch
        Series seriesEs = seriesSearchRepository.findOne(testSeries.getId());
        assertThat(seriesEs).isEqualToComparingFieldByField(testSeries);
    }

    @Test
    @Transactional
    public void createSeriesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = seriesRepository.findAll().size();

        // Create the Series with an existing ID
        series.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeriesMockMvc.perform(post("/api/series")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(series)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Series> seriesList = seriesRepository.findAll();
        assertThat(seriesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSeries() throws Exception {
        // Initialize the database
        seriesRepository.saveAndFlush(series);

        // Get all the seriesList
        restSeriesMockMvc.perform(get("/api/series?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(series.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].organizer").value(hasItem(DEFAULT_ORGANIZER.toString())));
    }

    @Test
    @Transactional
    public void getSeries() throws Exception {
        // Initialize the database
        seriesRepository.saveAndFlush(series);

        // Get the series
        restSeriesMockMvc.perform(get("/api/series/{id}", series.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(series.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.organizer").value(DEFAULT_ORGANIZER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSeries() throws Exception {
        // Get the series
        restSeriesMockMvc.perform(get("/api/series/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSeries() throws Exception {
        // Initialize the database
        seriesRepository.saveAndFlush(series);
        seriesSearchRepository.save(series);
        int databaseSizeBeforeUpdate = seriesRepository.findAll().size();

        // Update the series
        Series updatedSeries = seriesRepository.findOne(series.getId());
        updatedSeries
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .organizer(UPDATED_ORGANIZER);

        restSeriesMockMvc.perform(put("/api/series")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSeries)))
            .andExpect(status().isOk());

        // Validate the Series in the database
        List<Series> seriesList = seriesRepository.findAll();
        assertThat(seriesList).hasSize(databaseSizeBeforeUpdate);
        Series testSeries = seriesList.get(seriesList.size() - 1);
        assertThat(testSeries.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSeries.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSeries.getOrganizer()).isEqualTo(UPDATED_ORGANIZER);

        // Validate the Series in Elasticsearch
        Series seriesEs = seriesSearchRepository.findOne(testSeries.getId());
        assertThat(seriesEs).isEqualToComparingFieldByField(testSeries);
    }

    @Test
    @Transactional
    public void updateNonExistingSeries() throws Exception {
        int databaseSizeBeforeUpdate = seriesRepository.findAll().size();

        // Create the Series

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSeriesMockMvc.perform(put("/api/series")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(series)))
            .andExpect(status().isCreated());

        // Validate the Series in the database
        List<Series> seriesList = seriesRepository.findAll();
        assertThat(seriesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSeries() throws Exception {
        // Initialize the database
        seriesRepository.saveAndFlush(series);
        seriesSearchRepository.save(series);
        int databaseSizeBeforeDelete = seriesRepository.findAll().size();

        // Get the series
        restSeriesMockMvc.perform(delete("/api/series/{id}", series.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean seriesExistsInEs = seriesSearchRepository.exists(series.getId());
        assertThat(seriesExistsInEs).isFalse();

        // Validate the database is empty
        List<Series> seriesList = seriesRepository.findAll();
        assertThat(seriesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSeries() throws Exception {
        // Initialize the database
        seriesRepository.saveAndFlush(series);
        seriesSearchRepository.save(series);

        // Search the series
        restSeriesMockMvc.perform(get("/api/_search/series?query=id:" + series.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(series.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].organizer").value(hasItem(DEFAULT_ORGANIZER.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Series.class);
    }
}
