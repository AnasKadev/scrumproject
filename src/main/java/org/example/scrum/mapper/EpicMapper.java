package org.example.scrum.mapper;

import org.example.scrum.dto.EpicDTO;
import org.example.scrum.entities.Epic;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EpicMapper {

    @Mapping(target = "productBacklogId", source = "productBacklog.id")
    @Mapping(target = "productBacklogName", source = "productBacklog.nom")
    @Mapping(target = "userStoriesCount", expression = "java(epic.getUserStories() != null ? epic.getUserStories().size() : 0)")
    EpicDTO toDTO(Epic epic);

    List<EpicDTO> toDTOList(List<Epic> epics);

    @Mapping(target = "productBacklog", ignore = true)
    @Mapping(target = "userStories", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Epic toEntity(EpicDTO dto);
}

