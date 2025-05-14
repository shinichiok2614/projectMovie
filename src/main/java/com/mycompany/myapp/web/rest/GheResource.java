package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Ghe;
import com.mycompany.myapp.repository.GheRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Ghe}.
 */
@RestController
@RequestMapping("/api/ghes")
@Transactional
public class GheResource {

    private static final Logger log = LoggerFactory.getLogger(GheResource.class);

    private static final String ENTITY_NAME = "ghe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GheRepository gheRepository;

    public GheResource(GheRepository gheRepository) {
        this.gheRepository = gheRepository;
    }

    /**
     * {@code POST  /ghes} : Create a new ghe.
     *
     * @param ghe the ghe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ghe, or with status {@code 400 (Bad Request)} if the ghe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Ghe> createGhe(@Valid @RequestBody Ghe ghe) throws URISyntaxException {
        log.debug("REST request to save Ghe : {}", ghe);
        if (ghe.getId() != null) {
            throw new BadRequestAlertException("A new ghe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ghe = gheRepository.save(ghe);
        return ResponseEntity.created(new URI("/api/ghes/" + ghe.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ghe.getId().toString()))
            .body(ghe);
    }

    /**
     * {@code PUT  /ghes/:id} : Updates an existing ghe.
     *
     * @param id the id of the ghe to save.
     * @param ghe the ghe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ghe,
     * or with status {@code 400 (Bad Request)} if the ghe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ghe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ghe> updateGhe(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Ghe ghe)
        throws URISyntaxException {
        log.debug("REST request to update Ghe : {}, {}", id, ghe);
        if (ghe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ghe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ghe = gheRepository.save(ghe);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ghe.getId().toString()))
            .body(ghe);
    }

    /**
     * {@code PATCH  /ghes/:id} : Partial updates given fields of an existing ghe, field will ignore if it is null
     *
     * @param id the id of the ghe to save.
     * @param ghe the ghe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ghe,
     * or with status {@code 400 (Bad Request)} if the ghe is not valid,
     * or with status {@code 404 (Not Found)} if the ghe is not found,
     * or with status {@code 500 (Internal Server Error)} if the ghe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ghe> partialUpdateGhe(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Ghe ghe)
        throws URISyntaxException {
        log.debug("REST request to partial update Ghe partially : {}, {}", id, ghe);
        if (ghe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ghe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ghe> result = gheRepository
            .findById(ghe.getId())
            .map(existingGhe -> {
                if (ghe.getTenGhe() != null) {
                    existingGhe.setTenGhe(ghe.getTenGhe());
                }
                if (ghe.getTinhTrang() != null) {
                    existingGhe.setTinhTrang(ghe.getTinhTrang());
                }

                return existingGhe;
            })
            .map(gheRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ghe.getId().toString())
        );
    }

    /**
     * {@code GET  /ghes} : get all the ghes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ghes in body.
     */
    @GetMapping("")
    public List<Ghe> getAllGhes() {
        log.debug("REST request to get all Ghes");
        return gheRepository.findAll();
    }

    /**
     * {@code GET  /ghes/:id} : get the "id" ghe.
     *
     * @param id the id of the ghe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ghe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ghe> getGhe(@PathVariable("id") Long id) {
        log.debug("REST request to get Ghe : {}", id);
        Optional<Ghe> ghe = gheRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ghe);
    }

    /**
     * {@code DELETE  /ghes/:id} : delete the "id" ghe.
     *
     * @param id the id of the ghe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGhe(@PathVariable("id") Long id) {
        log.debug("REST request to delete Ghe : {}", id);
        gheRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
