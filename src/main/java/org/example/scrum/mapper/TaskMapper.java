package org.example.scrum.mapper;

import org.example.scrum.dto.TaskDTO;
import org.example.scrum.entities.Task;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    @Mapping(target = "userStoryId", source = "userStory.id")
    @Mapping(target = "userStoryTitle", source = "userStory.title")
    @Mapping(target = "sprintBacklogId", source = "sprintBacklog.id")
    @Mapping(target = "sprintBacklogName", source = "sprintBacklog.name")
    @Mapping(target = "assignedToId", source = "assignedTo.id")
    @Mapping(target = "assignedToName", expression = "java(task.getAssignedTo() != null ? task.getAssignedTo().getUser().getFirstname() + \" \" + task.getAssignedTo().getUser().getLastname() : null)")
    TaskDTO toDTO(Task task);

    List<TaskDTO> toDTOList(List<Task> tasks);

    @Mapping(target = "userStory", ignore = true)
    @Mapping(target = "sprintBacklog", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Task toEntity(TaskDTO dto);
}

