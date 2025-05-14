package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Phong;
import com.mycompany.myapp.repository.PhongRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Phong}.
 */
@RestController
@RequestMapping("/api/phongs")
@Transactional
public class PhongResource {

    private static final Logger log = LoggerFactory.getLogger(PhongResource.class);

    private static final String ENTITY_NAME = "phong";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhongRepository phongRepository;

    public PhongResource(PhongRepository phongRepository) {
        this.phongRepository = phongRepository;
    }

    /**
     * {@code POST  /phongs} : Create a new phong.
     *
     * @param phong the phong to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new phong, or with status {@code 400 (Bad Request)} if the phong has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Phong> createPhong(@Valid @RequestBody Phong phong) throws URISyntaxException {
        log.debug("REST request to save Phong : {}", phong);
        if (phong.getId() != null) {
            throw new BadRequestAlertException("A new phong cannot already have an ID", ENTITY_NAME, "idexists");
        }
        phong = phongRepository.save(phong);
        return ResponseEntity.created(new URI("/api/phongs/" + phong.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, phong.getId().toString()))
            .body(phong);
    }

    /**
     * {@code PUT  /phongs/:id} : Updates an existing phong.
     *
     * @param id the id of the phong to save.
     * @param phong the phong to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phong,
     * or with status {@code 400 (Bad Request)} if the phong is not valid,
     * or with status {@code 500 (Internal Server Error)} if the phong couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Phong> updatePhong(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Phong phong)
        throws URISyntaxException {
        log.debug("REST request to update Phong : {}, {}", id, phong);
        if (phong.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phong.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phongRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        phong = phongRepository.save(phong);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phong.getId().toString()))
            .body(phong);
    }

    /**
     * {@code PATCH  /phongs/:id} : Partial updates given fields of an existing phong, field will ignore if it is null
     *
     * @param id the id of the phong to save.
     * @param phong the phong to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phong,
     * or with status {@code 400 (Bad Request)} if the phong is not valid,
     * or with status {@code 404 (Not Found)} if the phong is not found,
     * or with status {@code 500 (Internal Server Error)} if the phong couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Phong> partialUpdatePhong(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Phong phong
    ) throws URISyntaxException {
        log.debug("REST request to partial update Phong partially : {}, {}", id, phong);
        if (phong.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phong.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phongRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Phong> result = phongRepository
            .findById(phong.getId())
            .map(existingPhong -> {
                if (phong.getTenPhong() != null) {
                    existingPhong.setTenPhong(phong.getTenPhong());
                }

                return existingPhong;
            })
            .map(phongRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phong.getId().toString())
        );
    }

    /**
     * {@code GET  /phongs} : get all the phongs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of phongs in body.
     */
    @GetMapping("")
    public List<Phong> getAllPhongs() {
        log.debug("REST request to get all Phongs");
        return phongRepository.findAll();
    }

    /**
     * {@code GET  /phongs/:id} : get the "id" phong.
     *
     * @param id the id of the phong to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the phong, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Phong> getPhong(@PathVariable("id") Long id) {
        log.debug("REST request to get Phong : {}", id);
        Optional<Phong> phong = phongRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(phong);
    }

    /**
     * {@code DELETE  /phongs/:id} : delete the "id" phong.
     *
     * @param id the id of the phong to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhong(@PathVariable("id") Long id) {
        log.debug("REST request to delete Phong : {}", id);
        phongRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
