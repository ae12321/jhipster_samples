package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Representative;
import com.mycompany.myapp.repository.RepresentativeRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Representative}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RepresentativeResource {

    private final Logger log = LoggerFactory.getLogger(RepresentativeResource.class);

    private static final String ENTITY_NAME = "representative";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RepresentativeRepository representativeRepository;

    public RepresentativeResource(RepresentativeRepository representativeRepository) {
        this.representativeRepository = representativeRepository;
    }

    /**
     * {@code POST  /representatives} : Create a new representative.
     *
     * @param representative the representative to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new representative, or with status {@code 400 (Bad Request)} if the representative has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/representatives")
    public ResponseEntity<Representative> createRepresentative(@Valid @RequestBody Representative representative)
        throws URISyntaxException {
        log.debug("REST request to save Representative : {}", representative);
        if (representative.getId() != null) {
            throw new BadRequestAlertException("A new representative cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Representative result = representativeRepository.save(representative);
        return ResponseEntity
            .created(new URI("/api/representatives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /representatives/:id} : Updates an existing representative.
     *
     * @param id the id of the representative to save.
     * @param representative the representative to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated representative,
     * or with status {@code 400 (Bad Request)} if the representative is not valid,
     * or with status {@code 500 (Internal Server Error)} if the representative couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/representatives/{id}")
    public ResponseEntity<Representative> updateRepresentative(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Representative representative
    ) throws URISyntaxException {
        log.debug("REST request to update Representative : {}, {}", id, representative);
        if (representative.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, representative.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!representativeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Representative result = representativeRepository.save(representative);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, representative.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /representatives/:id} : Partial updates given fields of an existing representative, field will ignore if it is null
     *
     * @param id the id of the representative to save.
     * @param representative the representative to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated representative,
     * or with status {@code 400 (Bad Request)} if the representative is not valid,
     * or with status {@code 404 (Not Found)} if the representative is not found,
     * or with status {@code 500 (Internal Server Error)} if the representative couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/representatives/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Representative> partialUpdateRepresentative(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Representative representative
    ) throws URISyntaxException {
        log.debug("REST request to partial update Representative partially : {}, {}", id, representative);
        if (representative.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, representative.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!representativeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Representative> result = representativeRepository
            .findById(representative.getId())
            .map(existingRepresentative -> {
                if (representative.getName() != null) {
                    existingRepresentative.setName(representative.getName());
                }
                if (representative.getMail() != null) {
                    existingRepresentative.setMail(representative.getMail());
                }
                if (representative.getFace() != null) {
                    existingRepresentative.setFace(representative.getFace());
                }
                if (representative.getFaceId() != null) {
                    existingRepresentative.setFaceId(representative.getFaceId());
                }
                if (representative.getHash() != null) {
                    existingRepresentative.setHash(representative.getHash());
                }

                return existingRepresentative;
            })
            .map(representativeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, representative.getId().toString())
        );
    }

    /**
     * {@code GET  /representatives} : get all the representatives.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of representatives in body.
     */
    @GetMapping("/representatives")
    public ResponseEntity<List<Representative>> getAllRepresentatives(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Representatives");
        Page<Representative> page = representativeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /representatives/:id} : get the "id" representative.
     *
     * @param id the id of the representative to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the representative, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/representatives/{id}")
    public ResponseEntity<Representative> getRepresentative(@PathVariable Long id) {
        log.debug("REST request to get Representative : {}", id);
        Optional<Representative> representative = representativeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(representative);
    }

    /**
     * {@code DELETE  /representatives/:id} : delete the "id" representative.
     *
     * @param id the id of the representative to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/representatives/{id}")
    public ResponseEntity<Void> deleteRepresentative(@PathVariable Long id) {
        log.debug("REST request to delete Representative : {}", id);
        representativeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
