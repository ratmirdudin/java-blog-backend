package com.ratmirdudin.jblog_server.services;

import com.ratmirdudin.jblog_server.exceptions.BadRequestException;
import com.ratmirdudin.jblog_server.exceptions.ResourceNotFoundException;
import com.ratmirdudin.jblog_server.models.entities.Comment;
import com.ratmirdudin.jblog_server.models.entities.Post;
import com.ratmirdudin.jblog_server.payloads.requests.CommentRequest;
import com.ratmirdudin.jblog_server.repositories.CommentRepository;
import com.ratmirdudin.jblog_server.repositories.PostRepository;
import com.ratmirdudin.jblog_server.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public Comment createComment(Long postId, CommentRequest commentRequest, UserPrincipal currentUser) {
        log.info("Creating comment with message: \"{}\" for post with id: \"{}\" by user with username: \"{}\"", commentRequest.getMessage(), postId, currentUser.getUsername());
        Comment comment = new Comment();
        comment.setMessage(commentRequest.getMessage());
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId)
        );
        comment.setPost(post);
        comment.setUsername(currentUser.getUsername());
        comment.setUserId(currentUser.getId());
        commentRepository.save(comment);
        return comment;
    }

    public void deleteCommentByiD(Long commentId, UserPrincipal currentUser) {
        log.info("Deleting comment with id: \"{}\" by user with username: \"{}\"", commentId, currentUser.getUsername());
        if (commentRepository.deleteCommentByIdAndUserId(commentId, currentUser.getId()) == 0) {
            throw new BadRequestException("The delete comment operation could not be completed." +
                    " Comment not found or user has no rights.");
        }
    }
}
