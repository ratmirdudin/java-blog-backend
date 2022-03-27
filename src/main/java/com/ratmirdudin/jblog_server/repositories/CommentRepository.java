package com.ratmirdudin.jblog_server.repositories;

import com.ratmirdudin.jblog_server.models.entities.Comment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
    @Query("select c from Comment c where c.post.id = :postId order by c.createdAt desc")
    List<Comment> findAllCommentsByPostId(@Param("postId") Long postId);

    @Modifying
    @Query("delete from Comment c where c.id = :commentId and c.userId = :userId")
    int deleteCommentByIdAndUserId(@Param("commentId") Long commentId, @Param("userId") Long userId);

    @Modifying
    @Query("delete from Comment c where c.post.id = :postId")
    void deleteCommentsByPostId(@Param("postId") Long postId);

    @Modifying
    @Query("delete from Comment c where c.id = :commentId")
    int deleteCommentById(@Param("commentId") Long commentId);
}
