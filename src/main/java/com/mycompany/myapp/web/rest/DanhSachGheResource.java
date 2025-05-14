package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.DanhSachGhe;
import com.mycompany.myapp.repository.DanhSachGheRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.DanhSachGhe}.
 */
@RestController
@RequestMapping("/api/danh-sach-ghes")
@Transactional
public class DanhSachGheResource {

    private static final Logger log = LoggerFactory.getLogger(DanhSachGheResource.class);

    private static final String ENTITY_NAME = "danhSachGhe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DanhSachGheRepository danhSachGheRepository;

    public DanhSachGheResource(DanhSachGheRepository danhSachGheRepository) {
        this.danhSachGheRepository = danhSachGheRepository;
    }

    /**
     * {@code POST  /danh-sach-ghes} : Create a new danhSachGhe.
     *
     * @param danhSachGhe the danhSachGhe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new danhSachGhe, or with status {@code 400 (Bad Request)} if the danhSachGhe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DanhSachGhe> createDanhSachGhe(@Valid @RequestBody DanhSachGhe danhSachGhe) throws URISyntaxException {
        log.debug("REST request to save DanhSachGhe : {}", danhSachGhe);
        if (danhSachGhe.getId() != null) {
            throw new BadRequestAlertException("A new danhSachGhe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        danhSachGhe = danhSachGheRepository.save(danhSachGhe);
        return ResponseEntity.created(new URI("/api/danh-sach-ghes/" + danhSachGhe.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, danhSachGhe.getId().toString()))
            .body(danhSachGhe);
    }

    /**
     * {@code PUT  /danh-sach-ghes/:id} : Updates an existing danhSachGhe.
     *
     * @param id the id of the danhSachGhe to save.
     * @param danhSachGhe the danhSachGhe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated danhSachGhe,
     * or with status {@code 400 (Bad Request)} if the danhSachGhe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the danhSachGhe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DanhSachGhe> updateDanhSachGhe(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DanhSachGhe danhSachGhe
    ) throws URISyntaxException {
        log.debug("REST request to update DanhSachGhe : {}, {}", id, danhSachGhe);
        if (danhSachGhe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, danhSachGhe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!danhSachGheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        danhSachGhe = danhSachGheRepository.save(danhSachGhe);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, danhSachGhe.getId().toString()))
            .body(danhSachGhe);
    }

    /**
     * {@code PATCH  /danh-sach-ghes/:id} : Partial updates given fields of an existing danhSachGhe, field will ignore if it is null
     *
     * @param id the id of the danhSachGhe to save.
     * @param danhSachGhe the danhSachGhe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated danhSachGhe,
     * or with status {@code 400 (Bad Request)} if the danhSachGhe is not valid,
     * or with status {@code 404 (Not Found)} if the danhSachGhe is not found,
     * or with status {@code 500 (Internal Server Error)} if the danhSachGhe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DanhSachGhe> partialUpdateDanhSachGhe(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DanhSachGhe danhSachGhe
    ) throws URISyntaxException {
        log.debug("REST request to partial update DanhSachGhe partially : {}, {}", id, danhSachGhe);
        if (danhSachGhe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, danhSachGhe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!danhSachGheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DanhSachGhe> result = danhSachGheRepository
            .findById(danhSachGhe.getId())
            .map(existingDanhSachGhe -> {
                if (danhSachGhe.getSoDienThoai() != null) {
                    existingDanhSachGhe.setSoDienThoai(danhSachGhe.getSoDienThoai());
                }
                if (danhSachGhe.getTenGhe() != null) {
                    existingDanhSachGhe.setTenGhe(danhSachGhe.getTenGhe());
                }

                return existingDanhSachGhe;
            })
            .map(danhSachGheRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, danhSachGhe.getId().toString())
        );
    }

    /**
     * {@code GET  /danh-sach-ghes} : get all the danhSachGhes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of danhSachGhes in body.
     */
    @GetMapping("")
    public List<DanhSachGhe> getAllDanhSachGhes() {
        log.debug("REST request to get all DanhSachGhes");
        return danhSachGheRepository.findAll();
    }

    /**
     * {@code GET  /danh-sach-ghes/:id} : get the "id" danhSachGhe.
     *
     * @param id the id of the danhSachGhe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the danhSachGhe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DanhSachGhe> getDanhSachGhe(@PathVariable("id") Long id) {
        log.debug("REST request to get DanhSachGhe : {}", id);
        Optional<DanhSachGhe> danhSachGhe = danhSachGheRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(danhSachGhe);
    }

    /**
     * {@code DELETE  /danh-sach-ghes/:id} : delete the "id" danhSachGhe.
     *
     * @param id the id of the danhSachGhe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDanhSachGhe(@PathVariable("id") Long id) {
        log.debug("REST request to delete DanhSachGhe : {}", id);
        danhSachGheRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
