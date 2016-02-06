package org.transandalus.backend.repository;

import org.transandalus.backend.domain.Stage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Stage entity.
 */
public interface StageRepository extends JpaRepository<Stage,Long> {

}
