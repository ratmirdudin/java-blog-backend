package com.ratmirdudin.jblog_server.repositories;

import com.ratmirdudin.jblog_server.models.entities.Category;
import com.ratmirdudin.jblog_server.models.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    @Query("select p from Post p" +
            " where concat(lower(p.title), ' ', lower(p.content)) like concat('%', lower(:search), '%')")
    Page<Post> findAllPosts(@Param("search") String search, Pageable pageable);

    @Query("select p from Post p where :category member of p.categories")
    Page<Post> findAllPostsByCategory(@Param("category") Category category, Pageable pageable);

    @Modifying
    @Query("update Post p set p.title = :postTitle, p.content = :postContent where p.id = :postId")
    int updatePostById(Long postId, String postTitle, String postContent);

    @Modifying
    @Query("delete from Post p where p.id = :postId")
    int deletePostById(@Param("postId") Long postId);

}
