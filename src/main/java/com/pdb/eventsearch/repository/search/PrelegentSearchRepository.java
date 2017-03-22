package com.pdb.eventsearch.repository.search;

import com.pdb.eventsearch.domain.Prelegent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Prelegent entity.
 */
public interface PrelegentSearchRepository extends ElasticsearchRepository<Prelegent, Long> {
}
