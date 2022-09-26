package com.jokes.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jokes.IntegrationTest;
import com.jokes.domain.JokesUser;
import com.jokes.repository.JokesUserRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link JokesUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JokesUserResourceIT {

    private static final String DEFAULT_ID_JOKE = "AAAAAAAAAA";
    private static final String UPDATED_ID_JOKE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ICON_URL = "AAAAAAAAAA";
    private static final String UPDATED_ICON_URL = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/jokes-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JokesUserRepository jokesUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJokesUserMockMvc;

    private JokesUser jokesUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JokesUser createEntity(EntityManager em) {
        JokesUser jokesUser = new JokesUser().idJoke(DEFAULT_ID_JOKE).url(DEFAULT_URL).iconUrl(DEFAULT_ICON_URL).value(DEFAULT_VALUE);
        return jokesUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JokesUser createUpdatedEntity(EntityManager em) {
        JokesUser jokesUser = new JokesUser().idJoke(UPDATED_ID_JOKE).url(UPDATED_URL).iconUrl(UPDATED_ICON_URL).value(UPDATED_VALUE);
        return jokesUser;
    }

    @BeforeEach
    public void initTest() {
        jokesUser = createEntity(em);
    }

    @Test
    @Transactional
    void createJokesUser() throws Exception {
        int databaseSizeBeforeCreate = jokesUserRepository.findAll().size();
        // Create the JokesUser
        restJokesUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jokesUser))
            )
            .andExpect(status().isCreated());

        // Validate the JokesUser in the database
        List<JokesUser> jokesUserList = jokesUserRepository.findAll();
        assertThat(jokesUserList).hasSize(databaseSizeBeforeCreate + 1);
        JokesUser testJokesUser = jokesUserList.get(jokesUserList.size() - 1);
        assertThat(testJokesUser.getIdJoke()).isEqualTo(DEFAULT_ID_JOKE);
        assertThat(testJokesUser.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testJokesUser.getIconUrl()).isEqualTo(DEFAULT_ICON_URL);
        assertThat(testJokesUser.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createJokesUserWithExistingId() throws Exception {
        // Create the JokesUser with an existing ID
        jokesUser.setId(1L);

        int databaseSizeBeforeCreate = jokesUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJokesUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jokesUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the JokesUser in the database
        List<JokesUser> jokesUserList = jokesUserRepository.findAll();
        assertThat(jokesUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdJokeIsRequired() throws Exception {
        int databaseSizeBeforeTest = jokesUserRepository.findAll().size();
        // set the field null
        jokesUser.setIdJoke(null);

        // Create the JokesUser, which fails.

        restJokesUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jokesUser))
            )
            .andExpect(status().isBadRequest());

        List<JokesUser> jokesUserList = jokesUserRepository.findAll();
        assertThat(jokesUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = jokesUserRepository.findAll().size();
        // set the field null
        jokesUser.setUrl(null);

        // Create the JokesUser, which fails.

        restJokesUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jokesUser))
            )
            .andExpect(status().isBadRequest());

        List<JokesUser> jokesUserList = jokesUserRepository.findAll();
        assertThat(jokesUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIconUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = jokesUserRepository.findAll().size();
        // set the field null
        jokesUser.setIconUrl(null);

        // Create the JokesUser, which fails.

        restJokesUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jokesUser))
            )
            .andExpect(status().isBadRequest());

        List<JokesUser> jokesUserList = jokesUserRepository.findAll();
        assertThat(jokesUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = jokesUserRepository.findAll().size();
        // set the field null
        jokesUser.setValue(null);

        // Create the JokesUser, which fails.

        restJokesUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jokesUser))
            )
            .andExpect(status().isBadRequest());

        List<JokesUser> jokesUserList = jokesUserRepository.findAll();
        assertThat(jokesUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJokesUsers() throws Exception {
        // Initialize the database
        jokesUserRepository.saveAndFlush(jokesUser);

        // Get all the jokesUserList
        restJokesUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jokesUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].idJoke").value(hasItem(DEFAULT_ID_JOKE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].iconUrl").value(hasItem(DEFAULT_ICON_URL)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getJokesUser() throws Exception {
        // Initialize the database
        jokesUserRepository.saveAndFlush(jokesUser);

        // Get the jokesUser
        restJokesUserMockMvc
            .perform(get(ENTITY_API_URL_ID, jokesUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jokesUser.getId().intValue()))
            .andExpect(jsonPath("$.idJoke").value(DEFAULT_ID_JOKE))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.iconUrl").value(DEFAULT_ICON_URL))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingJokesUser() throws Exception {
        // Get the jokesUser
        restJokesUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJokesUser() throws Exception {
        // Initialize the database
        jokesUserRepository.saveAndFlush(jokesUser);

        int databaseSizeBeforeUpdate = jokesUserRepository.findAll().size();

        // Update the jokesUser
        JokesUser updatedJokesUser = jokesUserRepository.findById(jokesUser.getId()).get();
        // Disconnect from session so that the updates on updatedJokesUser are not directly saved in db
        em.detach(updatedJokesUser);
        updatedJokesUser.idJoke(UPDATED_ID_JOKE).url(UPDATED_URL).iconUrl(UPDATED_ICON_URL).value(UPDATED_VALUE);

        restJokesUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJokesUser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedJokesUser))
            )
            .andExpect(status().isOk());

        // Validate the JokesUser in the database
        List<JokesUser> jokesUserList = jokesUserRepository.findAll();
        assertThat(jokesUserList).hasSize(databaseSizeBeforeUpdate);
        JokesUser testJokesUser = jokesUserList.get(jokesUserList.size() - 1);
        assertThat(testJokesUser.getIdJoke()).isEqualTo(UPDATED_ID_JOKE);
        assertThat(testJokesUser.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testJokesUser.getIconUrl()).isEqualTo(UPDATED_ICON_URL);
        assertThat(testJokesUser.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingJokesUser() throws Exception {
        int databaseSizeBeforeUpdate = jokesUserRepository.findAll().size();
        jokesUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJokesUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jokesUser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jokesUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the JokesUser in the database
        List<JokesUser> jokesUserList = jokesUserRepository.findAll();
        assertThat(jokesUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJokesUser() throws Exception {
        int databaseSizeBeforeUpdate = jokesUserRepository.findAll().size();
        jokesUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJokesUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jokesUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the JokesUser in the database
        List<JokesUser> jokesUserList = jokesUserRepository.findAll();
        assertThat(jokesUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJokesUser() throws Exception {
        int databaseSizeBeforeUpdate = jokesUserRepository.findAll().size();
        jokesUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJokesUserMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jokesUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JokesUser in the database
        List<JokesUser> jokesUserList = jokesUserRepository.findAll();
        assertThat(jokesUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJokesUserWithPatch() throws Exception {
        // Initialize the database
        jokesUserRepository.saveAndFlush(jokesUser);

        int databaseSizeBeforeUpdate = jokesUserRepository.findAll().size();

        // Update the jokesUser using partial update
        JokesUser partialUpdatedJokesUser = new JokesUser();
        partialUpdatedJokesUser.setId(jokesUser.getId());

        partialUpdatedJokesUser.idJoke(UPDATED_ID_JOKE);

        restJokesUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJokesUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJokesUser))
            )
            .andExpect(status().isOk());

        // Validate the JokesUser in the database
        List<JokesUser> jokesUserList = jokesUserRepository.findAll();
        assertThat(jokesUserList).hasSize(databaseSizeBeforeUpdate);
        JokesUser testJokesUser = jokesUserList.get(jokesUserList.size() - 1);
        assertThat(testJokesUser.getIdJoke()).isEqualTo(UPDATED_ID_JOKE);
        assertThat(testJokesUser.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testJokesUser.getIconUrl()).isEqualTo(DEFAULT_ICON_URL);
        assertThat(testJokesUser.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateJokesUserWithPatch() throws Exception {
        // Initialize the database
        jokesUserRepository.saveAndFlush(jokesUser);

        int databaseSizeBeforeUpdate = jokesUserRepository.findAll().size();

        // Update the jokesUser using partial update
        JokesUser partialUpdatedJokesUser = new JokesUser();
        partialUpdatedJokesUser.setId(jokesUser.getId());

        partialUpdatedJokesUser.idJoke(UPDATED_ID_JOKE).url(UPDATED_URL).iconUrl(UPDATED_ICON_URL).value(UPDATED_VALUE);

        restJokesUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJokesUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJokesUser))
            )
            .andExpect(status().isOk());

        // Validate the JokesUser in the database
        List<JokesUser> jokesUserList = jokesUserRepository.findAll();
        assertThat(jokesUserList).hasSize(databaseSizeBeforeUpdate);
        JokesUser testJokesUser = jokesUserList.get(jokesUserList.size() - 1);
        assertThat(testJokesUser.getIdJoke()).isEqualTo(UPDATED_ID_JOKE);
        assertThat(testJokesUser.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testJokesUser.getIconUrl()).isEqualTo(UPDATED_ICON_URL);
        assertThat(testJokesUser.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingJokesUser() throws Exception {
        int databaseSizeBeforeUpdate = jokesUserRepository.findAll().size();
        jokesUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJokesUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jokesUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jokesUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the JokesUser in the database
        List<JokesUser> jokesUserList = jokesUserRepository.findAll();
        assertThat(jokesUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJokesUser() throws Exception {
        int databaseSizeBeforeUpdate = jokesUserRepository.findAll().size();
        jokesUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJokesUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jokesUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the JokesUser in the database
        List<JokesUser> jokesUserList = jokesUserRepository.findAll();
        assertThat(jokesUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJokesUser() throws Exception {
        int databaseSizeBeforeUpdate = jokesUserRepository.findAll().size();
        jokesUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJokesUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jokesUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JokesUser in the database
        List<JokesUser> jokesUserList = jokesUserRepository.findAll();
        assertThat(jokesUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJokesUser() throws Exception {
        // Initialize the database
        jokesUserRepository.saveAndFlush(jokesUser);

        int databaseSizeBeforeDelete = jokesUserRepository.findAll().size();

        // Delete the jokesUser
        restJokesUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, jokesUser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JokesUser> jokesUserList = jokesUserRepository.findAll();
        assertThat(jokesUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
