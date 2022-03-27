package com.ratmirdudin.jblog_server.models.entities;

import com.ratmirdudin.jblog_server.models.enums.CategoryEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private CategoryEnum name;

    public Category(Long id, CategoryEnum name) {
        this.id = id;
        this.name = name;
    }
}

