package com.skywriter.report.service.mapper;

import com.skywriter.report.domain.*;
import com.skywriter.report.service.dto.ReportDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Report and its DTO ReportDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReportMapper extends EntityMapper<ReportDTO, Report> {

    

    @Mapping(target = "reportparameters", ignore = true)
    Report toEntity(ReportDTO reportDTO);

    default Report fromId(Long id) {
        if (id == null) {
            return null;
        }
        Report report = new Report();
        report.setId(id);
        return report;
    }
}
