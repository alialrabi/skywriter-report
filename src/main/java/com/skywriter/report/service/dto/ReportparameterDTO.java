package com.skywriter.report.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Reportparameter entity.
 */
public class ReportparameterDTO implements Serializable {

    private Long id;

    @NotNull
    private String label;

    @NotNull
    private String instructions;

    @NotNull
    private String datatype;

    @NotNull
    private String required;

    @NotNull
    private String minlength;

    @NotNull
    private String maxlength;

    @NotNull
    private String validation;

    @NotNull
    private String status;

    private String lastmodifiedby;

    private ZonedDateTime lastmodifieddatetime;

    private String domain;

    private Long reportId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getMinlength() {
        return minlength;
    }

    public void setMinlength(String minlength) {
        this.minlength = minlength;
    }

    public String getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastmodifiedby() {
        return lastmodifiedby;
    }

    public void setLastmodifiedby(String lastmodifiedby) {
        this.lastmodifiedby = lastmodifiedby;
    }

    public ZonedDateTime getLastmodifieddatetime() {
        return lastmodifieddatetime;
    }

    public void setLastmodifieddatetime(ZonedDateTime lastmodifieddatetime) {
        this.lastmodifieddatetime = lastmodifieddatetime;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReportparameterDTO reportparameterDTO = (ReportparameterDTO) o;
        if(reportparameterDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reportparameterDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReportparameterDTO{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", instructions='" + getInstructions() + "'" +
            ", datatype='" + getDatatype() + "'" +
            ", required='" + getRequired() + "'" +
            ", minlength='" + getMinlength() + "'" +
            ", maxlength='" + getMaxlength() + "'" +
            ", validation='" + getValidation() + "'" +
            ", status='" + getStatus() + "'" +
            ", lastmodifiedby='" + getLastmodifiedby() + "'" +
            ", lastmodifieddatetime='" + getLastmodifieddatetime() + "'" +
            ", domain='" + getDomain() + "'" +
            "}";
    }
}
