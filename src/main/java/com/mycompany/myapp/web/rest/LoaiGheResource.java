package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.LoaiGhe;
import com.mycompany.myapp.repository.LoaiGheRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.LoaiGhe}.
 */
@RestController
@RequestMapping("/api/loai-ghes")
@Transactional
public class LoaiGheResource {

    private static final Logger log = LoggerFactory.getLogger(LoaiGheResource.class);

    private static final String ENTITY_NAME = "loaiGhe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoaiGheRepository loaiGheRepository;

    public LoaiGheResource(LoaiGheRepository loaiGheRepository) {
        this.loaiGheRepository = loaiGheRepository;
    }

    /**
     * {@code POST  /loai-ghes} : Create a new loaiGhe.
     *
     * @param loaiGhe the loaiGhe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loaiGhe, or with status {@code 400 (Bad Request)} if the loaiGhe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LoaiGhe> createLoaiGhe(@Valid @RequestBody LoaiGhe loaiGhe) throws URISyntaxException {
        log.debug("REST request to save LoaiGhe : {}", loaiGhe);
        if (loaiGhe.getId() != null) {
            throw new BadRequestAlertException("A new loaiGhe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        loaiGhe = loaiGheRepository.save(loaiGhe);
        return ResponseEntity.created(new URI("/api/loai-ghes/" + loaiGhe.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, loaiGhe.getId().toString()))
            .body(loaiGhe);
    }

    /**
     * {@code PUT  /loai-ghes/:id} : Updates an existing loaiGhe.
     *
     * @param id the id of the loaiGhe to save.
     * @param loaiGhe the loaiGhe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loaiGhe,
     * or with status {@code 400 (Bad Request)} if the loaiGhe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loaiGhe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LoaiGhe> updateLoaiGhe(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LoaiGhe loaiGhe
    ) throws URISyntaxException {
        log.debug("REST request to update LoaiGhe : {}, {}", id, loaiGhe);
        if (loaiGhe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loaiGhe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loaiGheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        loaiGhe = loaiGheRepository.save(loaiGhe);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loaiGhe.getId().toString()))
            .body(loaiGhe);
    }

    /**
     * {@code PATCH  /loai-ghes/:id} : Partial updates given fields of an existing loaiGhe, field will ignore if it is null
     *
     * @param id the id of the loaiGhe to save.
     * @param loaiGhe the loaiGhe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loaiGhe,
     * or with status {@code 400 (Bad Request)} if the loaiGhe is not valid,
     * or with status {@code 404 (Not Found)} if the loaiGhe is not found,
     * or with status {@code 500 (Internal Server Error)} if the loaiGhe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoaiGhe> partialUpdateLoaiGhe(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LoaiGhe loaiGhe
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoaiGhe partially : {}, {}", id, loaiGhe);
        if (loaiGhe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loaiGhe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loaiGheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoaiGhe> result = loaiGheRepository
            .findById(loaiGhe.getId())
            .map(existingLoaiGhe -> {
                if (loaiGhe.getTenLoai() != null) {
                    existingLoaiGhe.setTenLoai(loaiGhe.getTenLoai());
                }
                if (loaiGhe.getGiaTien() != null) {
                    existingLoaiGhe.setGiaTien(loaiGhe.getGiaTien());
                }

                return existingLoaiGhe;
            })
            .map(loaiGheRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loaiGhe.getId().toString())
        );
    }

    /**
     * {@code GET  /loai-ghes} : get all the loaiGhes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loaiGhes in body.
     */
    @GetMapping("")
    public List<LoaiGhe> getAllLoaiGhes() {
        log.debug("REST request to get all LoaiGhes");
        return loaiGheRepository.findAll();
    }

    /**
     * {@code GET  /loai-ghes/:id} : get the "id" loaiGhe.
     *
     * @param id the id of the loaiGhe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loaiGhe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoaiGhe> getLoaiGhe(@PathVariable("id") Long id) {
        log.debug("REST request to get LoaiGhe : {}", id);
        Optional<LoaiGhe> loaiGhe = loaiGheRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(loaiGhe);
    }

    /**
     * {@code DELETE  /loai-ghes/:id} : delete the "id" loaiGhe.
     *
     * @param id the id of the loaiGhe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoaiGhe(@PathVariable("id") Long id) {
        log.debug("REST request to delete LoaiGhe : {}", id);
        loaiGheRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
