package com.ratmirdudin.jblog_server.models.entities;

import com.ratmirdudin.jblog_server.models.audit.DateAudit;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Comment extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
    @Column(nullable = false, updatable = false)
    private String username;
    @Column(nullable = false)
    private Long userId;
    @Column(columnDefinition = "text", nullable = false)
    private String message;

    public Comment(Post post, User user, String message) {
        this.post = post;
        this.username = user.getUsername();
        this.userId = user.getId();
        this.message = message;
    }

    public Comment(Long id, Post post, User user, String message) {
        this.id = id;
        this.post = post;
        this.username = user.getUsername();
        this.userId = user.getId();
        this.message = message;
    }
}

