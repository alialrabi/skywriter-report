package com.skywriter.report.web.rest;

import com.skywriter.report.ReportApp;

import com.skywriter.report.config.SecurityBeanOverrideConfiguration;

import com.skywriter.report.domain.Reportclass;
import com.skywriter.report.repository.ReportclassRepository;
import com.skywriter.report.repository.search.ReportclassSearchRepository;
import com.skywriter.report.service.dto.ReportclassDTO;
import com.skywriter.report.service.mapper.ReportclassMapper;
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
 * Test class for the ReportclassResource REST controller.
 *
 * @see ReportclassResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ReportApp.class, SecurityBeanOverrideConfiguration.class})
public class ReportclassResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_LASTMODIFIEDBY = "AAAAAAAAAA";
    private static final String UPDATED_LASTMODIFIEDBY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_LASTMODIFIEDDATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LASTMODIFIEDDATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN = "BBBBBBBBBB";

    @Autowired
    private ReportclassRepository reportclassRepository;

    @Autowired
    private ReportclassMapper reportclassMapper;

    @Autowired
    private ReportclassSearchRepository reportclassSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReportclassMockMvc;

    private Reportclass reportclass;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReportclassResource reportclassResource = new ReportclassResource(reportclassRepository, reportclassMapper, reportclassSearchRepository);
        this.restReportclassMockMvc = MockMvcBuilders.standaloneSetup(reportclassResource)
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
    public static Reportclass createEntity(EntityManager em) {
        Reportclass reportclass = new Reportclass()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS)
            .lastmodifiedby(DEFAULT_LASTMODIFIEDBY)
            .lastmodifieddatetime(DEFAULT_LASTMODIFIEDDATETIME)
            .domain(DEFAULT_DOMAIN);
        return reportclass;
    }

    @Before
    public void initTest() {
        reportclassSearchRepository.deleteAll();
        reportclass = createEntity(em);
    }

    @Test
    @Transactional
    public void createReportclass() throws Exception {
        int databaseSizeBeforeCreate = reportclassRepository.findAll().size();

        // Create the Reportclass
        ReportclassDTO reportclassDTO = reportclassMapper.toDto(reportclass);
        restReportclassMockMvc.perform(post("/api/reportclasses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportclassDTO)))
            .andExpect(status().isCreated());

        // Validate the Reportclass in the database
        List<Reportclass> reportclassList = reportclassRepository.findAll();
        assertThat(reportclassList).hasSize(databaseSizeBeforeCreate + 1);
        Reportclass testReportclass = reportclassList.get(reportclassList.size() - 1);
        assertThat(testReportclass.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testReportclass.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReportclass.getLastmodifiedby()).isEqualTo(DEFAULT_LASTMODIFIEDBY);
        assertThat(testReportclass.getLastmodifieddatetime()).isEqualTo(DEFAULT_LASTMODIFIEDDATETIME);
        assertThat(testReportclass.getDomain()).isEqualTo(DEFAULT_DOMAIN);

        // Validate the Reportclass in Elasticsearch
        Reportclass reportclassEs = reportclassSearchRepository.findOne(testReportclass.getId());
        assertThat(testReportclass.getLastmodifieddatetime()).isEqualTo(testReportclass.getLastmodifieddatetime());
        assertThat(reportclassEs).isEqualToIgnoringGivenFields(testReportclass, "lastmodifieddatetime");
    }

    @Test
    @Transactional
    public void createReportclassWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reportclassRepository.findAll().size();

        // Create the Reportclass with an existing ID
        reportclass.setId(1L);
        ReportclassDTO reportclassDTO = reportclassMapper.toDto(reportclass);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportclassMockMvc.perform(post("/api/reportclasses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportclassDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reportclass in the database
        List<Reportclass> reportclassList = reportclassRepository.findAll();
        assertThat(reportclassList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportclassRepository.findAll().size();
        // set the field null
        reportclass.setName(null);

        // Create the Reportclass, which fails.
        ReportclassDTO reportclassDTO = reportclassMapper.toDto(reportclass);

        restReportclassMockMvc.perform(post("/api/reportclasses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportclassDTO)))
            .andExpect(status().isBadRequest());

        List<Reportclass> reportclassList = reportclassRepository.findAll();
        assertThat(reportclassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReportclasses() throws Exception {
        // Initialize the database
        reportclassRepository.saveAndFlush(reportclass);

        // Get all the reportclassList
        restReportclassMockMvc.perform(get("/api/reportclasses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportclass.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
            .andExpect(jsonPath("$.[*].lastmodifieddatetime").value(hasItem(sameInstant(DEFAULT_LASTMODIFIEDDATETIME))))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())));
    }

    @Test
    @Transactional
    public void getReportclass() throws Exception {
        // Initialize the database
        reportclassRepository.saveAndFlush(reportclass);

        // Get the reportclass
        restReportclassMockMvc.perform(get("/api/reportclasses/{id}", reportclass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reportclass.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastmodifiedby").value(DEFAULT_LASTMODIFIEDBY.toString()))
            .andExpect(jsonPath("$.lastmodifieddatetime").value(sameInstant(DEFAULT_LASTMODIFIEDDATETIME)))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReportclass() throws Exception {
        // Get the reportclass
        restReportclassMockMvc.perform(get("/api/reportclasses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReportclass() throws Exception {
        // Initialize the database
        reportclassRepository.saveAndFlush(reportclass);
        reportclassSearchRepository.save(reportclass);
        int databaseSizeBeforeUpdate = reportclassRepository.findAll().size();

        // Update the reportclass
        Reportclass updatedReportclass = reportclassRepository.findOne(reportclass.getId());
        // Disconnect from session so that the updates on updatedReportclass are not directly saved in db
        em.detach(updatedReportclass);
        updatedReportclass
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .lastmodifiedby(UPDATED_LASTMODIFIEDBY)
            .lastmodifieddatetime(UPDATED_LASTMODIFIEDDATETIME)
            .domain(UPDATED_DOMAIN);
        ReportclassDTO reportclassDTO = reportclassMapper.toDto(updatedReportclass);

        restReportclassMockMvc.perform(put("/api/reportclasses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportclassDTO)))
            .andExpect(status().isOk());

        // Validate the Reportclass in the database
        List<Reportclass> reportclassList = reportclassRepository.findAll();
        assertThat(reportclassList).hasSize(databaseSizeBeforeUpdate);
        Reportclass testReportclass = reportclassList.get(reportclassList.size() - 1);
        assertThat(testReportclass.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReportclass.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReportclass.getLastmodifiedby()).isEqualTo(UPDATED_LASTMODIFIEDBY);
        assertThat(testReportclass.getLastmodifieddatetime()).isEqualTo(UPDATED_LASTMODIFIEDDATETIME);
        assertThat(testReportclass.getDomain()).isEqualTo(UPDATED_DOMAIN);

        // Validate the Reportclass in Elasticsearch
        Reportclass reportclassEs = reportclassSearchRepository.findOne(testReportclass.getId());
        assertThat(testReportclass.getLastmodifieddatetime()).isEqualTo(testReportclass.getLastmodifieddatetime());
        assertThat(reportclassEs).isEqualToIgnoringGivenFields(testReportclass, "lastmodifieddatetime");
    }

    @Test
    @Transactional
    public void updateNonExistingReportclass() throws Exception {
        int databaseSizeBeforeUpdate = reportclassRepository.findAll().size();

        // Create the Reportclass
        ReportclassDTO reportclassDTO = reportclassMapper.toDto(reportclass);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReportclassMockMvc.perform(put("/api/reportclasses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportclassDTO)))
            .andExpect(status().isCreated());

        // Validate the Reportclass in the database
        List<Reportclass> reportclassList = reportclassRepository.findAll();
        assertThat(reportclassList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReportclass() throws Exception {
        // Initialize the database
        reportclassRepository.saveAndFlush(reportclass);
        reportclassSearchRepository.save(reportclass);
        int databaseSizeBeforeDelete = reportclassRepository.findAll().size();

        // Get the reportclass
        restReportclassMockMvc.perform(delete("/api/reportclasses/{id}", reportclass.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean reportclassExistsInEs = reportclassSearchRepository.exists(reportclass.getId());
        assertThat(reportclassExistsInEs).isFalse();

        // Validate the database is empty
        List<Reportclass> reportclassList = reportclassRepository.findAll();
        assertThat(reportclassList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReportclass() throws Exception {
        // Initialize the database
        reportclassRepository.saveAndFlush(reportclass);
        reportclassSearchRepository.save(reportclass);

        // Search the reportclass
        restReportclassMockMvc.perform(get("/api/_search/reportclasses?query=id:" + reportclass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportclass.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
            .andExpect(jsonPath("$.[*].lastmodifieddatetime").value(hasItem(sameInstant(DEFAULT_LASTMODIFIEDDATETIME))))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reportclass.class);
        Reportclass reportclass1 = new Reportclass();
        reportclass1.setId(1L);
        Reportclass reportclass2 = new Reportclass();
        reportclass2.setId(reportclass1.getId());
        assertThat(reportclass1).isEqualTo(reportclass2);
        reportclass2.setId(2L);
        assertThat(reportclass1).isNotEqualTo(reportclass2);
        reportclass1.setId(null);
        assertThat(reportclass1).isNotEqualTo(reportclass2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportclassDTO.class);
        ReportclassDTO reportclassDTO1 = new ReportclassDTO();
        reportclassDTO1.setId(1L);
        ReportclassDTO reportclassDTO2 = new ReportclassDTO();
        assertThat(reportclassDTO1).isNotEqualTo(reportclassDTO2);
        reportclassDTO2.setId(reportclassDTO1.getId());
        assertThat(reportclassDTO1).isEqualTo(reportclassDTO2);
        reportclassDTO2.setId(2L);
        assertThat(reportclassDTO1).isNotEqualTo(reportclassDTO2);
        reportclassDTO1.setId(null);
        assertThat(reportclassDTO1).isNotEqualTo(reportclassDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(reportclassMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(reportclassMapper.fromId(null)).isNull();
    }
}
