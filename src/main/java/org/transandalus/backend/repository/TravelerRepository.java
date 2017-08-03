package org.transandalus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transandalus.backend.domain.Traveler;

/**
 * Spring Data JPA repository for the Traveler entity.
 */
public interface TravelerRepository extends JpaRepository<Traveler,Long> {

}
