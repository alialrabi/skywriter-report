package com.skywriter.report.service.mapper;

import com.skywriter.report.domain.*;
import com.skywriter.report.service.dto.ReportclassDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Reportclass and its DTO ReportclassDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReportclassMapper extends EntityMapper<ReportclassDTO, Reportclass> {

    

    

    default Reportclass fromId(Long id) {
        if (id == null) {
            return null;
        }
        Reportclass reportclass = new Reportclass();
        reportclass.setId(id);
        return reportclass;
    }
}
