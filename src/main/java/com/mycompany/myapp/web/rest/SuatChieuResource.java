package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SuatChieu;
import com.mycompany.myapp.repository.SuatChieuRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SuatChieu}.
 */
@RestController
@RequestMapping("/api/suat-chieus")
@Transactional
public class SuatChieuResource {

    private static final Logger log = LoggerFactory.getLogger(SuatChieuResource.class);

    private static final String ENTITY_NAME = "suatChieu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SuatChieuRepository suatChieuRepository;

    public SuatChieuResource(SuatChieuRepository suatChieuRepository) {
        this.suatChieuRepository = suatChieuRepository;
    }

    /**
     * {@code POST  /suat-chieus} : Create a new suatChieu.
     *
     * @param suatChieu the suatChieu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new suatChieu, or with status {@code 400 (Bad Request)} if the suatChieu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SuatChieu> createSuatChieu(@Valid @RequestBody SuatChieu suatChieu) throws URISyntaxException {
        log.debug("REST request to save SuatChieu : {}", suatChieu);
        if (suatChieu.getId() != null) {
            throw new BadRequestAlertException("A new suatChieu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        suatChieu = suatChieuRepository.save(suatChieu);
        return ResponseEntity.created(new URI("/api/suat-chieus/" + suatChieu.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, suatChieu.getId().toString()))
            .body(suatChieu);
    }

    /**
     * {@code PUT  /suat-chieus/:id} : Updates an existing suatChieu.
     *
     * @param id the id of the suatChieu to save.
     * @param suatChieu the suatChieu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suatChieu,
     * or with status {@code 400 (Bad Request)} if the suatChieu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the suatChieu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SuatChieu> updateSuatChieu(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SuatChieu suatChieu
    ) throws URISyntaxException {
        log.debug("REST request to update SuatChieu : {}, {}", id, suatChieu);
        if (suatChieu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, suatChieu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!suatChieuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        suatChieu = suatChieuRepository.save(suatChieu);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, suatChieu.getId().toString()))
            .body(suatChieu);
    }

    /**
     * {@code PATCH  /suat-chieus/:id} : Partial updates given fields of an existing suatChieu, field will ignore if it is null
     *
     * @param id the id of the suatChieu to save.
     * @param suatChieu the suatChieu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suatChieu,
     * or with status {@code 400 (Bad Request)} if the suatChieu is not valid,
     * or with status {@code 404 (Not Found)} if the suatChieu is not found,
     * or with status {@code 500 (Internal Server Error)} if the suatChieu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SuatChieu> partialUpdateSuatChieu(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SuatChieu suatChieu
    ) throws URISyntaxException {
        log.debug("REST request to partial update SuatChieu partially : {}, {}", id, suatChieu);
        if (suatChieu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, suatChieu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!suatChieuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SuatChieu> result = suatChieuRepository
            .findById(suatChieu.getId())
            .map(existingSuatChieu -> {
                if (suatChieu.getNgayChieu() != null) {
                    existingSuatChieu.setNgayChieu(suatChieu.getNgayChieu());
                }
                if (suatChieu.getGioChieu() != null) {
                    existingSuatChieu.setGioChieu(suatChieu.getGioChieu());
                }

                return existingSuatChieu;
            })
            .map(suatChieuRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, suatChieu.getId().toString())
        );
    }

    /**
     * {@code GET  /suat-chieus} : get all the suatChieus.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of suatChieus in body.
     */
    @GetMapping("")
    public List<SuatChieu> getAllSuatChieus() {
        log.debug("REST request to get all SuatChieus");
        return suatChieuRepository.findAll();
    }

    /**
     * {@code GET  /suat-chieus/:id} : get the "id" suatChieu.
     *
     * @param id the id of the suatChieu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the suatChieu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuatChieu> getSuatChieu(@PathVariable("id") Long id) {
        log.debug("REST request to get SuatChieu : {}", id);
        Optional<SuatChieu> suatChieu = suatChieuRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(suatChieu);
    }

    /**
     * {@code DELETE  /suat-chieus/:id} : delete the "id" suatChieu.
     *
     * @param id the id of the suatChieu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSuatChieu(@PathVariable("id") Long id) {
        log.debug("REST request to delete SuatChieu : {}", id);
        suatChieuRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
