package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Ve;
import com.mycompany.myapp.repository.VeRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Ve}.
 */
@RestController
@RequestMapping("/api/ves")
@Transactional
public class VeResource {

    private static final Logger log = LoggerFactory.getLogger(VeResource.class);

    private static final String ENTITY_NAME = "ve";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VeRepository veRepository;

    public VeResource(VeRepository veRepository) {
        this.veRepository = veRepository;
    }

    /**
     * {@code POST  /ves} : Create a new ve.
     *
     * @param ve the ve to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ve, or with status {@code 400 (Bad Request)} if the ve has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Ve> createVe(@Valid @RequestBody Ve ve) throws URISyntaxException {
        log.debug("REST request to save Ve : {}", ve);
        if (ve.getId() != null) {
            throw new BadRequestAlertException("A new ve cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ve = veRepository.save(ve);
        return ResponseEntity.created(new URI("/api/ves/" + ve.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ve.getId().toString()))
            .body(ve);
    }

    /**
     * {@code PUT  /ves/:id} : Updates an existing ve.
     *
     * @param id the id of the ve to save.
     * @param ve the ve to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ve,
     * or with status {@code 400 (Bad Request)} if the ve is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ve couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ve> updateVe(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Ve ve)
        throws URISyntaxException {
        log.debug("REST request to update Ve : {}, {}", id, ve);
        if (ve.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ve.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!veRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ve = veRepository.save(ve);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ve.getId().toString()))
            .body(ve);
    }

    /**
     * {@code PATCH  /ves/:id} : Partial updates given fields of an existing ve, field will ignore if it is null
     *
     * @param id the id of the ve to save.
     * @param ve the ve to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ve,
     * or with status {@code 400 (Bad Request)} if the ve is not valid,
     * or with status {@code 404 (Not Found)} if the ve is not found,
     * or with status {@code 500 (Internal Server Error)} if the ve couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ve> partialUpdateVe(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Ve ve)
        throws URISyntaxException {
        log.debug("REST request to partial update Ve partially : {}, {}", id, ve);
        if (ve.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ve.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!veRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ve> result = veRepository
            .findById(ve.getId())
            .map(existingVe -> {
                if (ve.getSoDienThoai() != null) {
                    existingVe.setSoDienThoai(ve.getSoDienThoai());
                }
                if (ve.getEmail() != null) {
                    existingVe.setEmail(ve.getEmail());
                }
                if (ve.getGiaTien() != null) {
                    existingVe.setGiaTien(ve.getGiaTien());
                }
                if (ve.getTinhTrang() != null) {
                    existingVe.setTinhTrang(ve.getTinhTrang());
                }

                return existingVe;
            })
            .map(veRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ve.getId().toString())
        );
    }

    /**
     * {@code GET  /ves} : get all the ves.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ves in body.
     */
    @GetMapping("")
    public List<Ve> getAllVes() {
        log.debug("REST request to get all Ves");
        return veRepository.findAll();
    }

    /**
     * {@code GET  /ves/:id} : get the "id" ve.
     *
     * @param id the id of the ve to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ve, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ve> getVe(@PathVariable("id") Long id) {
        log.debug("REST request to get Ve : {}", id);
        Optional<Ve> ve = veRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ve);
    }

    /**
     * {@code DELETE  /ves/:id} : delete the "id" ve.
     *
     * @param id the id of the ve to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVe(@PathVariable("id") Long id) {
        log.debug("REST request to delete Ve : {}", id);
        veRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
