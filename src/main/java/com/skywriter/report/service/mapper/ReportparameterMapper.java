package com.skywriter.report.service.mapper;

import com.skywriter.report.domain.*;
import com.skywriter.report.service.dto.ReportparameterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Reportparameter and its DTO ReportparameterDTO.
 */
@Mapper(componentModel = "spring", uses = {ReportMapper.class})
public interface ReportparameterMapper extends EntityMapper<ReportparameterDTO, Reportparameter> {

    @Mapping(source = "report.id", target = "reportId")
    ReportparameterDTO toDto(Reportparameter reportparameter); 

    @Mapping(source = "reportId", target = "report")
    Reportparameter toEntity(ReportparameterDTO reportparameterDTO);

    default Reportparameter fromId(Long id) {
        if (id == null) {
            return null;
        }
        Reportparameter reportparameter = new Reportparameter();
        reportparameter.setId(id);
        return reportparameter;
    }
}
