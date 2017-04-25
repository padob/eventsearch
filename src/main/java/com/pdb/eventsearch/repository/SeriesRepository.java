package com.pdb.eventsearch.repository;

import com.pdb.eventsearch.domain.Series;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Series entity.
 */
@SuppressWarnings("unused")
public interface SeriesRepository extends JpaRepository<Series,Long> {

}
