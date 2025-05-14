package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Phim;
import com.mycompany.myapp.repository.PhimRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Phim}.
 */
@RestController
@RequestMapping("/api/phims")
@Transactional
public class PhimResource {

    private static final Logger log = LoggerFactory.getLogger(PhimResource.class);

    private static final String ENTITY_NAME = "phim";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhimRepository phimRepository;

    public PhimResource(PhimRepository phimRepository) {
        this.phimRepository = phimRepository;
    }

    /**
     * {@code POST  /phims} : Create a new phim.
     *
     * @param phim the phim to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new phim, or with status {@code 400 (Bad Request)} if the phim has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Phim> createPhim(@Valid @RequestBody Phim phim) throws URISyntaxException {
        log.debug("REST request to save Phim : {}", phim);
        if (phim.getId() != null) {
            throw new BadRequestAlertException("A new phim cannot already have an ID", ENTITY_NAME, "idexists");
        }
        phim = phimRepository.save(phim);
        return ResponseEntity.created(new URI("/api/phims/" + phim.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, phim.getId().toString()))
            .body(phim);
    }

    /**
     * {@code PUT  /phims/:id} : Updates an existing phim.
     *
     * @param id the id of the phim to save.
     * @param phim the phim to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phim,
     * or with status {@code 400 (Bad Request)} if the phim is not valid,
     * or with status {@code 500 (Internal Server Error)} if the phim couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Phim> updatePhim(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Phim phim)
        throws URISyntaxException {
        log.debug("REST request to update Phim : {}, {}", id, phim);
        if (phim.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phim.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        phim = phimRepository.save(phim);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phim.getId().toString()))
            .body(phim);
    }

    /**
     * {@code PATCH  /phims/:id} : Partial updates given fields of an existing phim, field will ignore if it is null
     *
     * @param id the id of the phim to save.
     * @param phim the phim to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phim,
     * or with status {@code 400 (Bad Request)} if the phim is not valid,
     * or with status {@code 404 (Not Found)} if the phim is not found,
     * or with status {@code 500 (Internal Server Error)} if the phim couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Phim> partialUpdatePhim(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Phim phim
    ) throws URISyntaxException {
        log.debug("REST request to partial update Phim partially : {}, {}", id, phim);
        if (phim.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phim.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Phim> result = phimRepository
            .findById(phim.getId())
            .map(existingPhim -> {
                if (phim.getTenPhim() != null) {
                    existingPhim.setTenPhim(phim.getTenPhim());
                }
                if (phim.getThoiLuong() != null) {
                    existingPhim.setThoiLuong(phim.getThoiLuong());
                }
                if (phim.getGioiThieu() != null) {
                    existingPhim.setGioiThieu(phim.getGioiThieu());
                }
                if (phim.getNgayCongChieu() != null) {
                    existingPhim.setNgayCongChieu(phim.getNgayCongChieu());
                }
                if (phim.getLinkTrailer() != null) {
                    existingPhim.setLinkTrailer(phim.getLinkTrailer());
                }
                if (phim.getLogo() != null) {
                    existingPhim.setLogo(phim.getLogo());
                }
                if (phim.getLogoContentType() != null) {
                    existingPhim.setLogoContentType(phim.getLogoContentType());
                }
                if (phim.getDoTuoi() != null) {
                    existingPhim.setDoTuoi(phim.getDoTuoi());
                }
                if (phim.getTheLoai() != null) {
                    existingPhim.setTheLoai(phim.getTheLoai());
                }
                if (phim.getDinhDang() != null) {
                    existingPhim.setDinhDang(phim.getDinhDang());
                }

                return existingPhim;
            })
            .map(phimRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phim.getId().toString())
        );
    }

    /**
     * {@code GET  /phims} : get all the phims.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of phims in body.
     */
    @GetMapping("")
    public List<Phim> getAllPhims() {
        log.debug("REST request to get all Phims");
        return phimRepository.findAll();
    }

    /**
     * {@code GET  /phims/:id} : get the "id" phim.
     *
     * @param id the id of the phim to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the phim, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Phim> getPhim(@PathVariable("id") Long id) {
        log.debug("REST request to get Phim : {}", id);
        Optional<Phim> phim = phimRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(phim);
    }

    /**
     * {@code DELETE  /phims/:id} : delete the "id" phim.
     *
     * @param id the id of the phim to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhim(@PathVariable("id") Long id) {
        log.debug("REST request to delete Phim : {}", id);
        phimRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
