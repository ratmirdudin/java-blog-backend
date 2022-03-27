package com.ratmirdudin.jblog_server.services;

import com.ratmirdudin.jblog_server.cachings.CategoryCachingService;
import com.ratmirdudin.jblog_server.exceptions.ResourceNotFoundException;
import com.ratmirdudin.jblog_server.models.entities.Category;
import com.ratmirdudin.jblog_server.models.entities.Comment;
import com.ratmirdudin.jblog_server.models.entities.Post;
import com.ratmirdudin.jblog_server.models.enums.CategoryEnum;
import com.ratmirdudin.jblog_server.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryCachingService categoryCachingService;

    public Page<Post> getAllPosts(String search, String strCategory, int page, int limit) {
        log.info("Getting all posts with params search: \"{}\", category: \"{}\", page: \"{}\", limit: \"{}\"",
                search, strCategory, page, limit
        );
        if (page < 1) {
            page = 1;
        }
        if (limit < 1) {
            limit = 50;
        }

        Sort sort = Sort.by("createdAt").descending();
        Pageable pageableRequest = PageRequest.of(page - 1, limit, sort);

        Category category = null;

        String finalStrCategory = strCategory.toUpperCase();
        if (!finalStrCategory.isBlank() && isCategory(finalStrCategory)) {
            category = categoryCachingService.getCategory(CategoryEnum.valueOf(finalStrCategory)).orElseThrow(
                    () -> new ResourceNotFoundException("Category", "category name", finalStrCategory)
            );
        }

        if (category != null) {
            return postRepository.findAllPostsByCategory(category, pageableRequest);
        }
        return postRepository.findAllPosts(search, pageableRequest);
    }

    private boolean isCategory(String strCategory) {
        return Arrays.stream(CategoryEnum.values())
                .anyMatch(elem -> elem.toString().equals(strCategory));
    }

    public Post getDetailPostById(Long postId) {
        log.info("Getting detail post with id: \"{}\"", postId);

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId)
        );

        // Первыми - самые новые комментарии
        List<Comment> comments = post.getComments();
        comments.sort((o1, o2) -> o2.getCreatedAt().getNano() - o1.getCreatedAt().getNano());
        post.setComments(comments);

        return post;
    }

}
