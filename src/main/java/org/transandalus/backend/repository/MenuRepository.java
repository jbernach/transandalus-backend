package org.transandalus.backend.repository;

import org.transandalus.backend.domain.Menu;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Menu entity.
 */
public interface MenuRepository extends JpaRepository<Menu,Long> {

}
