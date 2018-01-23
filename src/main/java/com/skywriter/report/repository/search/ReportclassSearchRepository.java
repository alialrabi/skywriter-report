package com.skywriter.report.repository.search;

import com.skywriter.report.domain.Reportclass;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Reportclass entity.
 */
public interface ReportclassSearchRepository extends ElasticsearchRepository<Reportclass, Long> {
}
