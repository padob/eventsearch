package com.pdb.eventsearch.web.rest;

import com.pdb.eventsearch.EventsearchApp;

import com.pdb.eventsearch.domain.Topic;
import com.pdb.eventsearch.repository.TopicRepository;
import com.pdb.eventsearch.service.TopicService;
import com.pdb.eventsearch.repository.search.TopicSearchRepository;
import com.pdb.eventsearch.service.dto.TopicDTO;
import com.pdb.eventsearch.service.mapper.TopicMapper;
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
 * Test class for the TopicResource REST controller.
 *
 * @see TopicResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventsearchApp.class)
public class TopicResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private TopicService topicService;

    @Autowired
    private TopicSearchRepository topicSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTopicMockMvc;

    private Topic topic;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TopicResource topicResource = new TopicResource(topicService);
        this.restTopicMockMvc = MockMvcBuilders.standaloneSetup(topicResource)
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
    public static Topic createEntity(EntityManager em) {
        Topic topic = new Topic()
            .name(DEFAULT_NAME);
        return topic;
    }

    @Before
    public void initTest() {
        topicSearchRepository.deleteAll();
        topic = createEntity(em);
    }

    @Test
    @Transactional
    public void createTopic() throws Exception {
        int databaseSizeBeforeCreate = topicRepository.findAll().size();

        // Create the Topic
        TopicDTO topicDTO = topicMapper.topicToTopicDTO(topic);
        restTopicMockMvc.perform(post("/api/topics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(topicDTO)))
            .andExpect(status().isCreated());

        // Validate the Topic in the database
        List<Topic> topicList = topicRepository.findAll();
        assertThat(topicList).hasSize(databaseSizeBeforeCreate + 1);
        Topic testTopic = topicList.get(topicList.size() - 1);
        assertThat(testTopic.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Topic in Elasticsearch
        Topic topicEs = topicSearchRepository.findOne(testTopic.getId());
        assertThat(topicEs).isEqualToComparingFieldByField(testTopic);
    }

    @Test
    @Transactional
    public void createTopicWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = topicRepository.findAll().size();

        // Create the Topic with an existing ID
        topic.setId(1L);
        TopicDTO topicDTO = topicMapper.topicToTopicDTO(topic);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTopicMockMvc.perform(post("/api/topics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(topicDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Topic> topicList = topicRepository.findAll();
        assertThat(topicList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTopics() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);

        // Get all the topicList
        restTopicMockMvc.perform(get("/api/topics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topic.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getTopic() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);

        // Get the topic
        restTopicMockMvc.perform(get("/api/topics/{id}", topic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(topic.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTopic() throws Exception {
        // Get the topic
        restTopicMockMvc.perform(get("/api/topics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTopic() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);
        topicSearchRepository.save(topic);
        int databaseSizeBeforeUpdate = topicRepository.findAll().size();

        // Update the topic
        Topic updatedTopic = topicRepository.findOne(topic.getId());
        updatedTopic
            .name(UPDATED_NAME);
        TopicDTO topicDTO = topicMapper.topicToTopicDTO(updatedTopic);

        restTopicMockMvc.perform(put("/api/topics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(topicDTO)))
            .andExpect(status().isOk());

        // Validate the Topic in the database
        List<Topic> topicList = topicRepository.findAll();
        assertThat(topicList).hasSize(databaseSizeBeforeUpdate);
        Topic testTopic = topicList.get(topicList.size() - 1);
        assertThat(testTopic.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Topic in Elasticsearch
        Topic topicEs = topicSearchRepository.findOne(testTopic.getId());
        assertThat(topicEs).isEqualToComparingFieldByField(testTopic);
    }

    @Test
    @Transactional
    public void updateNonExistingTopic() throws Exception {
        int databaseSizeBeforeUpdate = topicRepository.findAll().size();

        // Create the Topic
        TopicDTO topicDTO = topicMapper.topicToTopicDTO(topic);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTopicMockMvc.perform(put("/api/topics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(topicDTO)))
            .andExpect(status().isCreated());

        // Validate the Topic in the database
        List<Topic> topicList = topicRepository.findAll();
        assertThat(topicList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTopic() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);
        topicSearchRepository.save(topic);
        int databaseSizeBeforeDelete = topicRepository.findAll().size();

        // Get the topic
        restTopicMockMvc.perform(delete("/api/topics/{id}", topic.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean topicExistsInEs = topicSearchRepository.exists(topic.getId());
        assertThat(topicExistsInEs).isFalse();

        // Validate the database is empty
        List<Topic> topicList = topicRepository.findAll();
        assertThat(topicList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTopic() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);
        topicSearchRepository.save(topic);

        // Search the topic
        restTopicMockMvc.perform(get("/api/_search/topics?query=id:" + topic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topic.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Topic.class);
    }
}
