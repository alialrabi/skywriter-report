package com.skywriter.report.repository.search;

import com.skywriter.report.domain.Reportparameter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Reportparameter entity.
 */
public interface ReportparameterSearchRepository extends ElasticsearchRepository<Reportparameter, Long> {
}
