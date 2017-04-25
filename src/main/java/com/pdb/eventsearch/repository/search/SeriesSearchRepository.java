package com.pdb.eventsearch.repository.search;

import com.pdb.eventsearch.domain.Series;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Series entity.
 */
public interface SeriesSearchRepository extends ElasticsearchRepository<Series, Long> {
}
