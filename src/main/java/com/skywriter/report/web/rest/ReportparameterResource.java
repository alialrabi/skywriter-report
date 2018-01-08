package com.skywriter.report.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.skywriter.report.domain.Reportparameter;

import com.skywriter.report.repository.ReportparameterRepository;
import com.skywriter.report.repository.search.ReportparameterSearchRepository;
import com.skywriter.report.web.rest.errors.BadRequestAlertException;
import com.skywriter.report.web.rest.util.HeaderUtil;
import com.skywriter.report.web.rest.util.PaginationUtil;
import com.skywriter.report.service.dto.ReportparameterDTO;
import com.skywriter.report.service.mapper.ReportparameterMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Reportparameter.
 */
@RestController
@RequestMapping("/api")
public class ReportparameterResource {

    private final Logger log = LoggerFactory.getLogger(ReportparameterResource.class);

    private static final String ENTITY_NAME = "reportparameter";

    private final ReportparameterRepository reportparameterRepository;

    private final ReportparameterMapper reportparameterMapper;

    private final ReportparameterSearchRepository reportparameterSearchRepository;

    public ReportparameterResource(ReportparameterRepository reportparameterRepository, ReportparameterMapper reportparameterMapper, ReportparameterSearchRepository reportparameterSearchRepository) {
        this.reportparameterRepository = reportparameterRepository;
        this.reportparameterMapper = reportparameterMapper;
        this.reportparameterSearchRepository = reportparameterSearchRepository;
    }

    /**
     * POST  /reportparameters : Create a new reportparameter.
     *
     * @param reportparameterDTO the reportparameterDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reportparameterDTO, or with status 400 (Bad Request) if the reportparameter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reportparameters")
    @Timed
    public ResponseEntity<ReportparameterDTO> createReportparameter(@Valid @RequestBody ReportparameterDTO reportparameterDTO) throws URISyntaxException {
        log.debug("REST request to save Reportparameter : {}", reportparameterDTO);
        if (reportparameterDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportparameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Reportparameter reportparameter = reportparameterMapper.toEntity(reportparameterDTO);
        reportparameter = reportparameterRepository.save(reportparameter);
        ReportparameterDTO result = reportparameterMapper.toDto(reportparameter);
        reportparameterSearchRepository.save(reportparameter);
        return ResponseEntity.created(new URI("/api/reportparameters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reportparameters : Updates an existing reportparameter.
     *
     * @param reportparameterDTO the reportparameterDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reportparameterDTO,
     * or with status 400 (Bad Request) if the reportparameterDTO is not valid,
     * or with status 500 (Internal Server Error) if the reportparameterDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reportparameters")
    @Timed
    public ResponseEntity<ReportparameterDTO> updateReportparameter(@Valid @RequestBody ReportparameterDTO reportparameterDTO) throws URISyntaxException {
        log.debug("REST request to update Reportparameter : {}", reportparameterDTO);
        if (reportparameterDTO.getId() == null) {
            return createReportparameter(reportparameterDTO);
        }
        Reportparameter reportparameter = reportparameterMapper.toEntity(reportparameterDTO);
        reportparameter = reportparameterRepository.save(reportparameter);
        ReportparameterDTO result = reportparameterMapper.toDto(reportparameter);
        reportparameterSearchRepository.save(reportparameter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reportparameterDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reportparameters : get all the reportparameters.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reportparameters in body
     */
    @GetMapping("/reportparameters")
    @Timed
    public ResponseEntity<List<ReportparameterDTO>> getAllReportparameters(Pageable pageable) {
        log.debug("REST request to get a page of Reportparameters");
        Page<Reportparameter> page = reportparameterRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reportparameters");
        return new ResponseEntity<>(reportparameterMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /reportparameters/:id : get the "id" reportparameter.
     *
     * @param id the id of the reportparameterDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reportparameterDTO, or with status 404 (Not Found)
     */
    @GetMapping("/reportparameters/{id}")
    @Timed
    public ResponseEntity<ReportparameterDTO> getReportparameter(@PathVariable Long id) {
        log.debug("REST request to get Reportparameter : {}", id);
        Reportparameter reportparameter = reportparameterRepository.findOne(id);
        ReportparameterDTO reportparameterDTO = reportparameterMapper.toDto(reportparameter);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reportparameterDTO));
    }

    /**
     * DELETE  /reportparameters/:id : delete the "id" reportparameter.
     *
     * @param id the id of the reportparameterDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reportparameters/{id}")
    @Timed
    public ResponseEntity<Void> deleteReportparameter(@PathVariable Long id) {
        log.debug("REST request to delete Reportparameter : {}", id);
        reportparameterRepository.delete(id);
        reportparameterSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reportparameters?query=:query : search for the reportparameter corresponding
     * to the query.
     *
     * @param query the query of the reportparameter search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/reportparameters")
    @Timed
    public ResponseEntity<List<ReportparameterDTO>> searchReportparameters(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Reportparameters for query {}", query);
        Page<Reportparameter> page = reportparameterSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/reportparameters");
        return new ResponseEntity<>(reportparameterMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

}
