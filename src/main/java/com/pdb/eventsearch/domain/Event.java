package com.pdb.eventsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.pdb.eventsearch.domain.enumeration.Fee;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "event")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "language")
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(name = "fee")
    private Fee fee;

    @Column(name = "place")
    private String place;

    @Column(name = "address")
    private String address;

    @Column(name = "registeration_link")
    private String registerationLink;

    @Column(name = "event_www")
    private String eventWww;

    @OneToMany(mappedBy = "topic")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Topic> events = new HashSet<>();

    @ManyToOne
    private City event;

    @ManyToOne
    private Series event;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Event name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Event description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Event startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Event endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getLanguage() {
        return language;
    }

    public Event language(String language) {
        this.language = language;
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Fee getFee() {
        return fee;
    }

    public Event fee(Fee fee) {
        this.fee = fee;
        return this;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }

    public String getPlace() {
        return place;
    }

    public Event place(String place) {
        this.place = place;
        return this;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAddress() {
        return address;
    }

    public Event address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegisterationLink() {
        return registerationLink;
    }

    public Event registerationLink(String registerationLink) {
        this.registerationLink = registerationLink;
        return this;
    }

    public void setRegisterationLink(String registerationLink) {
        this.registerationLink = registerationLink;
    }

    public String getEventWww() {
        return eventWww;
    }

    public Event eventWww(String eventWww) {
        this.eventWww = eventWww;
        return this;
    }

    public void setEventWww(String eventWww) {
        this.eventWww = eventWww;
    }

    public Set<Topic> getEvents() {
        return events;
    }

    public Event events(Set<Topic> topics) {
        this.events = topics;
        return this;
    }

    public Event addEvent(Topic topic) {
        this.events.add(topic);
        topic.setTopic(this);
        return this;
    }

    public Event removeEvent(Topic topic) {
        this.events.remove(topic);
        topic.setTopic(null);
        return this;
    }

    public void setEvents(Set<Topic> topics) {
        this.events = topics;
    }

    public City getEvent() {
        return event;
    }

    public Event event(City city) {
        this.event = city;
        return this;
    }

    public void setEvent(City city) {
        this.event = city;
    }

    public Series getEvent() {
        return event;
    }

    public Event event(Series series) {
        this.event = series;
        return this;
    }

    public void setEvent(Series series) {
        this.event = series;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        if (event.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            ", language='" + language + "'" +
            ", fee='" + fee + "'" +
            ", place='" + place + "'" +
            ", address='" + address + "'" +
            ", registerationLink='" + registerationLink + "'" +
            ", eventWww='" + eventWww + "'" +
            '}';
    }
}
