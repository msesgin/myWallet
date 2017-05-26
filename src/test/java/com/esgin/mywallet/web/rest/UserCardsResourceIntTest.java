package com.esgin.mywallet.web.rest;

import com.esgin.mywallet.MyWalletApp;

import com.esgin.mywallet.domain.UserCards;
import com.esgin.mywallet.repository.UserCardsRepository;
import com.esgin.mywallet.service.UserCardsService;
import com.esgin.mywallet.service.UserService;
import com.esgin.mywallet.service.dto.UserCardsDTO;
import com.esgin.mywallet.service.mapper.UserCardsMapper;
import com.esgin.mywallet.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserCardsResource REST controller.
 *
 * @see UserCardsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyWalletApp.class)
public class UserCardsResourceIntTest {

    private static final String DEFAULT_CREDIT_CARD_NUMBER = "1232132132132321";
    private static final String UPDATED_CREDIT_CARD_NUMBER = "2222222222222222";

    private static final String DEFAULT_LAST_USAGE_MONTH = "12";
    private static final String UPDATED_LAST_USAGE_MONTH = "21";

    private static final String DEFAULT_LAST_USAGE_YEAR = "22";
    private static final String UPDATED_LAST_USAGE_YEAR = "23";

    private static final String DEFAULT_CVV = "234";
    private static final String UPDATED_CVV = "345";

    @Autowired
    private UserCardsRepository userCardsRepository;

    @Autowired
    private UserCardsMapper userCardsMapper;

    @Autowired
    private UserCardsService userCardsService;

    @Autowired
    private UserService userService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserCardsMockMvc;

    private UserCards userCards;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserCardsResource userCardsResource = new UserCardsResource(userCardsService, userService);
        this.restUserCardsMockMvc = MockMvcBuilders.standaloneSetup(userCardsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserCards createEntity(EntityManager em) {
        UserCards userCards = new UserCards()
            .creditCardNumber(DEFAULT_CREDIT_CARD_NUMBER)
            .lastUsageMonth(DEFAULT_LAST_USAGE_MONTH)
            .lastUsageYear(DEFAULT_LAST_USAGE_YEAR)
            .cvv(DEFAULT_CVV);
        return userCards;
    }

    @Before
    public void initTest() {
        userCards = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserCards() throws Exception {
        int databaseSizeBeforeCreate = userCardsRepository.findAll().size();

        // Create the security context
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "token"));
        SecurityContextHolder.setContext(securityContext);
        
        // Create the UserCards
        UserCardsDTO userCardsDTO = userCardsMapper.toDto(userCards);
        restUserCardsMockMvc.perform(post("/api/user-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userCardsDTO)))
            .andExpect(status().isCreated());

        // Validate the UserCards in the database
        List<UserCards> userCardsList = userCardsRepository.findAll();
        assertThat(userCardsList).hasSize(databaseSizeBeforeCreate + 1);
        UserCards testUserCards = userCardsList.get(userCardsList.size() - 1);
        assertThat(testUserCards.getCreditCardNumber()).isEqualTo(DEFAULT_CREDIT_CARD_NUMBER);
        assertThat(testUserCards.getLastUsageMonth()).isEqualTo(DEFAULT_LAST_USAGE_MONTH);
        assertThat(testUserCards.getLastUsageYear()).isEqualTo(DEFAULT_LAST_USAGE_YEAR);
        assertThat(testUserCards.getCvv()).isEqualTo(DEFAULT_CVV);
    }

    @Test
    @Transactional
    public void createUserCardsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userCardsRepository.findAll().size();

        // Create the UserCards with an existing ID
        userCards.setId(1L);
        UserCardsDTO userCardsDTO = userCardsMapper.toDto(userCards);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserCardsMockMvc.perform(post("/api/user-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userCardsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserCards> userCardsList = userCardsRepository.findAll();
        assertThat(userCardsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreditCardNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCardsRepository.findAll().size();
        // set the field null
        userCards.setCreditCardNumber(null);

        // Create the UserCards, which fails.
        UserCardsDTO userCardsDTO = userCardsMapper.toDto(userCards);

        restUserCardsMockMvc.perform(post("/api/user-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userCardsDTO)))
            .andExpect(status().isBadRequest());

        List<UserCards> userCardsList = userCardsRepository.findAll();
        assertThat(userCardsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastUsageMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCardsRepository.findAll().size();
        // set the field null
        userCards.setLastUsageMonth(null);

        // Create the UserCards, which fails.
        UserCardsDTO userCardsDTO = userCardsMapper.toDto(userCards);

        restUserCardsMockMvc.perform(post("/api/user-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userCardsDTO)))
            .andExpect(status().isBadRequest());

        List<UserCards> userCardsList = userCardsRepository.findAll();
        assertThat(userCardsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastUsageYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCardsRepository.findAll().size();
        // set the field null
        userCards.setLastUsageYear(null);

        // Create the UserCards, which fails.
        UserCardsDTO userCardsDTO = userCardsMapper.toDto(userCards);

        restUserCardsMockMvc.perform(post("/api/user-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userCardsDTO)))
            .andExpect(status().isBadRequest());

        List<UserCards> userCardsList = userCardsRepository.findAll();
        assertThat(userCardsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCvvIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCardsRepository.findAll().size();
        // set the field null
        userCards.setCvv(null);

        // Create the UserCards, which fails.
        UserCardsDTO userCardsDTO = userCardsMapper.toDto(userCards);

        restUserCardsMockMvc.perform(post("/api/user-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userCardsDTO)))
            .andExpect(status().isBadRequest());

        List<UserCards> userCardsList = userCardsRepository.findAll();
        assertThat(userCardsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserCards() throws Exception {
        // Initialize the database
        userCardsRepository.saveAndFlush(userCards);

        // Get all the userCardsList
        restUserCardsMockMvc.perform(get("/api/user-cards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userCards.getId().intValue())))
            .andExpect(jsonPath("$.[*].creditCardNumber").value(hasItem(DEFAULT_CREDIT_CARD_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].lastUsageMonth").value(hasItem(DEFAULT_LAST_USAGE_MONTH.toString())))
            .andExpect(jsonPath("$.[*].lastUsageYear").value(hasItem(DEFAULT_LAST_USAGE_YEAR.toString())))
            .andExpect(jsonPath("$.[*].cvv").value(hasItem(DEFAULT_CVV.toString())));
    }

    @Test
    @Transactional
    public void getUserCards() throws Exception {
        // Initialize the database
        userCardsRepository.saveAndFlush(userCards);

        // Get the userCards
        restUserCardsMockMvc.perform(get("/api/user-cards/{id}", userCards.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userCards.getId().intValue()))
            .andExpect(jsonPath("$.creditCardNumber").value(DEFAULT_CREDIT_CARD_NUMBER.toString()))
            .andExpect(jsonPath("$.lastUsageMonth").value(DEFAULT_LAST_USAGE_MONTH.toString()))
            .andExpect(jsonPath("$.lastUsageYear").value(DEFAULT_LAST_USAGE_YEAR.toString()))
            .andExpect(jsonPath("$.cvv").value(DEFAULT_CVV.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserCards() throws Exception {
        // Get the userCards
        restUserCardsMockMvc.perform(get("/api/user-cards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserCards() throws Exception {
        // Initialize the database
        userCardsRepository.saveAndFlush(userCards);
        int databaseSizeBeforeUpdate = userCardsRepository.findAll().size();

        // Update the userCards
        UserCards updatedUserCards = userCardsRepository.findOne(userCards.getId());
        updatedUserCards
            .creditCardNumber(UPDATED_CREDIT_CARD_NUMBER)
            .lastUsageMonth(UPDATED_LAST_USAGE_MONTH)
            .lastUsageYear(UPDATED_LAST_USAGE_YEAR)
            .cvv(UPDATED_CVV);
        UserCardsDTO userCardsDTO = userCardsMapper.toDto(updatedUserCards);

        restUserCardsMockMvc.perform(put("/api/user-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userCardsDTO)))
            .andExpect(status().isOk());

        // Validate the UserCards in the database
        List<UserCards> userCardsList = userCardsRepository.findAll();
        assertThat(userCardsList).hasSize(databaseSizeBeforeUpdate);
        UserCards testUserCards = userCardsList.get(userCardsList.size() - 1);
        assertThat(testUserCards.getCreditCardNumber()).isEqualTo(UPDATED_CREDIT_CARD_NUMBER);
        assertThat(testUserCards.getLastUsageMonth()).isEqualTo(UPDATED_LAST_USAGE_MONTH);
        assertThat(testUserCards.getLastUsageYear()).isEqualTo(UPDATED_LAST_USAGE_YEAR);
        assertThat(testUserCards.getCvv()).isEqualTo(UPDATED_CVV);
    }

    @Test
    @Transactional
    public void updateNonExistingUserCards() throws Exception {
        int databaseSizeBeforeUpdate = userCardsRepository.findAll().size();

        // Create the security context
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "token"));
        SecurityContextHolder.setContext(securityContext);
        
        // Create the UserCards
        UserCardsDTO userCardsDTO = userCardsMapper.toDto(userCards);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserCardsMockMvc.perform(put("/api/user-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userCardsDTO)))
            .andExpect(status().isCreated());

        // Validate the UserCards in the database
        List<UserCards> userCardsList = userCardsRepository.findAll();
        assertThat(userCardsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserCards() throws Exception {
        // Initialize the database
        userCardsRepository.saveAndFlush(userCards);
        int databaseSizeBeforeDelete = userCardsRepository.findAll().size();

        // Get the userCards
        restUserCardsMockMvc.perform(delete("/api/user-cards/{id}", userCards.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserCards> userCardsList = userCardsRepository.findAll();
        assertThat(userCardsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserCards.class);
        UserCards userCards1 = new UserCards();
        userCards1.setId(1L);
        UserCards userCards2 = new UserCards();
        userCards2.setId(userCards1.getId());
        assertThat(userCards1).isEqualTo(userCards2);
        userCards2.setId(2L);
        assertThat(userCards1).isNotEqualTo(userCards2);
        userCards1.setId(null);
        assertThat(userCards1).isNotEqualTo(userCards2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserCardsDTO.class);
        UserCardsDTO userCardsDTO1 = new UserCardsDTO();
        userCardsDTO1.setId(1L);
        UserCardsDTO userCardsDTO2 = new UserCardsDTO();
        assertThat(userCardsDTO1).isNotEqualTo(userCardsDTO2);
        userCardsDTO2.setId(userCardsDTO1.getId());
        assertThat(userCardsDTO1).isEqualTo(userCardsDTO2);
        userCardsDTO2.setId(2L);
        assertThat(userCardsDTO1).isNotEqualTo(userCardsDTO2);
        userCardsDTO1.setId(null);
        assertThat(userCardsDTO1).isNotEqualTo(userCardsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userCardsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userCardsMapper.fromId(null)).isNull();
    }
}
