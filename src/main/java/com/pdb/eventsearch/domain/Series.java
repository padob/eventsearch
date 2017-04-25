package com.pdb.eventsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Series.
 */
@Entity
@Table(name = "series")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "series")
public class Series implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "organizer")
    private String organizer;

    @OneToMany(mappedBy = "event")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> series = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Series name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Series description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganizer() {
        return organizer;
    }

    public Series organizer(String organizer) {
        this.organizer = organizer;
        return this;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public Set<Event> getSeries() {
        return series;
    }

    public Series series(Set<Event> events) {
        this.series = events;
        return this;
    }

    public Series addSeries(Event event) {
        this.series.add(event);
        event.setEvent(this);
        return this;
    }

    public Series removeSeries(Event event) {
        this.series.remove(event);
        event.setEvent(null);
        return this;
    }

    public void setSeries(Set<Event> events) {
        this.series = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Series series = (Series) o;
        if (series.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, series.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Series{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", organizer='" + organizer + "'" +
            '}';
    }
}
