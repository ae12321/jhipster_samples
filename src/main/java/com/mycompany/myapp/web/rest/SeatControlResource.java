package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SeatControl;
import com.mycompany.myapp.repository.SeatControlRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.SeatControl}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SeatControlResource {

    private final Logger log = LoggerFactory.getLogger(SeatControlResource.class);

    private static final String ENTITY_NAME = "seatControl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SeatControlRepository seatControlRepository;

    public SeatControlResource(SeatControlRepository seatControlRepository) {
        this.seatControlRepository = seatControlRepository;
    }

    /**
     * {@code POST  /seat-controls} : Create a new seatControl.
     *
     * @param seatControl the seatControl to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new seatControl, or with status {@code 400 (Bad Request)} if the seatControl has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/seat-controls")
    public ResponseEntity<SeatControl> createSeatControl(@Valid @RequestBody SeatControl seatControl) throws URISyntaxException {
        log.debug("REST request to save SeatControl : {}", seatControl);
        if (seatControl.getId() != null) {
            throw new BadRequestAlertException("A new seatControl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SeatControl result = seatControlRepository.save(seatControl);
        return ResponseEntity
            .created(new URI("/api/seat-controls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /seat-controls/:id} : Updates an existing seatControl.
     *
     * @param id the id of the seatControl to save.
     * @param seatControl the seatControl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seatControl,
     * or with status {@code 400 (Bad Request)} if the seatControl is not valid,
     * or with status {@code 500 (Internal Server Error)} if the seatControl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/seat-controls/{id}")
    public ResponseEntity<SeatControl> updateSeatControl(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SeatControl seatControl
    ) throws URISyntaxException {
        log.debug("REST request to update SeatControl : {}, {}", id, seatControl);
        if (seatControl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seatControl.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seatControlRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SeatControl result = seatControlRepository.save(seatControl);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, seatControl.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /seat-controls/:id} : Partial updates given fields of an existing seatControl, field will ignore if it is null
     *
     * @param id the id of the seatControl to save.
     * @param seatControl the seatControl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seatControl,
     * or with status {@code 400 (Bad Request)} if the seatControl is not valid,
     * or with status {@code 404 (Not Found)} if the seatControl is not found,
     * or with status {@code 500 (Internal Server Error)} if the seatControl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/seat-controls/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SeatControl> partialUpdateSeatControl(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SeatControl seatControl
    ) throws URISyntaxException {
        log.debug("REST request to partial update SeatControl partially : {}, {}", id, seatControl);
        if (seatControl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seatControl.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seatControlRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SeatControl> result = seatControlRepository
            .findById(seatControl.getId())
            .map(existingSeatControl -> {
                if (seatControl.getSeatId() != null) {
                    existingSeatControl.setSeatId(seatControl.getSeatId());
                }

                return existingSeatControl;
            })
            .map(seatControlRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, seatControl.getId().toString())
        );
    }

    /**
     * {@code GET  /seat-controls} : get all the seatControls.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of seatControls in body.
     */
    @GetMapping("/seat-controls")
    public ResponseEntity<List<SeatControl>> getAllSeatControls(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SeatControls");
        Page<SeatControl> page = seatControlRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /seat-controls/:id} : get the "id" seatControl.
     *
     * @param id the id of the seatControl to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the seatControl, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/seat-controls/{id}")
    public ResponseEntity<SeatControl> getSeatControl(@PathVariable Long id) {
        log.debug("REST request to get SeatControl : {}", id);
        Optional<SeatControl> seatControl = seatControlRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(seatControl);
    }

    /**
     * {@code DELETE  /seat-controls/:id} : delete the "id" seatControl.
     *
     * @param id the id of the seatControl to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/seat-controls/{id}")
    public ResponseEntity<Void> deleteSeatControl(@PathVariable Long id) {
        log.debug("REST request to delete SeatControl : {}", id);
        seatControlRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
