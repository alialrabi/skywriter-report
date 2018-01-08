package com.skywriter.report.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Reportparameter.
 */
@Entity
@Table(name = "reportparameter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reportparameter")
public class Reportparameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @NotNull
    @Column(name = "instructions", nullable = false)
    private String instructions;

    @NotNull
    @Column(name = "datatype", nullable = false)
    private String datatype;

    @NotNull
    @Column(name = "required", nullable = false)
    private String required;

    @NotNull
    @Column(name = "minlength", nullable = false)
    private String minlength;

    @NotNull
    @Column(name = "maxlength", nullable = false)
    private String maxlength;

    @NotNull
    @Column(name = "validation", nullable = false)
    private String validation;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "lastmodifiedby")
    private String lastmodifiedby;

    @Column(name = "lastmodifieddatetime")
    private ZonedDateTime lastmodifieddatetime;

    @Column(name = "domain")
    private String domain;

    @ManyToOne
    private Report report;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public Reportparameter label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInstructions() {
        return instructions;
    }

    public Reportparameter instructions(String instructions) {
        this.instructions = instructions;
        return this;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getDatatype() {
        return datatype;
    }

    public Reportparameter datatype(String datatype) {
        this.datatype = datatype;
        return this;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getRequired() {
        return required;
    }

    public Reportparameter required(String required) {
        this.required = required;
        return this;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getMinlength() {
        return minlength;
    }

    public Reportparameter minlength(String minlength) {
        this.minlength = minlength;
        return this;
    }

    public void setMinlength(String minlength) {
        this.minlength = minlength;
    }

    public String getMaxlength() {
        return maxlength;
    }

    public Reportparameter maxlength(String maxlength) {
        this.maxlength = maxlength;
        return this;
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    public String getValidation() {
        return validation;
    }

    public Reportparameter validation(String validation) {
        this.validation = validation;
        return this;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getStatus() {
        return status;
    }

    public Reportparameter status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastmodifiedby() {
        return lastmodifiedby;
    }

    public Reportparameter lastmodifiedby(String lastmodifiedby) {
        this.lastmodifiedby = lastmodifiedby;
        return this;
    }

    public void setLastmodifiedby(String lastmodifiedby) {
        this.lastmodifiedby = lastmodifiedby;
    }

    public ZonedDateTime getLastmodifieddatetime() {
        return lastmodifieddatetime;
    }

    public Reportparameter lastmodifieddatetime(ZonedDateTime lastmodifieddatetime) {
        this.lastmodifieddatetime = lastmodifieddatetime;
        return this;
    }

    public void setLastmodifieddatetime(ZonedDateTime lastmodifieddatetime) {
        this.lastmodifieddatetime = lastmodifieddatetime;
    }

    public String getDomain() {
        return domain;
    }

    public Reportparameter domain(String domain) {
        this.domain = domain;
        return this;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Report getReport() {
        return report;
    }

    public Reportparameter report(Report report) {
        this.report = report;
        return this;
    }

    public void setReport(Report report) {
        this.report = report;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reportparameter reportparameter = (Reportparameter) o;
        if (reportparameter.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reportparameter.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Reportparameter{" +
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
