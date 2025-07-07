package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.todo.CommentDTO;
import com.gender_healthcare_system.entities.enu.CommentStatus;
import com.gender_healthcare_system.entities.todo.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

    @Query("SELECT new com.gender_healthcare_system.entities.todo.Comment" +
            "(c.commentId, c.content, c.parentComment, " +
            "c.createdAt, c.edited_Content, c.editedAt, c.status) " +
            "FROM Comment c " +
            "WHERE c.commentId = :commentId " +
            "AND c.parentComment = NULL")
    Optional<Comment> getParentCommentDumpById(int commentId);

    /*@Query("SELECT new com.gender_healthcare_system.entities.todo.Comment" +
            "(c.commentId, c.content, c.parentComment, " +
            "c.createdAt, c.edited_Content, c.editedAt, c.status) " +
            "FROM Comment c " +
            "WHERE c.commentId = :commentId " +
            "AND c.account.accountId = :accountId")
    Optional<Comment> getCommentDumpById(int commentId, int accountId);*/

    @Query("SELECT new com.gender_healthcare_system.dtos.todo.CommentDTO" +
            "(c.commentId, c.content, c.createdAt, c.edited_Content, c.editedAt, c.status) " +
            "FROM Comment c " +
            "WHERE c.blog.blogId = :blogId " +
            "AND c.parentComment = NULL")
    Page<CommentDTO> getAllTopCommentsOfABlog(int blogId, Pageable pageable);

    @Query("SELECT new com.gender_healthcare_system.dtos.todo.CommentDTO" +
            "(c.commentId, c.content, c.parentComment.commentId," +
            " c.createdAt, c.edited_Content, c.editedAt, c.status) " +
            "FROM Comment c " +
            "WHERE c.parentComment.commentId = :commentId")
    Page<CommentDTO> getAllSubCommentsOfAComment(int commentId, Pageable pageable);

    boolean existsByCommentIdAndAccountAccountId(int commentId, int accountAccountId);

    @Modifying
    @Query("UPDATE Comment c " +
            "SET c.edited_Content = :newContent," +
            "c.editedAt = :editedCreatedAt " +
            "WHERE c.commentId = :commentId")
    void updateACommentOrSubComment(int commentId, String newContent,
                                    LocalDateTime editedCreatedAt);

    @Modifying
    @Query("UPDATE Comment c " +
            "SET c.status = :status " +
            "WHERE c.commentId = :commentId")
    void removeACommentOrSubComment(int commentId, CommentStatus status);
}
