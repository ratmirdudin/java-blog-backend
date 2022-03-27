package com.ratmirdudin.jblog_server.models.entities;

import com.ratmirdudin.jblog_server.models.audit.DateAudit;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Post extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "text", nullable = false)
    private String content;
//    private Integer likes;

    //    @Column
//    @ElementCollection(targetClass = String.class)
//    private Set<String> likedUsers = new HashSet<>();

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, updatable = false)
    private String username;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "posts_categories",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    private Set<Category> categories;

    public Post(Long id, String title, String content, User user, Set<Category> categories) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.username = user.getUsername();
        this.categories = categories;
    }
}
