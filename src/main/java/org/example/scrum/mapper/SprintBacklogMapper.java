package org.example.scrum.mapper;

import org.example.scrum.dto.SprintBacklogDTO;
import org.example.scrum.entities.SprintBacklog;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SprintBacklogMapper {

    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "projectName", source = "project.name")
    @Mapping(target = "userStoriesCount", expression = "java(sprintBacklog.getUserStories() != null ? sprintBacklog.getUserStories().size() : 0)")
    @Mapping(target = "tasksCount", expression = "java(sprintBacklog.getTasks() != null ? sprintBacklog.getTasks().size() : 0)")
    SprintBacklogDTO toDTO(SprintBacklog sprintBacklog);

    List<SprintBacklogDTO> toDTOList(List<SprintBacklog> sprintBacklogs);

    @Mapping(target = "project", ignore = true)
    @Mapping(target = "userStories", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SprintBacklog toEntity(SprintBacklogDTO dto);
}

