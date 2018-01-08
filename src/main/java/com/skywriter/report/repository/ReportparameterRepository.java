package com.skywriter.report.repository;

import com.skywriter.report.domain.Reportparameter;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Reportparameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportparameterRepository extends JpaRepository<Reportparameter, Long> {

	Page<Reportparameter> findByReportId(long reportId,Pageable pageable);

}
