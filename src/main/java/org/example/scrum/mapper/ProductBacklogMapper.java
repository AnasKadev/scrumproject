package org.example.scrum.mapper;

import org.example.scrum.dto.ProductBacklogDTO;
import org.example.scrum.entities.ProductBacklog;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductBacklogMapper {

    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "projectName", source = "project.name")
    ProductBacklogDTO toDTO(ProductBacklog productBacklog);

    List<ProductBacklogDTO> toDTOList(List<ProductBacklog> productBacklogs);

    @Mapping(target = "project", ignore = true)
    @Mapping(target = "epics", ignore = true)
    @Mapping(target = "userStories", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProductBacklog toEntity(ProductBacklogDTO dto);
}

