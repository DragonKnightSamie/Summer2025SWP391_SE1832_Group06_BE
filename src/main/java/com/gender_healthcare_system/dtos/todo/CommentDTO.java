package com.gender_healthcare_system.dtos.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gender_healthcare_system.entities.enu.CommentStatus;
import com.gender_healthcare_system.entities.todo.Blog;
import com.gender_healthcare_system.entities.todo.Comment;
import com.gender_healthcare_system.entities.user.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO implements Serializable {

    private Integer commentId;

    @Nationalized
    private String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer parentCommentId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CommentDTO> subComments;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(type = "string", example = "05/06/2025 07:00")
    private LocalDateTime createdAt;

    @Nationalized
    private String editedContent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(type = "string", example = "05/06/2025 07:00")
    private LocalDateTime editedAt;

    private CommentStatus status;

    public CommentDTO(Integer commentId, String content, LocalDateTime createdAt,
                      String editedContent, LocalDateTime editedAt, CommentStatus status) {
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
        this.editedContent = editedContent;
        this.editedAt = editedAt;
        this.status = status;
    }

    public CommentDTO(Integer commentId, String content,
                      Integer parentCommentId, LocalDateTime createdAt,
                      String editedContent, LocalDateTime editedAt, CommentStatus status) {
        this.commentId = commentId;
        this.content = content;
        this.parentCommentId = parentCommentId;
        this.createdAt = createdAt;
        this.editedContent = editedContent;
        this.editedAt = editedAt;
        this.status = status;
    }
}
