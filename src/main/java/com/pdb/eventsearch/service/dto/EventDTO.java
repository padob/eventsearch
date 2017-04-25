package com.pdb.eventsearch.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.pdb.eventsearch.domain.enumeration.Fee;

/**
 * A DTO for the Event entity.
 */
public class EventDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private String language;

    private Fee fee;

    private String place;

    private String address;

    private String registerationLink;

    private String eventWww;

    private Long eventId;

    private Long eventId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getRegisterationLink() {
        return registerationLink;
    }

    public void setRegisterationLink(String registerationLink) {
        this.registerationLink = registerationLink;
    }
    public String getEventWww() {
        return eventWww;
    }

    public void setEventWww(String eventWww) {
        this.eventWww = eventWww;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long cityId) {
        this.eventId = cityId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long seriesId) {
        this.eventId = seriesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EventDTO eventDTO = (EventDTO) o;

        if ( ! Objects.equals(id, eventDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EventDTO{" +
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
