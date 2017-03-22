package com.pdb.eventsearch.repository;

import com.pdb.eventsearch.domain.Prelegent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Prelegent entity.
 */
@SuppressWarnings("unused")
public interface PrelegentRepository extends JpaRepository<Prelegent,Long> {

}
