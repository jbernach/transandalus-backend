package org.transandalus.backend.repository;

import org.transandalus.backend.domain.Article;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Article entity.
 */
public interface ArticleRepository extends JpaRepository<Article,Long> {

}
