package com.tresende.catalog.admin.infrastructure.genre.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<GenreCategoryID, String> {
    Page<GenreCategoryID> findAll(Specification<GenreCategoryID> whereClause, Pageable page);
}
