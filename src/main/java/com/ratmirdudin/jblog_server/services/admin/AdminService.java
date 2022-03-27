package com.ratmirdudin.jblog_server.services.admin;

import com.ratmirdudin.jblog_server.exceptions.BadRequestException;
import com.ratmirdudin.jblog_server.exceptions.ResourceNotFoundException;
import com.ratmirdudin.jblog_server.models.entities.Post;
import com.ratmirdudin.jblog_server.models.entities.User;
import com.ratmirdudin.jblog_server.payloads.requests.PostRequest;
import com.ratmirdudin.jblog_server.repositories.CommentRepository;
import com.ratmirdudin.jblog_server.repositories.PostRepository;
import com.ratmirdudin.jblog_server.repositories.UserRepository;
import com.ratmirdudin.jblog_server.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Post createPost(PostRequest postRequest, UserPrincipal currentUser) {
        log.info("Creating post with title: \"{}\", content: \"{}\" by admin",
                postRequest.getTitle(), postRequest.getContent()
        );
        Post post = new Post();

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());

        User user = userRepository.findUserById(currentUser.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", currentUser.getId())
        );
        post.setUser(user);
        post.setUsername(user.getUsername());

        postRepository.save(post);

        return post;
    }

    public void updatePostById(Long postId, PostRequest postRequest) {
        log.info("Updating post with id: {}, with post request title: \"{}\", content: \"{}\" by admin",
                postId, postRequest.getTitle(), postRequest.getContent());

        if (postRepository.updatePostById(postId, postRequest.getTitle(), postRequest.getContent()) == 0) {
            throw new BadRequestException("The update post operation could not be completed." +
                    " Post not found or user has no rights.");
        }

    }

    @Transactional
    public void deletePostById(Long postId) {
        log.info("Deleting post with id: \"{}\" by admin", postId);
        commentRepository.deleteCommentsByPostId(postId);

        if (postRepository.deletePostById(postId) == 0) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }
    }

    public void banUserById(Long userId) {
        log.info("Banning user with id: \"{}\" by admin", userId);
        if (userRepository.banUserById(userId) == 0) {
            throw new BadRequestException(
                    "The ban operation could not be completed." +
                            " The user does not exist or is immune."
            );
        }
    }

    public void unbanUserById(Long userId) {
        log.info("Unbanning user with id: \"{}\" by admin", userId);
        if (userRepository.unbanUserById(userId) == 0) {
            throw new BadRequestException(
                    "The unban operation could not be completed." +
                            " The user does not exist or is immune."
            );
        }
    }

    public void deleteCommentById(Long commentId) {
        log.info("Deleting comment with id: \"{}\" by admin", commentId);
        if (commentRepository.deleteCommentById(commentId) == 0) {
            throw new ResourceNotFoundException("Comment", "id", commentId);
        }
    }
}
