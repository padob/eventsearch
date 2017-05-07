package com.pdb.eventsearch.service.mapper;

import com.pdb.eventsearch.domain.*;
import com.pdb.eventsearch.service.dto.EventDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Event and its DTO EventDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EventMapper {

    @Mapping(source = "eventInCity.id", target = "eventInCityId")
    @Mapping(source = "eventInCity.name", target = "eventInCityName")
    @Mapping(source = "eventInSeries.id", target = "eventInSeriesId")
    @Mapping(source = "eventInSeries.name", target = "eventInSeriesName")
    EventDTO eventToEventDTO(Event event);

    List<EventDTO> eventsToEventDTOs(List<Event> events);

    @Mapping(target = "events", ignore = true)
    @Mapping(source = "eventInCityId", target = "eventInCity")
    @Mapping(source = "eventInSeriesId", target = "eventInSeries")
    Event eventDTOToEvent(EventDTO eventDTO);

    List<Event> eventDTOsToEvents(List<EventDTO> eventDTOs);

    default City cityFromId(Long id) {
        if (id == null) {
            return null;
        }
        City city = new City();
        city.setId(id);
        return city;
    }

    default Series seriesFromId(Long id) {
        if (id == null) {
            return null;
        }
        Series series = new Series();
        series.setId(id);
        return series;
    }
}
