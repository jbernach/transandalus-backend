package org.transandalus.backend.repository;

import org.transandalus.backend.domain.Stage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Stage entity.
 */
public interface StageRepository extends JpaRepository<Stage,Long> {
	@Query("SELECT DISTINCT s FROM Stage s JOIN s.i18nName i18n JOIN i18n.translations t WHERE t.idLocale = :locale AND t.txContent LIKE %:filter%")
	Page<Stage> findByFilter(Pageable pageable, @Param("filter") String filter, @Param("locale") String locale);
	
	Page<Stage> findByProvinceId(Pageable pageable, @Param("province") Long province);
	
}
