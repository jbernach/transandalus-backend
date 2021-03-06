package org.transandalus.backend.repository;

import org.transandalus.backend.domain.Category;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Category entity.
 */
public interface CategoryRepository extends JpaRepository<Category,Long> {

}
