package com.pdb.eventsearch.repository.search;

import com.pdb.eventsearch.domain.Topic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Topic entity.
 */
public interface TopicSearchRepository extends ElasticsearchRepository<Topic, Long> {
}
