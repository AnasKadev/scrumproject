package org.example.scrum.mapper;

import org.example.scrum.dto.ProjectDTO;
import org.example.scrum.entities.Project;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {

    @Mapping(target = "productBacklogId", source = "productBacklog.id")
    @Mapping(target = "productBacklogName", source = "productBacklog.nom")
    @Mapping(target = "sprintBacklogsCount", expression = "java(project.getSprintBacklogs() != null ? project.getSprintBacklogs().size() : 0)")
    @Mapping(target = "projectMembersCount", expression = "java(project.getProjectMembers() != null ? project.getProjectMembers().size() : 0)")
    ProjectDTO toDTO(Project project);

    List<ProjectDTO> toDTOList(List<Project> projects);

    @Mapping(target = "productBacklog", ignore = true)
    @Mapping(target = "sprintBacklogs", ignore = true)
    @Mapping(target = "projectMembers", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Project toEntity(ProjectDTO dto);
}

