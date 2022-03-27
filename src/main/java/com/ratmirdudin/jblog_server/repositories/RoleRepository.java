package com.ratmirdudin.jblog_server.repositories;

import com.ratmirdudin.jblog_server.models.entities.Role;
import com.ratmirdudin.jblog_server.models.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum roleEnum);
}
