package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BapNuoc;
import com.mycompany.myapp.repository.BapNuocRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BapNuoc}.
 */
@RestController
@RequestMapping("/api/bap-nuocs")
@Transactional
public class BapNuocResource {

    private static final Logger log = LoggerFactory.getLogger(BapNuocResource.class);

    private static final String ENTITY_NAME = "bapNuoc";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BapNuocRepository bapNuocRepository;

    public BapNuocResource(BapNuocRepository bapNuocRepository) {
        this.bapNuocRepository = bapNuocRepository;
    }

    /**
     * {@code POST  /bap-nuocs} : Create a new bapNuoc.
     *
     * @param bapNuoc the bapNuoc to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bapNuoc, or with status {@code 400 (Bad Request)} if the bapNuoc has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BapNuoc> createBapNuoc(@Valid @RequestBody BapNuoc bapNuoc) throws URISyntaxException {
        log.debug("REST request to save BapNuoc : {}", bapNuoc);
        if (bapNuoc.getId() != null) {
            throw new BadRequestAlertException("A new bapNuoc cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bapNuoc = bapNuocRepository.save(bapNuoc);
        return ResponseEntity.created(new URI("/api/bap-nuocs/" + bapNuoc.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bapNuoc.getId().toString()))
            .body(bapNuoc);
    }

    /**
     * {@code PUT  /bap-nuocs/:id} : Updates an existing bapNuoc.
     *
     * @param id the id of the bapNuoc to save.
     * @param bapNuoc the bapNuoc to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bapNuoc,
     * or with status {@code 400 (Bad Request)} if the bapNuoc is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bapNuoc couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BapNuoc> updateBapNuoc(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BapNuoc bapNuoc
    ) throws URISyntaxException {
        log.debug("REST request to update BapNuoc : {}, {}", id, bapNuoc);
        if (bapNuoc.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bapNuoc.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bapNuocRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bapNuoc = bapNuocRepository.save(bapNuoc);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bapNuoc.getId().toString()))
            .body(bapNuoc);
    }

    /**
     * {@code PATCH  /bap-nuocs/:id} : Partial updates given fields of an existing bapNuoc, field will ignore if it is null
     *
     * @param id the id of the bapNuoc to save.
     * @param bapNuoc the bapNuoc to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bapNuoc,
     * or with status {@code 400 (Bad Request)} if the bapNuoc is not valid,
     * or with status {@code 404 (Not Found)} if the bapNuoc is not found,
     * or with status {@code 500 (Internal Server Error)} if the bapNuoc couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BapNuoc> partialUpdateBapNuoc(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BapNuoc bapNuoc
    ) throws URISyntaxException {
        log.debug("REST request to partial update BapNuoc partially : {}, {}", id, bapNuoc);
        if (bapNuoc.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bapNuoc.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bapNuocRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BapNuoc> result = bapNuocRepository
            .findById(bapNuoc.getId())
            .map(existingBapNuoc -> {
                if (bapNuoc.getTenBapNuoc() != null) {
                    existingBapNuoc.setTenBapNuoc(bapNuoc.getTenBapNuoc());
                }
                if (bapNuoc.getLogo() != null) {
                    existingBapNuoc.setLogo(bapNuoc.getLogo());
                }
                if (bapNuoc.getLogoContentType() != null) {
                    existingBapNuoc.setLogoContentType(bapNuoc.getLogoContentType());
                }
                if (bapNuoc.getGiaTien() != null) {
                    existingBapNuoc.setGiaTien(bapNuoc.getGiaTien());
                }

                return existingBapNuoc;
            })
            .map(bapNuocRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bapNuoc.getId().toString())
        );
    }

    /**
     * {@code GET  /bap-nuocs} : get all the bapNuocs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bapNuocs in body.
     */
    @GetMapping("")
    public List<BapNuoc> getAllBapNuocs() {
        log.debug("REST request to get all BapNuocs");
        return bapNuocRepository.findAll();
    }

    /**
     * {@code GET  /bap-nuocs/:id} : get the "id" bapNuoc.
     *
     * @param id the id of the bapNuoc to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bapNuoc, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BapNuoc> getBapNuoc(@PathVariable("id") Long id) {
        log.debug("REST request to get BapNuoc : {}", id);
        Optional<BapNuoc> bapNuoc = bapNuocRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bapNuoc);
    }

    /**
     * {@code DELETE  /bap-nuocs/:id} : delete the "id" bapNuoc.
     *
     * @param id the id of the bapNuoc to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBapNuoc(@PathVariable("id") Long id) {
        log.debug("REST request to delete BapNuoc : {}", id);
        bapNuocRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
