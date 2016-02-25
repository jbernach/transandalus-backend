package org.transandalus.backend.repository;

import org.transandalus.backend.domain.MenuItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MenuItem entity.
 */
public interface MenuItemRepository extends JpaRepository<MenuItem,Long> {

}
