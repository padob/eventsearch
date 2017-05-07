package com.pdb.eventsearch.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Topic entity.
 */
public class TopicDTO implements Serializable {

    private Long id;

    private String name;

    private Long topicInEventId;

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

    public Long getTopicInEventId() {
        return topicInEventId;
    }

    public void setTopicInEventId(Long eventId) {
        this.topicInEventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TopicDTO topicDTO = (TopicDTO) o;

        if ( ! Objects.equals(id, topicDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TopicDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
