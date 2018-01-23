package com.skywriter.report.repository;

import com.skywriter.report.domain.Reportclass;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Reportclass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportclassRepository extends JpaRepository<Reportclass, Long> {

}
