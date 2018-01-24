package com.skywriter.report.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Report.
 */
@Entity
@Table(name = "report")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "report")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "reporttemplatename", nullable = false)
    private String reporttemplatename;

    @NotNull
    @Column(name = "reportoutputtypecode", nullable = false)
    private String reportoutputtypecode;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "lastmodifiedby", nullable = false)
    private String lastmodifiedby;

    @Column(name = "lastmodifieddatetime")
    private ZonedDateTime lastmodifieddatetime;

    @Column(name = "domain")
    private String domain;
    
    @Column(name = "bucket")
    private String bucket;

    @Lob
    @Column(name = "reportfile")
    private byte[] reportfile;

    @Column(name = "reportfile_content_type")
    private String reportfileContentType;

    @Lob
    @Column(name = "jrxmlfile")
    private byte[] jrxmlfile;

    @Column(name = "jrxmlfile_content_type")
    private String jrxmlfileContentType;
    
    

    @OneToMany(mappedBy = "report")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reportparameter> reportparameters = new HashSet<>();
    
    @ManyToOne
    private Reportclass reportclass;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Report name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReporttemplatename() {
        return reporttemplatename;
    }

    public Report reporttemplatename(String reporttemplatename) {
        this.reporttemplatename = reporttemplatename;
        return this;
    }

    public void setReporttemplatename(String reporttemplatename) {
        this.reporttemplatename = reporttemplatename;
    }

    public String getReportoutputtypecode() {
        return reportoutputtypecode;
    }

    public Report reportoutputtypecode(String reportoutputtypecode) {
        this.reportoutputtypecode = reportoutputtypecode;
        return this;
    }

    public void setReportoutputtypecode(String reportoutputtypecode) {
        this.reportoutputtypecode = reportoutputtypecode;
    }

    public String getStatus() {
        return status;
    }

    public Report status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastmodifiedby() {
        return lastmodifiedby;
    }

    public Report lastmodifiedby(String lastmodifiedby) {
        this.lastmodifiedby = lastmodifiedby;
        return this;
    }

    public void setLastmodifiedby(String lastmodifiedby) {
        this.lastmodifiedby = lastmodifiedby;
    }

    public ZonedDateTime getLastmodifieddatetime() {
        return lastmodifieddatetime;
    }

    public Report lastmodifieddatetime(ZonedDateTime lastmodifieddatetime) {
        this.lastmodifieddatetime = lastmodifieddatetime;
        return this;
    }

    public void setLastmodifieddatetime(ZonedDateTime lastmodifieddatetime) {
        this.lastmodifieddatetime = lastmodifieddatetime;
    }

    public String getDomain() {
        return domain;
    }

    public Report domain(String domain) {
        this.domain = domain;
        return this;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public byte[] getReportfile() {
        return reportfile;
    }

    public Report reportfile(byte[] reportfile) {
        this.reportfile = reportfile;
        return this;
    }

    public void setReportfile(byte[] reportfile) {
        this.reportfile = reportfile;
    }

    public String getReportfileContentType() {
        return reportfileContentType;
    }

    public Report reportfileContentType(String reportfileContentType) {
        this.reportfileContentType = reportfileContentType;
        return this;
    }
    

    public Reportclass getReportclass() {
		return reportclass;
	}

	public void setReportclass(Reportclass reportclass) {
		this.reportclass = reportclass;
	}

	public void setReportfileContentType(String reportfileContentType) {
        this.reportfileContentType = reportfileContentType;
    }

    public byte[] getJrxmlfile() {
        return jrxmlfile;
    }

    public Report jrxmlfile(byte[] jrxmlfile) {
        this.jrxmlfile = jrxmlfile;
        return this;
    }

    public void setJrxmlfile(byte[] jrxmlfile) {
        this.jrxmlfile = jrxmlfile;
    }

    public String getJrxmlfileContentType() {
        return jrxmlfileContentType;
    }

    public Report jrxmlfileContentType(String jrxmlfileContentType) {
        this.jrxmlfileContentType = jrxmlfileContentType;
        return this;
    }

    public void setJrxmlfileContentType(String jrxmlfileContentType) {
        this.jrxmlfileContentType = jrxmlfileContentType;
    }

    public Set<Reportparameter> getReportparameters() {
        return reportparameters;
    }

    public Report reportparameters(Set<Reportparameter> reportparameters) {
        this.reportparameters = reportparameters;
        return this;
    }

    public Report addReportparameter(Reportparameter reportparameter) {
        this.reportparameters.add(reportparameter);
        reportparameter.setReport(this);
        return this;
    }

    public Report removeReportparameter(Reportparameter reportparameter) {
        this.reportparameters.remove(reportparameter);
        reportparameter.setReport(null);
        return this;
    }

    public void setReportparameters(Set<Reportparameter> reportparameters) {
        this.reportparameters = reportparameters;
    }
    
    
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Report report = (Report) o;
        if (report.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), report.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Report{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", reporttemplatename='" + getReporttemplatename() + "'" +
            ", reportoutputtypecode='" + getReportoutputtypecode() + "'" +
            ", status='" + getStatus() + "'" +
            ", lastmodifiedby='" + getLastmodifiedby() + "'" +
            ", lastmodifieddatetime='" + getLastmodifieddatetime() + "'" +
            ", domain='" + getDomain() + "'" +
            ", reportfile='" + getReportfile() + "'" +
            ", reportfileContentType='" + getReportfileContentType() + "'" +
            ", jrxmlfile='" + getJrxmlfile() + "'" +
            ", jrxmlfileContentType='" + getJrxmlfileContentType() + "'" +
            "}";
    }
}
