package com.skywriter.report.web.rest;

import com.skywriter.report.ReportApp;

import com.skywriter.report.config.SecurityBeanOverrideConfiguration;

import com.skywriter.report.domain.Reportparameter;
import com.skywriter.report.repository.ReportparameterRepository;
import com.skywriter.report.repository.search.ReportparameterSearchRepository;
import com.skywriter.report.service.dto.ReportparameterDTO;
import com.skywriter.report.service.mapper.ReportparameterMapper;
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
 * Test class for the ReportparameterResource REST controller.
 *
 * @see ReportparameterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ReportApp.class, SecurityBeanOverrideConfiguration.class})
public class ReportparameterResourceIntTest {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_INSTRUCTIONS = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTIONS = "BBBBBBBBBB";

    private static final String DEFAULT_DATATYPE = "AAAAAAAAAA";
    private static final String UPDATED_DATATYPE = "BBBBBBBBBB";

    private static final String DEFAULT_REQUIRED = "AAAAAAAAAA";
    private static final String UPDATED_REQUIRED = "BBBBBBBBBB";

    private static final String DEFAULT_MINLENGTH = "AAAAAAAAAA";
    private static final String UPDATED_MINLENGTH = "BBBBBBBBBB";

    private static final String DEFAULT_MAXLENGTH = "AAAAAAAAAA";
    private static final String UPDATED_MAXLENGTH = "BBBBBBBBBB";

    private static final String DEFAULT_VALIDATION = "AAAAAAAAAA";
    private static final String UPDATED_VALIDATION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_LASTMODIFIEDBY = "AAAAAAAAAA";
    private static final String UPDATED_LASTMODIFIEDBY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_LASTMODIFIEDDATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LASTMODIFIEDDATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN = "BBBBBBBBBB";

    @Autowired
    private ReportparameterRepository reportparameterRepository;

    @Autowired
    private ReportparameterMapper reportparameterMapper;

    @Autowired
    private ReportparameterSearchRepository reportparameterSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReportparameterMockMvc;

    private Reportparameter reportparameter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReportparameterResource reportparameterResource = new ReportparameterResource(reportparameterRepository, reportparameterMapper, reportparameterSearchRepository);
        this.restReportparameterMockMvc = MockMvcBuilders.standaloneSetup(reportparameterResource)
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
    public static Reportparameter createEntity(EntityManager em) {
        Reportparameter reportparameter = new Reportparameter()
            .label(DEFAULT_LABEL)
            .instructions(DEFAULT_INSTRUCTIONS)
            .datatype(DEFAULT_DATATYPE)
            .required(DEFAULT_REQUIRED)
            .minlength(DEFAULT_MINLENGTH)
            .maxlength(DEFAULT_MAXLENGTH)
            .validation(DEFAULT_VALIDATION)
            .status(DEFAULT_STATUS)
            .lastmodifiedby(DEFAULT_LASTMODIFIEDBY)
            .lastmodifieddatetime(DEFAULT_LASTMODIFIEDDATETIME)
            .domain(DEFAULT_DOMAIN);
        return reportparameter;
    }

    @Before
    public void initTest() {
        reportparameterSearchRepository.deleteAll();
        reportparameter = createEntity(em);
    }

    @Test
    @Transactional
    public void createReportparameter() throws Exception {
        int databaseSizeBeforeCreate = reportparameterRepository.findAll().size();

        // Create the Reportparameter
        ReportparameterDTO reportparameterDTO = reportparameterMapper.toDto(reportparameter);
        restReportparameterMockMvc.perform(post("/api/reportparameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportparameterDTO)))
            .andExpect(status().isCreated());

        // Validate the Reportparameter in the database
        List<Reportparameter> reportparameterList = reportparameterRepository.findAll();
        assertThat(reportparameterList).hasSize(databaseSizeBeforeCreate + 1);
        Reportparameter testReportparameter = reportparameterList.get(reportparameterList.size() - 1);
        assertThat(testReportparameter.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testReportparameter.getInstructions()).isEqualTo(DEFAULT_INSTRUCTIONS);
        assertThat(testReportparameter.getDatatype()).isEqualTo(DEFAULT_DATATYPE);
        assertThat(testReportparameter.getRequired()).isEqualTo(DEFAULT_REQUIRED);
        assertThat(testReportparameter.getMinlength()).isEqualTo(DEFAULT_MINLENGTH);
        assertThat(testReportparameter.getMaxlength()).isEqualTo(DEFAULT_MAXLENGTH);
        assertThat(testReportparameter.getValidation()).isEqualTo(DEFAULT_VALIDATION);
        assertThat(testReportparameter.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReportparameter.getLastmodifiedby()).isEqualTo(DEFAULT_LASTMODIFIEDBY);
        assertThat(testReportparameter.getLastmodifieddatetime()).isEqualTo(DEFAULT_LASTMODIFIEDDATETIME);
        assertThat(testReportparameter.getDomain()).isEqualTo(DEFAULT_DOMAIN);

        // Validate the Reportparameter in Elasticsearch
        Reportparameter reportparameterEs = reportparameterSearchRepository.findOne(testReportparameter.getId());
        assertThat(testReportparameter.getLastmodifieddatetime()).isEqualTo(testReportparameter.getLastmodifieddatetime());
        assertThat(reportparameterEs).isEqualToIgnoringGivenFields(testReportparameter, "lastmodifieddatetime");
    }

    @Test
    @Transactional
    public void createReportparameterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reportparameterRepository.findAll().size();

        // Create the Reportparameter with an existing ID
        reportparameter.setId(1L);
        ReportparameterDTO reportparameterDTO = reportparameterMapper.toDto(reportparameter);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportparameterMockMvc.perform(post("/api/reportparameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportparameterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reportparameter in the database
        List<Reportparameter> reportparameterList = reportparameterRepository.findAll();
        assertThat(reportparameterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportparameterRepository.findAll().size();
        // set the field null
        reportparameter.setLabel(null);

        // Create the Reportparameter, which fails.
        ReportparameterDTO reportparameterDTO = reportparameterMapper.toDto(reportparameter);

        restReportparameterMockMvc.perform(post("/api/reportparameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportparameterDTO)))
            .andExpect(status().isBadRequest());

        List<Reportparameter> reportparameterList = reportparameterRepository.findAll();
        assertThat(reportparameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInstructionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportparameterRepository.findAll().size();
        // set the field null
        reportparameter.setInstructions(null);

        // Create the Reportparameter, which fails.
        ReportparameterDTO reportparameterDTO = reportparameterMapper.toDto(reportparameter);

        restReportparameterMockMvc.perform(post("/api/reportparameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportparameterDTO)))
            .andExpect(status().isBadRequest());

        List<Reportparameter> reportparameterList = reportparameterRepository.findAll();
        assertThat(reportparameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDatatypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportparameterRepository.findAll().size();
        // set the field null
        reportparameter.setDatatype(null);

        // Create the Reportparameter, which fails.
        ReportparameterDTO reportparameterDTO = reportparameterMapper.toDto(reportparameter);

        restReportparameterMockMvc.perform(post("/api/reportparameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportparameterDTO)))
            .andExpect(status().isBadRequest());

        List<Reportparameter> reportparameterList = reportparameterRepository.findAll();
        assertThat(reportparameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRequiredIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportparameterRepository.findAll().size();
        // set the field null
        reportparameter.setRequired(null);

        // Create the Reportparameter, which fails.
        ReportparameterDTO reportparameterDTO = reportparameterMapper.toDto(reportparameter);

        restReportparameterMockMvc.perform(post("/api/reportparameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportparameterDTO)))
            .andExpect(status().isBadRequest());

        List<Reportparameter> reportparameterList = reportparameterRepository.findAll();
        assertThat(reportparameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMinlengthIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportparameterRepository.findAll().size();
        // set the field null
        reportparameter.setMinlength(null);

        // Create the Reportparameter, which fails.
        ReportparameterDTO reportparameterDTO = reportparameterMapper.toDto(reportparameter);

        restReportparameterMockMvc.perform(post("/api/reportparameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportparameterDTO)))
            .andExpect(status().isBadRequest());

        List<Reportparameter> reportparameterList = reportparameterRepository.findAll();
        assertThat(reportparameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaxlengthIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportparameterRepository.findAll().size();
        // set the field null
        reportparameter.setMaxlength(null);

        // Create the Reportparameter, which fails.
        ReportparameterDTO reportparameterDTO = reportparameterMapper.toDto(reportparameter);

        restReportparameterMockMvc.perform(post("/api/reportparameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportparameterDTO)))
            .andExpect(status().isBadRequest());

        List<Reportparameter> reportparameterList = reportparameterRepository.findAll();
        assertThat(reportparameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValidationIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportparameterRepository.findAll().size();
        // set the field null
        reportparameter.setValidation(null);

        // Create the Reportparameter, which fails.
        ReportparameterDTO reportparameterDTO = reportparameterMapper.toDto(reportparameter);

        restReportparameterMockMvc.perform(post("/api/reportparameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportparameterDTO)))
            .andExpect(status().isBadRequest());

        List<Reportparameter> reportparameterList = reportparameterRepository.findAll();
        assertThat(reportparameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportparameterRepository.findAll().size();
        // set the field null
        reportparameter.setStatus(null);

        // Create the Reportparameter, which fails.
        ReportparameterDTO reportparameterDTO = reportparameterMapper.toDto(reportparameter);

        restReportparameterMockMvc.perform(post("/api/reportparameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportparameterDTO)))
            .andExpect(status().isBadRequest());

        List<Reportparameter> reportparameterList = reportparameterRepository.findAll();
        assertThat(reportparameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReportparameters() throws Exception {
        // Initialize the database
        reportparameterRepository.saveAndFlush(reportparameter);

        // Get all the reportparameterList
        restReportparameterMockMvc.perform(get("/api/reportparameters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportparameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].instructions").value(hasItem(DEFAULT_INSTRUCTIONS.toString())))
            .andExpect(jsonPath("$.[*].datatype").value(hasItem(DEFAULT_DATATYPE.toString())))
            .andExpect(jsonPath("$.[*].required").value(hasItem(DEFAULT_REQUIRED.toString())))
            .andExpect(jsonPath("$.[*].minlength").value(hasItem(DEFAULT_MINLENGTH.toString())))
            .andExpect(jsonPath("$.[*].maxlength").value(hasItem(DEFAULT_MAXLENGTH.toString())))
            .andExpect(jsonPath("$.[*].validation").value(hasItem(DEFAULT_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
            .andExpect(jsonPath("$.[*].lastmodifieddatetime").value(hasItem(sameInstant(DEFAULT_LASTMODIFIEDDATETIME))))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())));
    }

    @Test
    @Transactional
    public void getReportparameter() throws Exception {
        // Initialize the database
        reportparameterRepository.saveAndFlush(reportparameter);

        // Get the reportparameter
        restReportparameterMockMvc.perform(get("/api/reportparameters/{id}", reportparameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reportparameter.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
            .andExpect(jsonPath("$.instructions").value(DEFAULT_INSTRUCTIONS.toString()))
            .andExpect(jsonPath("$.datatype").value(DEFAULT_DATATYPE.toString()))
            .andExpect(jsonPath("$.required").value(DEFAULT_REQUIRED.toString()))
            .andExpect(jsonPath("$.minlength").value(DEFAULT_MINLENGTH.toString()))
            .andExpect(jsonPath("$.maxlength").value(DEFAULT_MAXLENGTH.toString()))
            .andExpect(jsonPath("$.validation").value(DEFAULT_VALIDATION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastmodifiedby").value(DEFAULT_LASTMODIFIEDBY.toString()))
            .andExpect(jsonPath("$.lastmodifieddatetime").value(sameInstant(DEFAULT_LASTMODIFIEDDATETIME)))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReportparameter() throws Exception {
        // Get the reportparameter
        restReportparameterMockMvc.perform(get("/api/reportparameters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReportparameter() throws Exception {
        // Initialize the database
        reportparameterRepository.saveAndFlush(reportparameter);
        reportparameterSearchRepository.save(reportparameter);
        int databaseSizeBeforeUpdate = reportparameterRepository.findAll().size();

        // Update the reportparameter
        Reportparameter updatedReportparameter = reportparameterRepository.findOne(reportparameter.getId());
        // Disconnect from session so that the updates on updatedReportparameter are not directly saved in db
        em.detach(updatedReportparameter);
        updatedReportparameter
            .label(UPDATED_LABEL)
            .instructions(UPDATED_INSTRUCTIONS)
            .datatype(UPDATED_DATATYPE)
            .required(UPDATED_REQUIRED)
            .minlength(UPDATED_MINLENGTH)
            .maxlength(UPDATED_MAXLENGTH)
            .validation(UPDATED_VALIDATION)
            .status(UPDATED_STATUS)
            .lastmodifiedby(UPDATED_LASTMODIFIEDBY)
            .lastmodifieddatetime(UPDATED_LASTMODIFIEDDATETIME)
            .domain(UPDATED_DOMAIN);
        ReportparameterDTO reportparameterDTO = reportparameterMapper.toDto(updatedReportparameter);

        restReportparameterMockMvc.perform(put("/api/reportparameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportparameterDTO)))
            .andExpect(status().isOk());

        // Validate the Reportparameter in the database
        List<Reportparameter> reportparameterList = reportparameterRepository.findAll();
        assertThat(reportparameterList).hasSize(databaseSizeBeforeUpdate);
        Reportparameter testReportparameter = reportparameterList.get(reportparameterList.size() - 1);
        assertThat(testReportparameter.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testReportparameter.getInstructions()).isEqualTo(UPDATED_INSTRUCTIONS);
        assertThat(testReportparameter.getDatatype()).isEqualTo(UPDATED_DATATYPE);
        assertThat(testReportparameter.getRequired()).isEqualTo(UPDATED_REQUIRED);
        assertThat(testReportparameter.getMinlength()).isEqualTo(UPDATED_MINLENGTH);
        assertThat(testReportparameter.getMaxlength()).isEqualTo(UPDATED_MAXLENGTH);
        assertThat(testReportparameter.getValidation()).isEqualTo(UPDATED_VALIDATION);
        assertThat(testReportparameter.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReportparameter.getLastmodifiedby()).isEqualTo(UPDATED_LASTMODIFIEDBY);
        assertThat(testReportparameter.getLastmodifieddatetime()).isEqualTo(UPDATED_LASTMODIFIEDDATETIME);
        assertThat(testReportparameter.getDomain()).isEqualTo(UPDATED_DOMAIN);

        // Validate the Reportparameter in Elasticsearch
        Reportparameter reportparameterEs = reportparameterSearchRepository.findOne(testReportparameter.getId());
        assertThat(testReportparameter.getLastmodifieddatetime()).isEqualTo(testReportparameter.getLastmodifieddatetime());
        assertThat(reportparameterEs).isEqualToIgnoringGivenFields(testReportparameter, "lastmodifieddatetime");
    }

    @Test
    @Transactional
    public void updateNonExistingReportparameter() throws Exception {
        int databaseSizeBeforeUpdate = reportparameterRepository.findAll().size();

        // Create the Reportparameter
        ReportparameterDTO reportparameterDTO = reportparameterMapper.toDto(reportparameter);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReportparameterMockMvc.perform(put("/api/reportparameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportparameterDTO)))
            .andExpect(status().isCreated());

        // Validate the Reportparameter in the database
        List<Reportparameter> reportparameterList = reportparameterRepository.findAll();
        assertThat(reportparameterList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReportparameter() throws Exception {
        // Initialize the database
        reportparameterRepository.saveAndFlush(reportparameter);
        reportparameterSearchRepository.save(reportparameter);
        int databaseSizeBeforeDelete = reportparameterRepository.findAll().size();

        // Get the reportparameter
        restReportparameterMockMvc.perform(delete("/api/reportparameters/{id}", reportparameter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean reportparameterExistsInEs = reportparameterSearchRepository.exists(reportparameter.getId());
        assertThat(reportparameterExistsInEs).isFalse();

        // Validate the database is empty
        List<Reportparameter> reportparameterList = reportparameterRepository.findAll();
        assertThat(reportparameterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReportparameter() throws Exception {
        // Initialize the database
        reportparameterRepository.saveAndFlush(reportparameter);
        reportparameterSearchRepository.save(reportparameter);

        // Search the reportparameter
        restReportparameterMockMvc.perform(get("/api/_search/reportparameters?query=id:" + reportparameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportparameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].instructions").value(hasItem(DEFAULT_INSTRUCTIONS.toString())))
            .andExpect(jsonPath("$.[*].datatype").value(hasItem(DEFAULT_DATATYPE.toString())))
            .andExpect(jsonPath("$.[*].required").value(hasItem(DEFAULT_REQUIRED.toString())))
            .andExpect(jsonPath("$.[*].minlength").value(hasItem(DEFAULT_MINLENGTH.toString())))
            .andExpect(jsonPath("$.[*].maxlength").value(hasItem(DEFAULT_MAXLENGTH.toString())))
            .andExpect(jsonPath("$.[*].validation").value(hasItem(DEFAULT_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
            .andExpect(jsonPath("$.[*].lastmodifieddatetime").value(hasItem(sameInstant(DEFAULT_LASTMODIFIEDDATETIME))))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reportparameter.class);
        Reportparameter reportparameter1 = new Reportparameter();
        reportparameter1.setId(1L);
        Reportparameter reportparameter2 = new Reportparameter();
        reportparameter2.setId(reportparameter1.getId());
        assertThat(reportparameter1).isEqualTo(reportparameter2);
        reportparameter2.setId(2L);
        assertThat(reportparameter1).isNotEqualTo(reportparameter2);
        reportparameter1.setId(null);
        assertThat(reportparameter1).isNotEqualTo(reportparameter2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportparameterDTO.class);
        ReportparameterDTO reportparameterDTO1 = new ReportparameterDTO();
        reportparameterDTO1.setId(1L);
        ReportparameterDTO reportparameterDTO2 = new ReportparameterDTO();
        assertThat(reportparameterDTO1).isNotEqualTo(reportparameterDTO2);
        reportparameterDTO2.setId(reportparameterDTO1.getId());
        assertThat(reportparameterDTO1).isEqualTo(reportparameterDTO2);
        reportparameterDTO2.setId(2L);
        assertThat(reportparameterDTO1).isNotEqualTo(reportparameterDTO2);
        reportparameterDTO1.setId(null);
        assertThat(reportparameterDTO1).isNotEqualTo(reportparameterDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(reportparameterMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(reportparameterMapper.fromId(null)).isNull();
    }
}
