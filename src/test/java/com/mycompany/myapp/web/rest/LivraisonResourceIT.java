package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.StefitApp;
import com.mycompany.myapp.domain.Livraison;
import com.mycompany.myapp.repository.LivraisonRepository;
import com.mycompany.myapp.service.LivraisonService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.StatutLivraison;
/**
 * Integration tests for the {@link LivraisonResource} REST controller.
 */
@SpringBootTest(classes = StefitApp.class)
public class LivraisonResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Float DEFAULT_POIDS = 1F;
    private static final Float UPDATED_POIDS = 2F;

    private static final LocalDate DEFAULT_DATELIVRAISON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATELIVRAISON = LocalDate.now(ZoneId.systemDefault());

    private static final StatutLivraison DEFAULT_STATUS = StatutLivraison.EN_PREPARATION;
    private static final StatutLivraison UPDATED_STATUS = StatutLivraison.PRET;

    @Autowired
    private LivraisonRepository livraisonRepository;

    @Autowired
    private LivraisonService livraisonService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restLivraisonMockMvc;

    private Livraison livraison;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LivraisonResource livraisonResource = new LivraisonResource(livraisonService);
        this.restLivraisonMockMvc = MockMvcBuilders.standaloneSetup(livraisonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livraison createEntity(EntityManager em) {
        Livraison livraison = new Livraison()
            .code(DEFAULT_CODE)
            .poids(DEFAULT_POIDS)
            .datelivraison(DEFAULT_DATELIVRAISON)
            .status(DEFAULT_STATUS);
        return livraison;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livraison createUpdatedEntity(EntityManager em) {
        Livraison livraison = new Livraison()
            .code(UPDATED_CODE)
            .poids(UPDATED_POIDS)
            .datelivraison(UPDATED_DATELIVRAISON)
            .status(UPDATED_STATUS);
        return livraison;
    }

    @BeforeEach
    public void initTest() {
        livraison = createEntity(em);
    }

    @Test
    @Transactional
    public void createLivraison() throws Exception {
        int databaseSizeBeforeCreate = livraisonRepository.findAll().size();

        // Create the Livraison
        restLivraisonMockMvc.perform(post("/api/livraisons")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(livraison)))
            .andExpect(status().isCreated());

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeCreate + 1);
        Livraison testLivraison = livraisonList.get(livraisonList.size() - 1);
        assertThat(testLivraison.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testLivraison.getPoids()).isEqualTo(DEFAULT_POIDS);
        assertThat(testLivraison.getDatelivraison()).isEqualTo(DEFAULT_DATELIVRAISON);
        assertThat(testLivraison.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createLivraisonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = livraisonRepository.findAll().size();

        // Create the Livraison with an existing ID
        livraison.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLivraisonMockMvc.perform(post("/api/livraisons")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(livraison)))
            .andExpect(status().isBadRequest());

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLivraisons() throws Exception {
        // Initialize the database
        livraisonRepository.saveAndFlush(livraison);

        // Get all the livraisonList
        restLivraisonMockMvc.perform(get("/api/livraisons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livraison.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].poids").value(hasItem(DEFAULT_POIDS.doubleValue())))
            .andExpect(jsonPath("$.[*].datelivraison").value(hasItem(DEFAULT_DATELIVRAISON.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getLivraison() throws Exception {
        // Initialize the database
        livraisonRepository.saveAndFlush(livraison);

        // Get the livraison
        restLivraisonMockMvc.perform(get("/api/livraisons/{id}", livraison.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(livraison.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.poids").value(DEFAULT_POIDS.doubleValue()))
            .andExpect(jsonPath("$.datelivraison").value(DEFAULT_DATELIVRAISON.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLivraison() throws Exception {
        // Get the livraison
        restLivraisonMockMvc.perform(get("/api/livraisons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLivraison() throws Exception {
        // Initialize the database
        livraisonService.save(livraison);

        int databaseSizeBeforeUpdate = livraisonRepository.findAll().size();

        // Update the livraison
        Livraison updatedLivraison = livraisonRepository.findById(livraison.getId()).get();
        // Disconnect from session so that the updates on updatedLivraison are not directly saved in db
        em.detach(updatedLivraison);
        updatedLivraison
            .code(UPDATED_CODE)
            .poids(UPDATED_POIDS)
            .datelivraison(UPDATED_DATELIVRAISON)
            .status(UPDATED_STATUS);

        restLivraisonMockMvc.perform(put("/api/livraisons")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLivraison)))
            .andExpect(status().isOk());

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
        Livraison testLivraison = livraisonList.get(livraisonList.size() - 1);
        assertThat(testLivraison.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testLivraison.getPoids()).isEqualTo(UPDATED_POIDS);
        assertThat(testLivraison.getDatelivraison()).isEqualTo(UPDATED_DATELIVRAISON);
        assertThat(testLivraison.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().size();

        // Create the Livraison

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivraisonMockMvc.perform(put("/api/livraisons")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(livraison)))
            .andExpect(status().isBadRequest());

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLivraison() throws Exception {
        // Initialize the database
        livraisonService.save(livraison);

        int databaseSizeBeforeDelete = livraisonRepository.findAll().size();

        // Delete the livraison
        restLivraisonMockMvc.perform(delete("/api/livraisons/{id}", livraison.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
