package org.transandalus.backend.repository;

import org.transandalus.backend.domain.Volunteer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Volunteer entity.
 */
public interface VolunteerRepository extends JpaRepository<Volunteer,Long> {

}
