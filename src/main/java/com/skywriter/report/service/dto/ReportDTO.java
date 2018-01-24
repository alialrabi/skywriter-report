package com.skywriter.report.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Report entity.
 */
public class ReportDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String reporttemplatename;

    @NotNull
    private String reportoutputtypecode;

    private String status;

    private String lastmodifiedby;

    private ZonedDateTime lastmodifieddatetime;

    private String domain;
    
    private String bucket;

    @Lob
    private byte[] reportfile;
    private String reportfileContentType;

    @Lob
    private byte[] jrxmlfile;
    private String jrxmlfileContentType;
    
    private Long reportclassId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReporttemplatename() {
        return reporttemplatename;
    }

    public void setReporttemplatename(String reporttemplatename) {
        this.reporttemplatename = reporttemplatename;
    }

    public String getReportoutputtypecode() {
        return reportoutputtypecode;
    }

    public void setReportoutputtypecode(String reportoutputtypecode) {
        this.reportoutputtypecode = reportoutputtypecode;
    }

    public String getStatus() {
        return status;
    }

    public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
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

    public byte[] getReportfile() {
        return reportfile;
    }

    public void setReportfile(byte[] reportfile) {
        this.reportfile = reportfile;
    }

    public String getReportfileContentType() {
        return reportfileContentType;
    }

    public void setReportfileContentType(String reportfileContentType) {
        this.reportfileContentType = reportfileContentType;
    }

    public byte[] getJrxmlfile() {
        return jrxmlfile;
    }

    public void setJrxmlfile(byte[] jrxmlfile) {
        this.jrxmlfile = jrxmlfile;
    }

    public String getJrxmlfileContentType() {
        return jrxmlfileContentType;
    }

    public void setJrxmlfileContentType(String jrxmlfileContentType) {
        this.jrxmlfileContentType = jrxmlfileContentType;
    }

    public Long getReportclassId() {
		return reportclassId;
	}

	public void setReportclassId(Long reportclassId) {
		this.reportclassId = reportclassId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReportDTO reportDTO = (ReportDTO) o;
        if(reportDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reportDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReportDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", reporttemplatename='" + getReporttemplatename() + "'" +
            ", reportoutputtypecode='" + getReportoutputtypecode() + "'" +
            ", status='" + getStatus() + "'" +
            ", lastmodifiedby='" + getLastmodifiedby() + "'" +
            ", lastmodifieddatetime='" + getLastmodifieddatetime() + "'" +
            ", domain='" + getDomain() + "'" +
            ", reportfile='" + getReportfile() + "'" +
            ", jrxmlfile='" + getJrxmlfile() + "'" +
            "}";
    }
}
