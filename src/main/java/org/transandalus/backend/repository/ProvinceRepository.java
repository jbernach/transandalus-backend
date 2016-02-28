package org.transandalus.backend.repository;

import org.transandalus.backend.domain.Province;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Province entity.
 */
public interface ProvinceRepository extends JpaRepository<Province,Long> {

}
