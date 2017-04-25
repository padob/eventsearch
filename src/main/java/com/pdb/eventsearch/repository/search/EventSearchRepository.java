package com.pdb.eventsearch.repository.search;

import com.pdb.eventsearch.domain.Event;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Event entity.
 */
public interface EventSearchRepository extends ElasticsearchRepository<Event, Long> {
}
