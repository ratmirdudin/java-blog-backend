package com.ratmirdudin.jblog_server.cachings;

import com.ratmirdudin.jblog_server.models.entities.Role;
import com.ratmirdudin.jblog_server.models.enums.RoleEnum;
import com.ratmirdudin.jblog_server.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleCachingService {

    private final RoleRepository roleRepository;

    @Cacheable(value = "roles", key = "#role.name()")
    public Optional<Role> getRole(RoleEnum role) {
        return roleRepository.findByName(role);
    }
}
