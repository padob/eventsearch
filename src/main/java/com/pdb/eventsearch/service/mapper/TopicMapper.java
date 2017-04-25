package com.pdb.eventsearch.service.mapper;

import com.pdb.eventsearch.domain.*;
import com.pdb.eventsearch.service.dto.TopicDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Topic and its DTO TopicDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TopicMapper {

    @Mapping(source = "topic.id", target = "topicId")
    TopicDTO topicToTopicDTO(Topic topic);

    List<TopicDTO> topicsToTopicDTOs(List<Topic> topics);

    @Mapping(source = "topicId", target = "topic")
    Topic topicDTOToTopic(TopicDTO topicDTO);

    List<Topic> topicDTOsToTopics(List<TopicDTO> topicDTOs);

    default Event eventFromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
