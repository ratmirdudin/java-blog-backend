package com.ratmirdudin.jblog_server.repositories;

import com.ratmirdudin.jblog_server.models.entities.Category;
import com.ratmirdudin.jblog_server.models.enums.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(CategoryEnum categoryEnum);

}
