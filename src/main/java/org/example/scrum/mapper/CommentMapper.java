package org.example.scrum.mapper;

import org.example.scrum.dto.CommentDTO;
import org.example.scrum.entities.Comment;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", expression = "java(comment.getAuthor() != null ? comment.getAuthor().getFirstname() + \" \" + comment.getAuthor().getLastname() : null)")
    @Mapping(target = "userStoryId", source = "userStory.id")
    @Mapping(target = "userStoryTitle", source = "userStory.title")
    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "taskTitle", source = "task.title")
    CommentDTO toDTO(Comment comment);

    List<CommentDTO> toDTOList(List<Comment> comments);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "userStory", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Comment toEntity(CommentDTO dto);
}

