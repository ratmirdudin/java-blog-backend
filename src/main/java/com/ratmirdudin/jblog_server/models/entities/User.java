package com.ratmirdudin.jblog_server.models.entities;

import com.ratmirdudin.jblog_server.models.audit.DateAudit;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@Data
@NoArgsConstructor
public class User extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 60)
    private String firstname;

    @Column(length = 60)
    private String lastname;

    @Column(nullable = false, updatable = false)
    private String username;

    @Column(nullable = false, updatable = false)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;

    @Column(length = 30)
    private String verificationCode;

    @Column(nullable = false)
    private boolean verified;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean immunity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;

    public User(Long id, String firstname, String lastname, String username, String email, String encryptedPassword, boolean immunity, Set<Role> roles) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.verified = true;
        this.enabled = true;
        this.immunity = immunity;
        this.roles = roles;
    }
}
