package com.pdb.eventsearch.service;

import com.pdb.eventsearch.domain.Event;
import com.pdb.eventsearch.repository.EventRepository;
import com.pdb.eventsearch.repository.search.EventSearchRepository;
import com.pdb.eventsearch.service.dto.EventDTO;
import com.pdb.eventsearch.service.mapper.EventMapper;
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
 * Service Implementation for managing Event.
 */
@Service
@Transactional
public class EventService {

    private final Logger log = LoggerFactory.getLogger(EventService.class);
    
    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final EventSearchRepository eventSearchRepository;

    public EventService(EventRepository eventRepository, EventMapper eventMapper, EventSearchRepository eventSearchRepository) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.eventSearchRepository = eventSearchRepository;
    }

    /**
     * Save a event.
     *
     * @param eventDTO the entity to save
     * @return the persisted entity
     */
    public EventDTO save(EventDTO eventDTO) {
        log.debug("Request to save Event : {}", eventDTO);
        Event event = eventMapper.eventDTOToEvent(eventDTO);
        event = eventRepository.save(event);
        EventDTO result = eventMapper.eventToEventDTO(event);
        eventSearchRepository.save(event);
        return result;
    }

    /**
     *  Get all the events.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Events");
        Page<Event> result = eventRepository.findAll(pageable);
        return result.map(event -> eventMapper.eventToEventDTO(event));
    }

    /**
     *  Get one event by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EventDTO findOne(Long id) {
        log.debug("Request to get Event : {}", id);
        Event event = eventRepository.findOne(id);
        EventDTO eventDTO = eventMapper.eventToEventDTO(event);
        return eventDTO;
    }

    /**
     *  Delete the  event by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Event : {}", id);
        eventRepository.delete(id);
        eventSearchRepository.delete(id);
    }

    /**
     * Search for the event corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EventDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Events for query {}", query);
        Page<Event> result = eventSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(event -> eventMapper.eventToEventDTO(event));
    }
}
