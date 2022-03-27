package com.ratmirdudin.jblog_server.cachings;

import com.ratmirdudin.jblog_server.models.entities.Category;
import com.ratmirdudin.jblog_server.models.enums.CategoryEnum;
import com.ratmirdudin.jblog_server.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryCachingService {

    private final CategoryRepository categoryRepository;

    @Cacheable(value = "categories", key = "#category.name()")
    public Optional<Category> getCategory(CategoryEnum category) {
        return categoryRepository.findByName(category);
    }
}
