package com.pdb.eventsearch.service;

import com.pdb.eventsearch.domain.Topic;
import com.pdb.eventsearch.repository.TopicRepository;
import com.pdb.eventsearch.repository.search.TopicSearchRepository;
import com.pdb.eventsearch.service.dto.TopicDTO;
import com.pdb.eventsearch.service.mapper.TopicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Topic.
 */
@Service
@Transactional
public class TopicService {

    private final Logger log = LoggerFactory.getLogger(TopicService.class);
    
    private final TopicRepository topicRepository;

    private final TopicMapper topicMapper;

    private final TopicSearchRepository topicSearchRepository;

    public TopicService(TopicRepository topicRepository, TopicMapper topicMapper, TopicSearchRepository topicSearchRepository) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
        this.topicSearchRepository = topicSearchRepository;
    }

    /**
     * Save a topic.
     *
     * @param topicDTO the entity to save
     * @return the persisted entity
     */
    public TopicDTO save(TopicDTO topicDTO) {
        log.debug("Request to save Topic : {}", topicDTO);
        Topic topic = topicMapper.topicDTOToTopic(topicDTO);
        topic = topicRepository.save(topic);
        TopicDTO result = topicMapper.topicToTopicDTO(topic);
        topicSearchRepository.save(topic);
        return result;
    }

    /**
     *  Get all the topics.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TopicDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Topics");
        Page<Topic> result = topicRepository.findAll(pageable);
        return result.map(topic -> topicMapper.topicToTopicDTO(topic));
    }

    /**
     *  Get one topic by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TopicDTO findOne(Long id) {
        log.debug("Request to get Topic : {}", id);
        Topic topic = topicRepository.findOne(id);
        TopicDTO topicDTO = topicMapper.topicToTopicDTO(topic);
        return topicDTO;
    }

    /**
     *  Delete the  topic by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Topic : {}", id);
        topicRepository.delete(id);
        topicSearchRepository.delete(id);
    }

    /**
     * Search for the topic corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TopicDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Topics for query {}", query);
        Page<Topic> result = topicSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(topic -> topicMapper.topicToTopicDTO(topic));
    }
}
