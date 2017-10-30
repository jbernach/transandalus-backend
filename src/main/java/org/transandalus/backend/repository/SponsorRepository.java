package org.transandalus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transandalus.backend.domain.Sponsor;

/**
 * Spring Data JPA repository for the Sponsor entity.
 */
public interface SponsorRepository extends JpaRepository<Sponsor,Long> {

}
