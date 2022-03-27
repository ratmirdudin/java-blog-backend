package com.ratmirdudin.jblog_server.services;

import com.ratmirdudin.jblog_server.cachings.CategoryCachingService;
import com.ratmirdudin.jblog_server.exceptions.ResourceNotFoundException;
import com.ratmirdudin.jblog_server.models.entities.Category;
import com.ratmirdudin.jblog_server.models.entities.Post;
import com.ratmirdudin.jblog_server.models.entities.Role;
import com.ratmirdudin.jblog_server.models.entities.User;
import com.ratmirdudin.jblog_server.models.enums.CategoryEnum;
import com.ratmirdudin.jblog_server.models.enums.RoleEnum;
import com.ratmirdudin.jblog_server.repositories.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private CategoryCachingService categoryCachingService;
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private Post postOne;
    private Post postTwo;
    private Post postThree;

    private Category categoryJava;
    private Category categorySpring;
    private Category categoryDocker;
    private Category categoryKubernetes;

    @BeforeEach
    void setUp() {
        User userRatmir = new User(
                1L,
                "Ratmir",
                "Dudin",
                "ratmir",
                "ratmir@mail.ru",
                "password",
                true,
                Set.of(new Role(1L, RoleEnum.ROLE_USER), new Role(2L, RoleEnum.ROLE_ADMIN))
        );

        categoryJava = new Category(1L, CategoryEnum.JAVA);
        categorySpring = new Category(2L, CategoryEnum.SPRING);
        categoryDocker = new Category(3L, CategoryEnum.DOCKER);
        categoryKubernetes = new Category(4L, CategoryEnum.KUBERNETES);

        postOne = new Post(
                1L,
                "1.Title",
                "1.Content",
                userRatmir,
                Set.of(categoryJava, categorySpring)
        );
        postTwo = new Post(
                2L,
                "2.Title",
                "2.Content",
                userRatmir,
                Set.of(categoryJava, categoryDocker)
        );
        postThree = new Post(
                3L,
                "3.Title",
                "3.Content",
                userRatmir,
                Set.of(categoryJava, categoryKubernetes)
        );
    }

    @Test
    void whenGetAllPosts_thenReturnPageOfPosts() {
        List<Post> postList = List.of(postOne, postTwo, postThree);
        PageRequest pageable = PageRequest.of(0, 2, Sort.by("createdAt").descending());
        when(postRepository.findAllPosts("", pageable))
                .thenReturn(
                        new PageImpl<>(postList, pageable, postList.size())
                );
        Page<Post> postPage = postService.getAllPosts("", "all", 1, 2);

        assertEquals(2, postPage.getSize());
        assertEquals(3, postPage.getTotalElements());
    }

    @Test
    void whenGetAllPostsWithCategory_thenReturnPageOfPostsByCategory() {
        when(categoryCachingService.getCategory(CategoryEnum.valueOf("JAVA")))
                .thenReturn(Optional.of(categoryJava));

        List<Post> postList = List.of(postOne, postTwo, postThree);
        PageRequest pageable = PageRequest.of(0, 3, Sort.by("createdAt").descending());
        when(postRepository.findAllPostsByCategory(categoryJava, pageable))
                .thenReturn(
                        new PageImpl<>(postList, pageable, postList.size())
                );
        Page<Post> postPage = postService.getAllPosts("", "java", 1, 3);

        assertEquals(3, postPage.getSize());
        assertEquals(3, postPage.getTotalElements());
    }

    @Test
    void whenGetAllPostsWithCategory_thenThrowResourceNotFoundException() {
        when(categoryCachingService.getCategory(CategoryEnum.valueOf("JAVA")))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> postService.getAllPosts("", "java", 1, 3)
        );
    }

    @Test
    void whenGetDetailPostById_thenReturnDetailPost() {
        Optional<Post> optionalPostOne = Optional.of(postOne);
        when(postRepository.findById(anyLong())).thenReturn(optionalPostOne);
        Post postById = postService.getDetailPostById(1L);

        assertNotNull(postById);
        assertEquals("1.Title", postById.getTitle());
        assertEquals(0, postById.getComments().size());
    }

    @Test
    void whenGetDetailPostById_thenThrowResourceNotFoundException() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> postService.getDetailPostById(1L)
        );
    }
}