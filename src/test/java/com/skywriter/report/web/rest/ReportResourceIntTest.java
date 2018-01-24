package com.skywriter.report.web.rest;

import com.skywriter.report.ReportApp;

import com.skywriter.report.config.SecurityBeanOverrideConfiguration;

import com.skywriter.report.domain.Report;
import com.skywriter.report.repository.ReportRepository;
import com.skywriter.report.repository.search.ReportSearchRepository;
import com.skywriter.report.service.dto.ReportDTO;
import com.skywriter.report.service.mapper.ReportMapper;
import com.skywriter.report.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.skywriter.report.web.rest.TestUtil.sameInstant;
import static com.skywriter.report.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReportResource REST controller.
 *
 * @see ReportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ReportApp.class, SecurityBeanOverrideConfiguration.class})
public class ReportResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REPORTTEMPLATENAME = "AAAAAAAAAA";
    private static final String UPDATED_REPORTTEMPLATENAME = "BBBBBBBBBB";

    private static final String DEFAULT_REPORTOUTPUTTYPECODE = "AAAAAAAAAA";
    private static final String UPDATED_REPORTOUTPUTTYPECODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_LASTMODIFIEDBY = "AAAAAAAAAA";
    private static final String UPDATED_LASTMODIFIEDBY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_LASTMODIFIEDDATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LASTMODIFIEDDATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN = "BBBBBBBBBB";

    private static final byte[] DEFAULT_REPORTFILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_REPORTFILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_REPORTFILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_REPORTFILE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_JRXMLFILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_JRXMLFILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_JRXMLFILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_JRXMLFILE_CONTENT_TYPE = "image/png";

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private ReportSearchRepository reportSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReportMockMvc;

    private Report report;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReportResource reportResource = new ReportResource(reportRepository, reportMapper, reportSearchRepository);
        this.restReportMockMvc = MockMvcBuilders.standaloneSetup(reportResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createEntity(EntityManager em) {
        Report report = new Report()
            .name(DEFAULT_NAME)
            .reporttemplatename(DEFAULT_REPORTTEMPLATENAME)
            .reportoutputtypecode(DEFAULT_REPORTOUTPUTTYPECODE)
            .status(DEFAULT_STATUS)
            .lastmodifiedby(DEFAULT_LASTMODIFIEDBY)
            .lastmodifieddatetime(DEFAULT_LASTMODIFIEDDATETIME)
            .domain(DEFAULT_DOMAIN)
            .reportfile(DEFAULT_REPORTFILE)
            .reportfileContentType(DEFAULT_REPORTFILE_CONTENT_TYPE)
            .jrxmlfile(DEFAULT_JRXMLFILE)
            .jrxmlfileContentType(DEFAULT_JRXMLFILE_CONTENT_TYPE);
        return report;
    }

    @Before
    public void initTest() {
        reportSearchRepository.deleteAll();
        report = createEntity(em);
    }

    @Test
    @Transactional
    public void createReport() throws Exception {
        int databaseSizeBeforeCreate = reportRepository.findAll().size();

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);
        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isCreated());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate + 1);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testReport.getReporttemplatename()).isEqualTo(DEFAULT_REPORTTEMPLATENAME);
        assertThat(testReport.getReportoutputtypecode()).isEqualTo(DEFAULT_REPORTOUTPUTTYPECODE);
        assertThat(testReport.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReport.getLastmodifiedby()).isEqualTo(DEFAULT_LASTMODIFIEDBY);
        assertThat(testReport.getLastmodifieddatetime()).isEqualTo(DEFAULT_LASTMODIFIEDDATETIME);
        assertThat(testReport.getDomain()).isEqualTo(DEFAULT_DOMAIN);
        assertThat(testReport.getReportfile()).isEqualTo(DEFAULT_REPORTFILE);
        assertThat(testReport.getReportfileContentType()).isEqualTo(DEFAULT_REPORTFILE_CONTENT_TYPE);
        assertThat(testReport.getJrxmlfile()).isEqualTo(DEFAULT_JRXMLFILE);
        assertThat(testReport.getJrxmlfileContentType()).isEqualTo(DEFAULT_JRXMLFILE_CONTENT_TYPE);

        // Validate the Report in Elasticsearch
        Report reportEs = reportSearchRepository.findOne(testReport.getId());
        assertThat(testReport.getLastmodifieddatetime()).isEqualTo(testReport.getLastmodifieddatetime());
        assertThat(reportEs).isEqualToIgnoringGivenFields(testReport, "lastmodifieddatetime");
    }

    @Test
    @Transactional
    public void createReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reportRepository.findAll().size();

        // Create the Report with an existing ID
        report.setId(1L);
        ReportDTO reportDTO = reportMapper.toDto(report);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setName(null);

        // Create the Report, which fails.
        ReportDTO reportDTO = reportMapper.toDto(report);

        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReporttemplatenameIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setReporttemplatename(null);

        // Create the Report, which fails.
        ReportDTO reportDTO = reportMapper.toDto(report);

        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReportoutputtypecodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setReportoutputtypecode(null);

        // Create the Report, which fails.
        ReportDTO reportDTO = reportMapper.toDto(report);

        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setStatus(null);

        // Create the Report, which fails.
        ReportDTO reportDTO = reportMapper.toDto(report);

        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastmodifiedbyIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setLastmodifiedby(null);

        // Create the Report, which fails.
        ReportDTO reportDTO = reportMapper.toDto(report);

        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReports() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList
        restReportMockMvc.perform(get("/api/reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].reporttemplatename").value(hasItem(DEFAULT_REPORTTEMPLATENAME.toString())))
            .andExpect(jsonPath("$.[*].reportoutputtypecode").value(hasItem(DEFAULT_REPORTOUTPUTTYPECODE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
            .andExpect(jsonPath("$.[*].lastmodifieddatetime").value(hasItem(sameInstant(DEFAULT_LASTMODIFIEDDATETIME))))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())))
            .andExpect(jsonPath("$.[*].reportfileContentType").value(hasItem(DEFAULT_REPORTFILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportfile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORTFILE))))
            .andExpect(jsonPath("$.[*].jrxmlfileContentType").value(hasItem(DEFAULT_JRXMLFILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].jrxmlfile").value(hasItem(Base64Utils.encodeToString(DEFAULT_JRXMLFILE))));
    }

    @Test
    @Transactional
    public void getReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get the report
        restReportMockMvc.perform(get("/api/reports/{id}", report.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(report.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.reporttemplatename").value(DEFAULT_REPORTTEMPLATENAME.toString()))
            .andExpect(jsonPath("$.reportoutputtypecode").value(DEFAULT_REPORTOUTPUTTYPECODE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastmodifiedby").value(DEFAULT_LASTMODIFIEDBY.toString()))
            .andExpect(jsonPath("$.lastmodifieddatetime").value(sameInstant(DEFAULT_LASTMODIFIEDDATETIME)))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()))
            .andExpect(jsonPath("$.reportfileContentType").value(DEFAULT_REPORTFILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportfile").value(Base64Utils.encodeToString(DEFAULT_REPORTFILE)))
            .andExpect(jsonPath("$.jrxmlfileContentType").value(DEFAULT_JRXMLFILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.jrxmlfile").value(Base64Utils.encodeToString(DEFAULT_JRXMLFILE)));
    }

    @Test
    @Transactional
    public void getNonExistingReport() throws Exception {
        // Get the report
        restReportMockMvc.perform(get("/api/reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);
        reportSearchRepository.save(report);
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report
        Report updatedReport = reportRepository.findOne(report.getId());
        // Disconnect from session so that the updates on updatedReport are not directly saved in db
        em.detach(updatedReport);
        updatedReport
            .name(UPDATED_NAME)
            .reporttemplatename(UPDATED_REPORTTEMPLATENAME)
            .reportoutputtypecode(UPDATED_REPORTOUTPUTTYPECODE)
            .status(UPDATED_STATUS)
            .lastmodifiedby(UPDATED_LASTMODIFIEDBY)
            .lastmodifieddatetime(UPDATED_LASTMODIFIEDDATETIME)
            .domain(UPDATED_DOMAIN)
            .reportfile(UPDATED_REPORTFILE)
            .reportfileContentType(UPDATED_REPORTFILE_CONTENT_TYPE)
            .jrxmlfile(UPDATED_JRXMLFILE)
            .jrxmlfileContentType(UPDATED_JRXMLFILE_CONTENT_TYPE);
        ReportDTO reportDTO = reportMapper.toDto(updatedReport);

        restReportMockMvc.perform(put("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReport.getReporttemplatename()).isEqualTo(UPDATED_REPORTTEMPLATENAME);
        assertThat(testReport.getReportoutputtypecode()).isEqualTo(UPDATED_REPORTOUTPUTTYPECODE);
        assertThat(testReport.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReport.getLastmodifiedby()).isEqualTo(UPDATED_LASTMODIFIEDBY);
        assertThat(testReport.getLastmodifieddatetime()).isEqualTo(UPDATED_LASTMODIFIEDDATETIME);
        assertThat(testReport.getDomain()).isEqualTo(UPDATED_DOMAIN);
        assertThat(testReport.getReportfile()).isEqualTo(UPDATED_REPORTFILE);
        assertThat(testReport.getReportfileContentType()).isEqualTo(UPDATED_REPORTFILE_CONTENT_TYPE);
        assertThat(testReport.getJrxmlfile()).isEqualTo(UPDATED_JRXMLFILE);
        assertThat(testReport.getJrxmlfileContentType()).isEqualTo(UPDATED_JRXMLFILE_CONTENT_TYPE);

        // Validate the Report in Elasticsearch
        Report reportEs = reportSearchRepository.findOne(testReport.getId());
        assertThat(testReport.getLastmodifieddatetime()).isEqualTo(testReport.getLastmodifieddatetime());
        assertThat(reportEs).isEqualToIgnoringGivenFields(testReport, "lastmodifieddatetime");
    }

    @Test
    @Transactional
    public void updateNonExistingReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReportMockMvc.perform(put("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isCreated());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);
        reportSearchRepository.save(report);
        int databaseSizeBeforeDelete = reportRepository.findAll().size();

        // Get the report
        restReportMockMvc.perform(delete("/api/reports/{id}", report.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean reportExistsInEs = reportSearchRepository.exists(report.getId());
        assertThat(reportExistsInEs).isFalse();

        // Validate the database is empty
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);
        reportSearchRepository.save(report);

        // Search the report
        restReportMockMvc.perform(get("/api/_search/reports?query=id:" + report.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].reporttemplatename").value(hasItem(DEFAULT_REPORTTEMPLATENAME.toString())))
            .andExpect(jsonPath("$.[*].reportoutputtypecode").value(hasItem(DEFAULT_REPORTOUTPUTTYPECODE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
            .andExpect(jsonPath("$.[*].lastmodifieddatetime").value(hasItem(sameInstant(DEFAULT_LASTMODIFIEDDATETIME))))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())))
            .andExpect(jsonPath("$.[*].reportfileContentType").value(hasItem(DEFAULT_REPORTFILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportfile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORTFILE))))
            .andExpect(jsonPath("$.[*].jrxmlfileContentType").value(hasItem(DEFAULT_JRXMLFILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].jrxmlfile").value(hasItem(Base64Utils.encodeToString(DEFAULT_JRXMLFILE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Report.class);
        Report report1 = new Report();
        report1.setId(1L);
        Report report2 = new Report();
        report2.setId(report1.getId());
        assertThat(report1).isEqualTo(report2);
        report2.setId(2L);
        assertThat(report1).isNotEqualTo(report2);
        report1.setId(null);
        assertThat(report1).isNotEqualTo(report2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDTO.class);
        ReportDTO reportDTO1 = new ReportDTO();
        reportDTO1.setId(1L);
        ReportDTO reportDTO2 = new ReportDTO();
        assertThat(reportDTO1).isNotEqualTo(reportDTO2);
        reportDTO2.setId(reportDTO1.getId());
        assertThat(reportDTO1).isEqualTo(reportDTO2);
        reportDTO2.setId(2L);
        assertThat(reportDTO1).isNotEqualTo(reportDTO2);
        reportDTO1.setId(null);
        assertThat(reportDTO1).isNotEqualTo(reportDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(reportMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(reportMapper.fromId(null)).isNull();
    }
}
