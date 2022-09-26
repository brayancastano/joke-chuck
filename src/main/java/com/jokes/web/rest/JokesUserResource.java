package com.jokes.web.rest;

import com.jokes.domain.JokesUser;
import com.jokes.domain.User;
import com.jokes.repository.JokesUserRepository;
import com.jokes.repository.UserRepository;
import com.jokes.security.SecurityUtils;
import com.jokes.service.JokerUserService;
import com.jokes.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.jokes.domain.JokesUser}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class JokesUserResource {

    private static class JokesUserResourceException extends RuntimeException {

        private JokesUserResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(JokesUserResource.class);

    private static final String ENTITY_NAME = "jokesUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JokesUserRepository jokesUserRepository;

    private final JokerUserService jokerUserService;

    private final UserRepository userRepository;

    public JokesUserResource(JokesUserRepository jokesUserRepository, JokerUserService jokerUserService, UserRepository userRepository) {
        this.jokesUserRepository = jokesUserRepository;
        this.jokerUserService = jokerUserService;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /jokes-users} : Create a new jokesUser.
     *
     * @param jokesUser the jokesUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jokesUser, or with status {@code 400 (Bad Request)} if the jokesUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/jokes-users")
    public ResponseEntity<JokesUser> createJokesUser(@Valid @RequestBody JokesUser jokesUser) throws URISyntaxException {
        log.debug("REST request to save JokesUser : {}", jokesUser);
        if (jokesUser.getId() != null) {
            throw new BadRequestAlertException("A new jokesUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new JokesUserResource.JokesUserResourceException("Current user login not found"));
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        jokesUser.setInternalUser(user.get());
        JokesUser result = jokesUserRepository.save(jokesUser);
        return ResponseEntity
            .created(new URI("/api/jokes-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /jokes-users/:id} : Updates an existing jokesUser.
     *
     * @param id the id of the jokesUser to save.
     * @param jokesUser the jokesUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jokesUser,
     * or with status {@code 400 (Bad Request)} if the jokesUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jokesUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/jokes-users/{id}")
    public ResponseEntity<JokesUser> updateJokesUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody JokesUser jokesUser
    ) throws URISyntaxException {
        log.debug("REST request to update JokesUser : {}, {}", id, jokesUser);
        if (jokesUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jokesUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jokesUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        JokesUser result = jokesUserRepository.save(jokesUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, jokesUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /jokes-users/:id} : Partial updates given fields of an existing jokesUser, field will ignore if it is null
     *
     * @param id the id of the jokesUser to save.
     * @param jokesUser the jokesUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jokesUser,
     * or with status {@code 400 (Bad Request)} if the jokesUser is not valid,
     * or with status {@code 404 (Not Found)} if the jokesUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the jokesUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/jokes-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JokesUser> partialUpdateJokesUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody JokesUser jokesUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update JokesUser partially : {}, {}", id, jokesUser);
        if (jokesUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jokesUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jokesUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JokesUser> result = jokesUserRepository
            .findById(jokesUser.getId())
            .map(existingJokesUser -> {
                if (jokesUser.getIdJoke() != null) {
                    existingJokesUser.setIdJoke(jokesUser.getIdJoke());
                }
                if (jokesUser.getUrl() != null) {
                    existingJokesUser.setUrl(jokesUser.getUrl());
                }
                if (jokesUser.getIconUrl() != null) {
                    existingJokesUser.setIconUrl(jokesUser.getIconUrl());
                }
                if (jokesUser.getValue() != null) {
                    existingJokesUser.setValue(jokesUser.getValue());
                }

                return existingJokesUser;
            })
            .map(jokesUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, jokesUser.getId().toString())
        );
    }

    /**
     * {@code GET  /jokes-users} : get all the jokesUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jokesUsers in body.
     */
    @GetMapping("/jokes-users")
    public List<JokesUser> getAllJokesUsers() {
        log.debug("REST request to get all JokesUsers");
        return jokesUserRepository.findAll();
    }

    /**
     * {@code GET  /jokes-users/:id} : get the "id" jokesUser.
     *
     * @param id the id of the jokesUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jokesUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/jokes-users/{id}")
    public ResponseEntity<JokesUser> getJokesUser(@PathVariable Long id) {
        log.debug("REST request to get JokesUser : {}", id);
        Optional<JokesUser> jokesUser = jokesUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(jokesUser);
    }

    /**
     * {@code DELETE  /jokes-users/:id} : delete the "id" jokesUser.
     *
     * @param id the id of the jokesUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/jokes-users/{id}")
    public ResponseEntity<Void> deleteJokesUser(@PathVariable Long id) {
        log.debug("REST request to delete JokesUser : {}", id);
        jokesUserRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /chuck-api: get Joke.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jokesUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/jokes-users/chuck-api/")
    public ResponseEntity<JokesUser> getJokesUserByApi() {
        return ResponseEntity.ok().body(jokerUserService.transaction());
    }

    /**
     * {@code GET  /jokes-users} : get all the jokesUsers.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jokesUsers in body.
     */
    @GetMapping("/jokes-users/id-user")
    public List<JokesUser> getAllJokesUsersByIdUser() {
        return jokesUserRepository.findByInternalUserIsCurrentUser();
    }
}
