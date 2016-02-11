package org.transandalus.backend.repository;

import org.transandalus.backend.domain.Track;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Track entity.
 */
public interface TrackRepository extends JpaRepository<Track,Long> {

}
