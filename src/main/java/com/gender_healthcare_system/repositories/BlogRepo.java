package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.todo.BlogDTO;
import com.gender_healthcare_system.entities.todo.Blog;
import com.gender_healthcare_system.payloads.todo.BlogUpdatePayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogRepo extends JpaRepository<Blog, Integer> {

    @Query("SELECT new com.gender_healthcare_system.dtos.todo.BlogDTO" +
            "(b.blogId, b.author, b.title, b.content, b.createdAt, b.status) " +
            "FROM Blog b " +
            "WHERE b.blogId = :id")
    Optional<BlogDTO> getBlogDetailsById(int id);

    @Query("SELECT new com.gender_healthcare_system.dtos.todo.BlogDTO" +
            "(b.blogId, b.author, b.title, b.content, b.createdAt, b.status) " +
            "FROM Blog b " +
            "WHERE b.blogId = :id " +
            "AND b.status = com.gender_healthcare_system.entities.enu.BlogStatus.ACTIVE")
    Optional<BlogDTO> getBlogDetailsActiveById(int id);

    @Query("SELECT new com.gender_healthcare_system.dtos.todo.BlogDTO" +
            "(b.blogId, b.author, b.title, b.content, b.createdAt, b.status) " +
            "FROM Blog b")
    Page<BlogDTO> getAllBlogs(Pageable pageable);

/*    @Query("SELECT new com.gender_healthcare_system.dtos." +
            "(b.blogId, b.title, b.content, b.createdAt, b.status) " +
            "FROM Blog b " +
            "WHERE b.title LIKE :keyword")*/
    Page<BlogDTO> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    @Modifying
    @Query("UPDATE Blog b " +
            "SET b.title = :#{#payload.title}, " +
            "b.content = :#{#payload.content}, " +
            "b.status = :#{#payload.status} " +
            "WHERE b.blogId = :id")
    void updateBlogById(int id, @Param("payload") BlogUpdatePayload payload);

    @Modifying
    @Query("DELETE FROM Blog b " +
            "WHERE b.blogId = :id")
    void deleteBlogById(int id);
}

