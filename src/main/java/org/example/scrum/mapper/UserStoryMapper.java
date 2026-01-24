package org.example.scrum.mapper;

import org.example.scrum.dto.UserStoryDTO;
import org.example.scrum.entities.UserStory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserStoryMapper {

    @Mapping(target = "epicId", source = "epic.id")
    @Mapping(target = "epicTitle", source = "epic.title")
    @Mapping(target = "productBacklogId", source = "productBacklog.id")
    @Mapping(target = "productBacklogName", source = "productBacklog.nom")
    @Mapping(target = "sprintBacklogId", source = "sprintBacklog.id")
    @Mapping(target = "sprintBacklogName", source = "sprintBacklog.name")
    @Mapping(target = "tasksCount", expression = "java(userStory.getTasks() != null ? userStory.getTasks().size() : 0)")
    @Mapping(target = "allTasksCompleted", expression = "java(userStory.areAllTasksCompleted())")
    @Mapping(target = "canBeCompleted", expression = "java(userStory.canBeCompleted())")
    UserStoryDTO toDTO(UserStory userStory);

    List<UserStoryDTO> toDTOList(List<UserStory> userStories);

    @Mapping(target = "epic", ignore = true)
    @Mapping(target = "productBacklog", ignore = true)
    @Mapping(target = "sprintBacklog", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserStory toEntity(UserStoryDTO dto);
}

