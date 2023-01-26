package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ReservedSeat;
import com.mycompany.myapp.repository.ReservedSeatRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ReservedSeat}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReservedSeatResource {

    private final Logger log = LoggerFactory.getLogger(ReservedSeatResource.class);

    private static final String ENTITY_NAME = "reservedSeat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReservedSeatRepository reservedSeatRepository;

    public ReservedSeatResource(ReservedSeatRepository reservedSeatRepository) {
        this.reservedSeatRepository = reservedSeatRepository;
    }

    /**
     * {@code POST  /reserved-seats} : Create a new reservedSeat.
     *
     * @param reservedSeat the reservedSeat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reservedSeat, or with status {@code 400 (Bad Request)} if the reservedSeat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reserved-seats")
    public ResponseEntity<ReservedSeat> createReservedSeat(@Valid @RequestBody ReservedSeat reservedSeat) throws URISyntaxException {
        log.debug("REST request to save ReservedSeat : {}", reservedSeat);
        if (reservedSeat.getId() != null) {
            throw new BadRequestAlertException("A new reservedSeat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReservedSeat result = reservedSeatRepository.save(reservedSeat);
        return ResponseEntity
            .created(new URI("/api/reserved-seats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reserved-seats/:id} : Updates an existing reservedSeat.
     *
     * @param id the id of the reservedSeat to save.
     * @param reservedSeat the reservedSeat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservedSeat,
     * or with status {@code 400 (Bad Request)} if the reservedSeat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reservedSeat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reserved-seats/{id}")
    public ResponseEntity<ReservedSeat> updateReservedSeat(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReservedSeat reservedSeat
    ) throws URISyntaxException {
        log.debug("REST request to update ReservedSeat : {}, {}", id, reservedSeat);
        if (reservedSeat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservedSeat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservedSeatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReservedSeat result = reservedSeatRepository.save(reservedSeat);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reservedSeat.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reserved-seats/:id} : Partial updates given fields of an existing reservedSeat, field will ignore if it is null
     *
     * @param id the id of the reservedSeat to save.
     * @param reservedSeat the reservedSeat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservedSeat,
     * or with status {@code 400 (Bad Request)} if the reservedSeat is not valid,
     * or with status {@code 404 (Not Found)} if the reservedSeat is not found,
     * or with status {@code 500 (Internal Server Error)} if the reservedSeat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reserved-seats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReservedSeat> partialUpdateReservedSeat(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReservedSeat reservedSeat
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReservedSeat partially : {}, {}", id, reservedSeat);
        if (reservedSeat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservedSeat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservedSeatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReservedSeat> result = reservedSeatRepository
            .findById(reservedSeat.getId())
            .map(existingReservedSeat -> {
                if (reservedSeat.getPersonName() != null) {
                    existingReservedSeat.setPersonName(reservedSeat.getPersonName());
                }
                if (reservedSeat.getSeatName() != null) {
                    existingReservedSeat.setSeatName(reservedSeat.getSeatName());
                }

                return existingReservedSeat;
            })
            .map(reservedSeatRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reservedSeat.getId().toString())
        );
    }

    /**
     * {@code GET  /reserved-seats} : get all the reservedSeats.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reservedSeats in body.
     */
    @GetMapping("/reserved-seats")
    public ResponseEntity<List<ReservedSeat>> getAllReservedSeats(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of ReservedSeats");
        Page<ReservedSeat> page;
        if (eagerload) {
            page = reservedSeatRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = reservedSeatRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reserved-seats/:id} : get the "id" reservedSeat.
     *
     * @param id the id of the reservedSeat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reservedSeat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reserved-seats/{id}")
    public ResponseEntity<ReservedSeat> getReservedSeat(@PathVariable Long id) {
        log.debug("REST request to get ReservedSeat : {}", id);
        Optional<ReservedSeat> reservedSeat = reservedSeatRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(reservedSeat);
    }

    /**
     * {@code DELETE  /reserved-seats/:id} : delete the "id" reservedSeat.
     *
     * @param id the id of the reservedSeat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reserved-seats/{id}")
    public ResponseEntity<Void> deleteReservedSeat(@PathVariable Long id) {
        log.debug("REST request to delete ReservedSeat : {}", id);
        reservedSeatRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
