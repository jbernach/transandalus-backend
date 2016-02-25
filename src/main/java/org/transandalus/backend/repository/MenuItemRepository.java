package org.transandalus.backend.repository;

import org.transandalus.backend.domain.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the MenuItem entity.
 */
public interface MenuItemRepository extends JpaRepository<MenuItem,Long> {
	Page<MenuItem> findByMenuId(Pageable pageable, @Param("menu") Long menu);
}
