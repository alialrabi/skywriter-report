package com.skywriter.report.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.skywriter.report.domain.Report;
import com.skywriter.report.domain.Reportparameter;
import com.skywriter.report.jasper.GenerateReportFile;
import com.skywriter.report.jasper.Parameters;
import com.skywriter.report.jasper.Validation;
import com.skywriter.report.repository.ReportRepository;
import com.skywriter.report.repository.ReportparameterRepository;
import com.skywriter.report.repository.search.ReportSearchRepository;
import com.skywriter.report.web.rest.errors.BadRequestAlertException;
import com.skywriter.report.web.rest.util.HeaderUtil;
import com.skywriter.report.web.rest.util.PaginationUtil;
import com.skywriter.report.service.dto.ReportDTO;
import com.skywriter.report.service.mapper.ReportMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Report.
 */
@RestController
@RequestMapping("/api")
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    private static final String ENTITY_NAME = "report";

    private final ReportRepository reportRepository;

    private final ReportMapper reportMapper;

    private final ReportSearchRepository reportSearchRepository;
    
    private final ReportparameterRepository reportparameterRepository;
    

    @Inject
    GenerateReportFile generateReportFile; 

    public ReportResource(ReportRepository reportRepository, ReportMapper reportMapper, ReportSearchRepository reportSearchRepository,ReportparameterRepository reportparameterRepository) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
        this.reportSearchRepository = reportSearchRepository;
        this.reportparameterRepository = reportparameterRepository;
    }

    /**
     * POST  /reports : Create a new report.
     *
     * @param reportDTO the reportDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reportDTO, or with status 400 (Bad Request) if the report has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reports")
    @Timed
    public ResponseEntity<ReportDTO> createReport(@Valid @RequestBody ReportDTO reportDTO) throws URISyntaxException {
        log.debug("REST request to save Report : {}", reportDTO);
        if (reportDTO.getId() != null) {
            throw new BadRequestAlertException("A new report cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Report report = reportMapper.toEntity(reportDTO);
        report = reportRepository.save(report);
        ReportDTO result = reportMapper.toDto(report);
        reportSearchRepository.save(report);
        return ResponseEntity.created(new URI("/api/reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reports : Updates an existing report.
     *
     * @param reportDTO the reportDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reportDTO,
     * or with status 400 (Bad Request) if the reportDTO is not valid,
     * or with status 500 (Internal Server Error) if the reportDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reports")
    @Timed
    public ResponseEntity<ReportDTO> updateReport(@Valid @RequestBody ReportDTO reportDTO) throws URISyntaxException {
        log.debug("REST request to update Report : {}", reportDTO);
        if (reportDTO.getId() == null) {
            return createReport(reportDTO);
        }
        Report report = reportMapper.toEntity(reportDTO);
        report = reportRepository.save(report);
        ReportDTO result = reportMapper.toDto(report);
        reportSearchRepository.save(report);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reportDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reports : get all the reports.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reports in body
     */
    @GetMapping("/reports")
    @Timed
    public ResponseEntity<List<ReportDTO>> getAllReports(Pageable pageable) {
        log.debug("REST request to get a page of Reports");
        Page<Report> page = reportRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reports");
        return new ResponseEntity<>(reportMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /reports/:id : get the "id" report.
     *
     * @param id the id of the reportDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reportDTO, or with status 404 (Not Found)
     */
    @GetMapping("/reports/{id}")
    @Timed
    public ResponseEntity<ReportDTO> getReport(@PathVariable Long id) {
        log.debug("REST request to get Report : {}", id);
        Report report = reportRepository.findOne(id);
        ReportDTO reportDTO = reportMapper.toDto(report);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reportDTO));
    }

    /**
     * DELETE  /reports/:id : delete the "id" report.
     *
     * @param id the id of the reportDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        log.debug("REST request to delete Report : {}", id);
        reportRepository.delete(id);
        reportSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reports?query=:query : search for the report corresponding
     * to the query.
     *
     * @param query the query of the report search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/reports")
    @Timed
    public ResponseEntity<List<ReportDTO>> searchReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Reports for query {}", query);
        Page<Report> page = reportSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/reports");
        return new ResponseEntity<>(reportMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }
 
    /**
     * 
     * @param reportId
     * @param parameters
     * @param response
     * @throws Exception
     */
    @PostMapping("/generateReport")
    @Timed
    public void generateReport(MultipartHttpServletRequest request) throws Exception {
    	 String reportId=request.getParameter("reportId");
    	 String parameters=request.getParameter("parameters");
         Report report = reportRepository.findOne(Long.parseLong(reportId));
         generateReportFile.generateReport(report, parameters);

         }
    
    /**
     * 
     * @param reportId
     * @param pageable
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/parameterList/{reportId}")
    @Timed
    public  ResponseEntity<List<Parameters>> parameterList(@PathVariable("reportId") long reportId,
        		           Pageable pageable) throws Exception {
    	List<Parameters> list=new ArrayList<Parameters>();
    	Page<Reportparameter> reportparameters=reportparameterRepository.findByReportId(reportId,pageable);
    	HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(reportparameters, "/api/reports");

    	for(Reportparameter reportparameter:reportparameters.getContent()){
        	System.out.println(reportparameter.getValidation());
    		Parameters parameters=new Parameters();
    		 parameters.setKey(reportparameter.getLabel());
    		 parameters.setValue("");
    		 parameters.setDataType(reportparameter.getDatatype());
    		  Validation validation=new Validation();
    	    	validation.setRequired(reportparameter.getRequired());
    	    	validation.setMinlength(reportparameter.getMinlength());
    	    	validation.setMaxlength(reportparameter.getMaxlength());
    		 parameters.setValidation(validation);
    		list.add(parameters);
    	}
        return new ResponseEntity<>(list, headers, HttpStatus.OK);
      }
}