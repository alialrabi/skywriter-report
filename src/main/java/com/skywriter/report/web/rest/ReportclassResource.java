package com.skywriter.report.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.skywriter.report.domain.Reportclass;

import com.skywriter.report.repository.ReportclassRepository;
import com.skywriter.report.repository.search.ReportclassSearchRepository;
import com.skywriter.report.web.rest.errors.BadRequestAlertException;
import com.skywriter.report.web.rest.util.HeaderUtil;
import com.skywriter.report.web.rest.util.PaginationUtil;
import com.skywriter.report.service.dto.ReportclassDTO;
import com.skywriter.report.service.mapper.ReportclassMapper;
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
 * REST controller for managing Reportclass.
 */
@RestController
@RequestMapping("/api")
public class ReportclassResource {

    private final Logger log = LoggerFactory.getLogger(ReportclassResource.class);

    private static final String ENTITY_NAME = "reportclass";

    private final ReportclassRepository reportclassRepository;

    private final ReportclassMapper reportclassMapper;

    private final ReportclassSearchRepository reportclassSearchRepository;

    public ReportclassResource(ReportclassRepository reportclassRepository, ReportclassMapper reportclassMapper, ReportclassSearchRepository reportclassSearchRepository) {
        this.reportclassRepository = reportclassRepository;
        this.reportclassMapper = reportclassMapper;
        this.reportclassSearchRepository = reportclassSearchRepository;
    }

    /**
     * POST  /reportclasses : Create a new reportclass.
     *
     * @param reportclassDTO the reportclassDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reportclassDTO, or with status 400 (Bad Request) if the reportclass has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reportclasses")
    @Timed
    public ResponseEntity<ReportclassDTO> createReportclass(@Valid @RequestBody ReportclassDTO reportclassDTO) throws URISyntaxException {
        log.debug("REST request to save Reportclass : {}", reportclassDTO);
        if (reportclassDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportclass cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Reportclass reportclass = reportclassMapper.toEntity(reportclassDTO);
        reportclass = reportclassRepository.save(reportclass);
        ReportclassDTO result = reportclassMapper.toDto(reportclass);
        reportclassSearchRepository.save(reportclass);
        return ResponseEntity.created(new URI("/api/reportclasses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reportclasses : Updates an existing reportclass.
     *
     * @param reportclassDTO the reportclassDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reportclassDTO,
     * or with status 400 (Bad Request) if the reportclassDTO is not valid,
     * or with status 500 (Internal Server Error) if the reportclassDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reportclasses")
    @Timed
    public ResponseEntity<ReportclassDTO> updateReportclass(@Valid @RequestBody ReportclassDTO reportclassDTO) throws URISyntaxException {
        log.debug("REST request to update Reportclass : {}", reportclassDTO);
        if (reportclassDTO.getId() == null) {
            return createReportclass(reportclassDTO);
        }
        Reportclass reportclass = reportclassMapper.toEntity(reportclassDTO);
        reportclass = reportclassRepository.save(reportclass);
        ReportclassDTO result = reportclassMapper.toDto(reportclass);
        reportclassSearchRepository.save(reportclass);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reportclassDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reportclasses : get all the reportclasses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reportclasses in body
     */
    @GetMapping("/reportclasses")
    @Timed
    public ResponseEntity<List<ReportclassDTO>> getAllReportclasses(Pageable pageable) {
        log.debug("REST request to get a page of Reportclasses");
        Page<Reportclass> page = reportclassRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reportclasses");
        return new ResponseEntity<>(reportclassMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /reportclasses/:id : get the "id" reportclass.
     *
     * @param id the id of the reportclassDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reportclassDTO, or with status 404 (Not Found)
     */
    @GetMapping("/reportclasses/{id}")
    @Timed
    public ResponseEntity<ReportclassDTO> getReportclass(@PathVariable Long id) {
        log.debug("REST request to get Reportclass : {}", id);
        Reportclass reportclass = reportclassRepository.findOne(id);
        ReportclassDTO reportclassDTO = reportclassMapper.toDto(reportclass);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reportclassDTO));
    }

    /**
     * DELETE  /reportclasses/:id : delete the "id" reportclass.
     *
     * @param id the id of the reportclassDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reportclasses/{id}")
    @Timed
    public ResponseEntity<Void> deleteReportclass(@PathVariable Long id) {
        log.debug("REST request to delete Reportclass : {}", id);
        reportclassRepository.delete(id);
        reportclassSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reportclasses?query=:query : search for the reportclass corresponding
     * to the query.
     *
     * @param query the query of the reportclass search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/reportclasses")
    @Timed
    public ResponseEntity<List<ReportclassDTO>> searchReportclasses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Reportclasses for query {}", query);
        Page<Reportclass> page = reportclassSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/reportclasses");
        return new ResponseEntity<>(reportclassMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

}
