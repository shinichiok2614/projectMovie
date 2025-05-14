package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.DanhSachBapNuoc;
import com.mycompany.myapp.repository.DanhSachBapNuocRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.DanhSachBapNuoc}.
 */
@RestController
@RequestMapping("/api/danh-sach-bap-nuocs")
@Transactional
public class DanhSachBapNuocResource {

    private static final Logger log = LoggerFactory.getLogger(DanhSachBapNuocResource.class);

    private static final String ENTITY_NAME = "danhSachBapNuoc";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DanhSachBapNuocRepository danhSachBapNuocRepository;

    public DanhSachBapNuocResource(DanhSachBapNuocRepository danhSachBapNuocRepository) {
        this.danhSachBapNuocRepository = danhSachBapNuocRepository;
    }

    /**
     * {@code POST  /danh-sach-bap-nuocs} : Create a new danhSachBapNuoc.
     *
     * @param danhSachBapNuoc the danhSachBapNuoc to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new danhSachBapNuoc, or with status {@code 400 (Bad Request)} if the danhSachBapNuoc has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DanhSachBapNuoc> createDanhSachBapNuoc(@Valid @RequestBody DanhSachBapNuoc danhSachBapNuoc)
        throws URISyntaxException {
        log.debug("REST request to save DanhSachBapNuoc : {}", danhSachBapNuoc);
        if (danhSachBapNuoc.getId() != null) {
            throw new BadRequestAlertException("A new danhSachBapNuoc cannot already have an ID", ENTITY_NAME, "idexists");
        }
        danhSachBapNuoc = danhSachBapNuocRepository.save(danhSachBapNuoc);
        return ResponseEntity.created(new URI("/api/danh-sach-bap-nuocs/" + danhSachBapNuoc.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, danhSachBapNuoc.getId().toString()))
            .body(danhSachBapNuoc);
    }

    /**
     * {@code PUT  /danh-sach-bap-nuocs/:id} : Updates an existing danhSachBapNuoc.
     *
     * @param id the id of the danhSachBapNuoc to save.
     * @param danhSachBapNuoc the danhSachBapNuoc to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated danhSachBapNuoc,
     * or with status {@code 400 (Bad Request)} if the danhSachBapNuoc is not valid,
     * or with status {@code 500 (Internal Server Error)} if the danhSachBapNuoc couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DanhSachBapNuoc> updateDanhSachBapNuoc(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DanhSachBapNuoc danhSachBapNuoc
    ) throws URISyntaxException {
        log.debug("REST request to update DanhSachBapNuoc : {}, {}", id, danhSachBapNuoc);
        if (danhSachBapNuoc.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, danhSachBapNuoc.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!danhSachBapNuocRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        danhSachBapNuoc = danhSachBapNuocRepository.save(danhSachBapNuoc);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, danhSachBapNuoc.getId().toString()))
            .body(danhSachBapNuoc);
    }

    /**
     * {@code PATCH  /danh-sach-bap-nuocs/:id} : Partial updates given fields of an existing danhSachBapNuoc, field will ignore if it is null
     *
     * @param id the id of the danhSachBapNuoc to save.
     * @param danhSachBapNuoc the danhSachBapNuoc to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated danhSachBapNuoc,
     * or with status {@code 400 (Bad Request)} if the danhSachBapNuoc is not valid,
     * or with status {@code 404 (Not Found)} if the danhSachBapNuoc is not found,
     * or with status {@code 500 (Internal Server Error)} if the danhSachBapNuoc couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DanhSachBapNuoc> partialUpdateDanhSachBapNuoc(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DanhSachBapNuoc danhSachBapNuoc
    ) throws URISyntaxException {
        log.debug("REST request to partial update DanhSachBapNuoc partially : {}, {}", id, danhSachBapNuoc);
        if (danhSachBapNuoc.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, danhSachBapNuoc.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!danhSachBapNuocRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DanhSachBapNuoc> result = danhSachBapNuocRepository
            .findById(danhSachBapNuoc.getId())
            .map(existingDanhSachBapNuoc -> {
                if (danhSachBapNuoc.getSoDienThoai() != null) {
                    existingDanhSachBapNuoc.setSoDienThoai(danhSachBapNuoc.getSoDienThoai());
                }
                if (danhSachBapNuoc.getTenBapNuoc() != null) {
                    existingDanhSachBapNuoc.setTenBapNuoc(danhSachBapNuoc.getTenBapNuoc());
                }

                return existingDanhSachBapNuoc;
            })
            .map(danhSachBapNuocRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, danhSachBapNuoc.getId().toString())
        );
    }

    /**
     * {@code GET  /danh-sach-bap-nuocs} : get all the danhSachBapNuocs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of danhSachBapNuocs in body.
     */
    @GetMapping("")
    public List<DanhSachBapNuoc> getAllDanhSachBapNuocs() {
        log.debug("REST request to get all DanhSachBapNuocs");
        return danhSachBapNuocRepository.findAll();
    }

    /**
     * {@code GET  /danh-sach-bap-nuocs/:id} : get the "id" danhSachBapNuoc.
     *
     * @param id the id of the danhSachBapNuoc to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the danhSachBapNuoc, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DanhSachBapNuoc> getDanhSachBapNuoc(@PathVariable("id") Long id) {
        log.debug("REST request to get DanhSachBapNuoc : {}", id);
        Optional<DanhSachBapNuoc> danhSachBapNuoc = danhSachBapNuocRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(danhSachBapNuoc);
    }

    /**
     * {@code DELETE  /danh-sach-bap-nuocs/:id} : delete the "id" danhSachBapNuoc.
     *
     * @param id the id of the danhSachBapNuoc to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDanhSachBapNuoc(@PathVariable("id") Long id) {
        log.debug("REST request to delete DanhSachBapNuoc : {}", id);
        danhSachBapNuocRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
