package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SeatGroup;
import com.mycompany.myapp.repository.SeatGroupRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SeatGroup}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SeatGroupResource {

    private final Logger log = LoggerFactory.getLogger(SeatGroupResource.class);

    private static final String ENTITY_NAME = "seatGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SeatGroupRepository seatGroupRepository;

    public SeatGroupResource(SeatGroupRepository seatGroupRepository) {
        this.seatGroupRepository = seatGroupRepository;
    }

    /**
     * {@code POST  /seat-groups} : Create a new seatGroup.
     *
     * @param seatGroup the seatGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new seatGroup, or with status {@code 400 (Bad Request)} if the seatGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/seat-groups")
    public ResponseEntity<SeatGroup> createSeatGroup(@Valid @RequestBody SeatGroup seatGroup) throws URISyntaxException {
        log.debug("REST request to save SeatGroup : {}", seatGroup);
        if (seatGroup.getId() != null) {
            throw new BadRequestAlertException("A new seatGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SeatGroup result = seatGroupRepository.save(seatGroup);
        return ResponseEntity
            .created(new URI("/api/seat-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /seat-groups/:id} : Updates an existing seatGroup.
     *
     * @param id the id of the seatGroup to save.
     * @param seatGroup the seatGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seatGroup,
     * or with status {@code 400 (Bad Request)} if the seatGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the seatGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/seat-groups/{id}")
    public ResponseEntity<SeatGroup> updateSeatGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SeatGroup seatGroup
    ) throws URISyntaxException {
        log.debug("REST request to update SeatGroup : {}, {}", id, seatGroup);
        if (seatGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seatGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seatGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SeatGroup result = seatGroupRepository.save(seatGroup);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, seatGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /seat-groups/:id} : Partial updates given fields of an existing seatGroup, field will ignore if it is null
     *
     * @param id the id of the seatGroup to save.
     * @param seatGroup the seatGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seatGroup,
     * or with status {@code 400 (Bad Request)} if the seatGroup is not valid,
     * or with status {@code 404 (Not Found)} if the seatGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the seatGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/seat-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SeatGroup> partialUpdateSeatGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SeatGroup seatGroup
    ) throws URISyntaxException {
        log.debug("REST request to partial update SeatGroup partially : {}, {}", id, seatGroup);
        if (seatGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seatGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seatGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SeatGroup> result = seatGroupRepository
            .findById(seatGroup.getId())
            .map(existingSeatGroup -> {
                if (seatGroup.getName() != null) {
                    existingSeatGroup.setName(seatGroup.getName());
                }

                return existingSeatGroup;
            })
            .map(seatGroupRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, seatGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /seat-groups} : get all the seatGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of seatGroups in body.
     */
    @GetMapping("/seat-groups")
    public ResponseEntity<List<SeatGroup>> getAllSeatGroups(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SeatGroups");
        Page<SeatGroup> page = seatGroupRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /seat-groups/:id} : get the "id" seatGroup.
     *
     * @param id the id of the seatGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the seatGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/seat-groups/{id}")
    public ResponseEntity<SeatGroup> getSeatGroup(@PathVariable Long id) {
        log.debug("REST request to get SeatGroup : {}", id);
        Optional<SeatGroup> seatGroup = seatGroupRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(seatGroup);
    }

    /**
     * {@code DELETE  /seat-groups/:id} : delete the "id" seatGroup.
     *
     * @param id the id of the seatGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/seat-groups/{id}")
    public ResponseEntity<Void> deleteSeatGroup(@PathVariable Long id) {
        log.debug("REST request to delete SeatGroup : {}", id);
        seatGroupRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
