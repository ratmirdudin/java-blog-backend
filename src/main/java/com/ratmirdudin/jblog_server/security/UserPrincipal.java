package com.ratmirdudin.jblog_server.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ratmirdudin.jblog_server.models.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {
    private final Long id;

    private final String username;

    @JsonIgnore
    private final String email;

    @JsonIgnore
    private final String password;

    private final boolean verified;

    private final boolean enabled;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities, boolean verified, boolean enabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.verified = verified;
        this.enabled = enabled;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(
                        role -> new SimpleGrantedAuthority(role.getName().name())
                )
                .collect(Collectors.toList());

        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getEncryptedPassword(),
                authorities,
                user.isVerified(),
                user.isEnabled());
    }


    public Long getId() {
        return id;
    }


    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return verified && enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return verified && enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
