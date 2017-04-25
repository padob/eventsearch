package com.pdb.eventsearch.repository;

import com.pdb.eventsearch.domain.Topic;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Topic entity.
 */
@SuppressWarnings("unused")
public interface TopicRepository extends JpaRepository<Topic,Long> {

}
