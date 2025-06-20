package com.gender_healthcare_system.dtos.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gender_healthcare_system.entities.enu.BlogStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BlogDTO implements Serializable {

    private int blogId;

    @Nationalized
    private String author;

    @Nationalized
    private String title;

    @Nationalized
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(type = "string", example = "05/06/2025 07:00")
    private LocalDateTime createdAt;

    private BlogStatus status;

    public BlogDTO(int blogId,
                   String author,
                   String title,
                   String content,
                   LocalDateTime createdAt,
                   BlogStatus status) {
        this.blogId = blogId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.status = status;
    }
}
