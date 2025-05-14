package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Rap;
import com.mycompany.myapp.repository.RapRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Rap}.
 */
@RestController
@RequestMapping("/api/raps")
@Transactional
public class RapResource {

    private static final Logger log = LoggerFactory.getLogger(RapResource.class);

    private static final String ENTITY_NAME = "rap";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RapRepository rapRepository;

    public RapResource(RapRepository rapRepository) {
        this.rapRepository = rapRepository;
    }

    /**
     * {@code POST  /raps} : Create a new rap.
     *
     * @param rap the rap to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rap, or with status {@code 400 (Bad Request)} if the rap has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Rap> createRap(@Valid @RequestBody Rap rap) throws URISyntaxException {
        log.debug("REST request to save Rap : {}", rap);
        if (rap.getId() != null) {
            throw new BadRequestAlertException("A new rap cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rap = rapRepository.save(rap);
        return ResponseEntity.created(new URI("/api/raps/" + rap.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, rap.getId().toString()))
            .body(rap);
    }

    /**
     * {@code PUT  /raps/:id} : Updates an existing rap.
     *
     * @param id the id of the rap to save.
     * @param rap the rap to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rap,
     * or with status {@code 400 (Bad Request)} if the rap is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rap couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Rap> updateRap(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Rap rap)
        throws URISyntaxException {
        log.debug("REST request to update Rap : {}, {}", id, rap);
        if (rap.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rap.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rapRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rap = rapRepository.save(rap);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rap.getId().toString()))
            .body(rap);
    }

    /**
     * {@code PATCH  /raps/:id} : Partial updates given fields of an existing rap, field will ignore if it is null
     *
     * @param id the id of the rap to save.
     * @param rap the rap to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rap,
     * or with status {@code 400 (Bad Request)} if the rap is not valid,
     * or with status {@code 404 (Not Found)} if the rap is not found,
     * or with status {@code 500 (Internal Server Error)} if the rap couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Rap> partialUpdateRap(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Rap rap)
        throws URISyntaxException {
        log.debug("REST request to partial update Rap partially : {}, {}", id, rap);
        if (rap.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rap.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rapRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Rap> result = rapRepository
            .findById(rap.getId())
            .map(existingRap -> {
                if (rap.getTenRap() != null) {
                    existingRap.setTenRap(rap.getTenRap());
                }
                if (rap.getDiaChi() != null) {
                    existingRap.setDiaChi(rap.getDiaChi());
                }
                if (rap.getThanhPho() != null) {
                    existingRap.setThanhPho(rap.getThanhPho());
                }

                return existingRap;
            })
            .map(rapRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rap.getId().toString())
        );
    }

    /**
     * {@code GET  /raps} : get all the raps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of raps in body.
     */
    @GetMapping("")
    public List<Rap> getAllRaps() {
        log.debug("REST request to get all Raps");
        return rapRepository.findAll();
    }

    /**
     * {@code GET  /raps/:id} : get the "id" rap.
     *
     * @param id the id of the rap to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rap, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Rap> getRap(@PathVariable("id") Long id) {
        log.debug("REST request to get Rap : {}", id);
        Optional<Rap> rap = rapRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rap);
    }

    @GetMapping("/by-cum-rap/{id}")
    public List<Rap> getAllRapsByCumRapId(@PathVariable("id") Long id) {
        log.debug("REST request to get Rap by CumRapId : {}", id);
        return rapRepository.findAllByCumRapId(id);
    }

    /**
     * {@code DELETE  /raps/:id} : delete the "id" rap.
     *
     * @param id the id of the rap to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRap(@PathVariable("id") Long id) {
        log.debug("REST request to delete Rap : {}", id);
        rapRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
