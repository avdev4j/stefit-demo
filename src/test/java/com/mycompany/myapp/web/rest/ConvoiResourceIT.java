package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.StefitApp;
import com.mycompany.myapp.domain.Convoi;
import com.mycompany.myapp.repository.ConvoiRepository;
import com.mycompany.myapp.service.ConvoiService;
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
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ConvoiResource} REST controller.
 */
@SpringBootTest(classes = StefitApp.class)
public class ConvoiResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Autowired
    private ConvoiRepository convoiRepository;

    @Autowired
    private ConvoiService convoiService;

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

    private MockMvc restConvoiMockMvc;

    private Convoi convoi;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConvoiResource convoiResource = new ConvoiResource(convoiService);
        this.restConvoiMockMvc = MockMvcBuilders.standaloneSetup(convoiResource)
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
    public static Convoi createEntity(EntityManager em) {
        Convoi convoi = new Convoi()
            .label(DEFAULT_LABEL);
        return convoi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Convoi createUpdatedEntity(EntityManager em) {
        Convoi convoi = new Convoi()
            .label(UPDATED_LABEL);
        return convoi;
    }

    @BeforeEach
    public void initTest() {
        convoi = createEntity(em);
    }

    @Test
    @Transactional
    public void createConvoi() throws Exception {
        int databaseSizeBeforeCreate = convoiRepository.findAll().size();

        // Create the Convoi
        restConvoiMockMvc.perform(post("/api/convois")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(convoi)))
            .andExpect(status().isCreated());

        // Validate the Convoi in the database
        List<Convoi> convoiList = convoiRepository.findAll();
        assertThat(convoiList).hasSize(databaseSizeBeforeCreate + 1);
        Convoi testConvoi = convoiList.get(convoiList.size() - 1);
        assertThat(testConvoi.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    public void createConvoiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = convoiRepository.findAll().size();

        // Create the Convoi with an existing ID
        convoi.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConvoiMockMvc.perform(post("/api/convois")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(convoi)))
            .andExpect(status().isBadRequest());

        // Validate the Convoi in the database
        List<Convoi> convoiList = convoiRepository.findAll();
        assertThat(convoiList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConvois() throws Exception {
        // Initialize the database
        convoiRepository.saveAndFlush(convoi);

        // Get all the convoiList
        restConvoiMockMvc.perform(get("/api/convois?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(convoi.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }
    
    @Test
    @Transactional
    public void getConvoi() throws Exception {
        // Initialize the database
        convoiRepository.saveAndFlush(convoi);

        // Get the convoi
        restConvoiMockMvc.perform(get("/api/convois/{id}", convoi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(convoi.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }

    @Test
    @Transactional
    public void getNonExistingConvoi() throws Exception {
        // Get the convoi
        restConvoiMockMvc.perform(get("/api/convois/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConvoi() throws Exception {
        // Initialize the database
        convoiService.save(convoi);

        int databaseSizeBeforeUpdate = convoiRepository.findAll().size();

        // Update the convoi
        Convoi updatedConvoi = convoiRepository.findById(convoi.getId()).get();
        // Disconnect from session so that the updates on updatedConvoi are not directly saved in db
        em.detach(updatedConvoi);
        updatedConvoi
            .label(UPDATED_LABEL);

        restConvoiMockMvc.perform(put("/api/convois")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedConvoi)))
            .andExpect(status().isOk());

        // Validate the Convoi in the database
        List<Convoi> convoiList = convoiRepository.findAll();
        assertThat(convoiList).hasSize(databaseSizeBeforeUpdate);
        Convoi testConvoi = convoiList.get(convoiList.size() - 1);
        assertThat(testConvoi.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void updateNonExistingConvoi() throws Exception {
        int databaseSizeBeforeUpdate = convoiRepository.findAll().size();

        // Create the Convoi

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConvoiMockMvc.perform(put("/api/convois")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(convoi)))
            .andExpect(status().isBadRequest());

        // Validate the Convoi in the database
        List<Convoi> convoiList = convoiRepository.findAll();
        assertThat(convoiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConvoi() throws Exception {
        // Initialize the database
        convoiService.save(convoi);

        int databaseSizeBeforeDelete = convoiRepository.findAll().size();

        // Delete the convoi
        restConvoiMockMvc.perform(delete("/api/convois/{id}", convoi.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Convoi> convoiList = convoiRepository.findAll();
        assertThat(convoiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
