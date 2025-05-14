package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CumRap;
import com.mycompany.myapp.repository.CumRapRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.CumRap}.
 */
@RestController
@RequestMapping("/api/cum-raps")
@Transactional
public class CumRapResource {

    private static final Logger log = LoggerFactory.getLogger(CumRapResource.class);

    private static final String ENTITY_NAME = "cumRap";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CumRapRepository cumRapRepository;

    public CumRapResource(CumRapRepository cumRapRepository) {
        this.cumRapRepository = cumRapRepository;
    }

    /**
     * {@code POST  /cum-raps} : Create a new cumRap.
     *
     * @param cumRap the cumRap to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cumRap, or with status {@code 400 (Bad Request)} if the cumRap has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CumRap> createCumRap(@Valid @RequestBody CumRap cumRap) throws URISyntaxException {
        log.debug("REST request to save CumRap : {}", cumRap);
        if (cumRap.getId() != null) {
            throw new BadRequestAlertException("A new cumRap cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cumRap = cumRapRepository.save(cumRap);
        return ResponseEntity.created(new URI("/api/cum-raps/" + cumRap.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cumRap.getId().toString()))
            .body(cumRap);
    }

    /**
     * {@code PUT  /cum-raps/:id} : Updates an existing cumRap.
     *
     * @param id the id of the cumRap to save.
     * @param cumRap the cumRap to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cumRap,
     * or with status {@code 400 (Bad Request)} if the cumRap is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cumRap couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CumRap> updateCumRap(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CumRap cumRap
    ) throws URISyntaxException {
        log.debug("REST request to update CumRap : {}, {}", id, cumRap);
        if (cumRap.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cumRap.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cumRapRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cumRap = cumRapRepository.save(cumRap);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cumRap.getId().toString()))
            .body(cumRap);
    }

    /**
     * {@code PATCH  /cum-raps/:id} : Partial updates given fields of an existing cumRap, field will ignore if it is null
     *
     * @param id the id of the cumRap to save.
     * @param cumRap the cumRap to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cumRap,
     * or with status {@code 400 (Bad Request)} if the cumRap is not valid,
     * or with status {@code 404 (Not Found)} if the cumRap is not found,
     * or with status {@code 500 (Internal Server Error)} if the cumRap couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CumRap> partialUpdateCumRap(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CumRap cumRap
    ) throws URISyntaxException {
        log.debug("REST request to partial update CumRap partially : {}, {}", id, cumRap);
        if (cumRap.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cumRap.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cumRapRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CumRap> result = cumRapRepository
            .findById(cumRap.getId())
            .map(existingCumRap -> {
                if (cumRap.getTenCumRap() != null) {
                    existingCumRap.setTenCumRap(cumRap.getTenCumRap());
                }
                if (cumRap.getLogo() != null) {
                    existingCumRap.setLogo(cumRap.getLogo());
                }
                if (cumRap.getLogoContentType() != null) {
                    existingCumRap.setLogoContentType(cumRap.getLogoContentType());
                }

                return existingCumRap;
            })
            .map(cumRapRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cumRap.getId().toString())
        );
    }

    /**
     * {@code GET  /cum-raps} : get all the cumRaps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cumRaps in body.
     */
    @GetMapping("")
    public List<CumRap> getAllCumRaps() {
        log.debug("REST request to get all CumRaps");
        return cumRapRepository.findAll();
    }

    /**
     * {@code GET  /cum-raps/:id} : get the "id" cumRap.
     *
     * @param id the id of the cumRap to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cumRap, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CumRap> getCumRap(@PathVariable("id") Long id) {
        log.debug("REST request to get CumRap : {}", id);
        Optional<CumRap> cumRap = cumRapRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cumRap);
    }

    /**
     * {@code DELETE  /cum-raps/:id} : delete the "id" cumRap.
     *
     * @param id the id of the cumRap to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCumRap(@PathVariable("id") Long id) {
        log.debug("REST request to delete CumRap : {}", id);
        cumRapRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
