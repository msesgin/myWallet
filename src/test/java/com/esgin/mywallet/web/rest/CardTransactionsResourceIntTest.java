package com.esgin.mywallet.web.rest;

import com.esgin.mywallet.MyWalletApp;

import com.esgin.mywallet.domain.CardTransactions;
import com.esgin.mywallet.domain.UserAccount;
import com.esgin.mywallet.domain.UserCards;
import com.esgin.mywallet.repository.CardTransactionsRepository;
import com.esgin.mywallet.service.CardTransactionsService;
import com.esgin.mywallet.service.UserAccountService;
import com.esgin.mywallet.service.UserCardsService;
import com.esgin.mywallet.service.dto.CardTransactionsDTO;
import com.esgin.mywallet.service.mapper.CardTransactionsMapper;
import com.esgin.mywallet.service.mapper.UserAccountMapper;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CardTransactionsResource REST controller.
 *
 * @see CardTransactionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyWalletApp.class)
public class CardTransactionsResourceIntTest {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_CREDIT_CARD_NUMBER = "1232132132132321";

    
    @Autowired
    private CardTransactionsRepository cardTransactionsRepository;

    @Autowired
    private CardTransactionsMapper cardTransactionsMapper;
    
    @Autowired
    private UserCardsMapper userCardsMapper;
    
    @Autowired
    private UserAccountMapper userAccountMapper;
    
    @Autowired
    private CardTransactionsService cardTransactionsService;
    
    @Autowired
    private UserCardsService userCardsService;
    
    @Autowired
    private UserAccountService userAccountService;
    
    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCardTransactionsMockMvc;
    
    private CardTransactions cardTransactions;
    
    private UserCards userCards;
    
    private UserAccount userAccount;
    

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CardTransactionsResource cardTransactionsResource = new CardTransactionsResource(cardTransactionsService, userCardsService, userAccountService);
        this.restCardTransactionsMockMvc = MockMvcBuilders.standaloneSetup(cardTransactionsResource)
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
    public static CardTransactions createEntity(EntityManager em) {
        CardTransactions cardTransactions = new CardTransactions()
            .amount(DEFAULT_AMOUNT);
        return cardTransactions;
    }

    @Before
    public void initTest() {
        cardTransactions = createEntity(em);
    }

    @Test
    @Transactional
    public void createCardTransactions() throws Exception {
        int databaseSizeBeforeCreate = cardTransactionsRepository.findAll().size();
        
        // Create the User Accounts
        userAccount= UserAccountResourceIntTest.createEntity(em);
        userAccount = userAccountMapper.toEntity(userAccountService.save(userAccountMapper.toDto(userAccount)));
        
        // Create the User Cards
        userCards = UserCardsResourceIntTest.createEntity(em);
        userCards.setToAccount(userAccount);
        userCardsService.save(userCardsMapper.toDto(userCards));
        
        // Create the CardTransactions
        userCards = userCardsMapper.toEntity(userCardsService.findByCreditCardNumber(DEFAULT_CREDIT_CARD_NUMBER));
		cardTransactions.creditCardNumber(userCards);
        CardTransactionsDTO cardTransactionsDTO = cardTransactionsMapper.toDto(cardTransactions);
        restCardTransactionsMockMvc.perform(post("/api/card-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardTransactionsDTO)))
            .andExpect(status().isCreated());

        // Validate the CardTransactions in the database
        List<CardTransactions> cardTransactionsList = cardTransactionsRepository.findAll();
        assertThat(cardTransactionsList).hasSize(databaseSizeBeforeCreate + 1);
        CardTransactions testCardTransactions = cardTransactionsList.get(cardTransactionsList.size() - 1);
        assertThat(testCardTransactions.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createCardTransactionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cardTransactionsRepository.findAll().size();

        // Create the CardTransactions with an existing ID
        cardTransactions.setId(1L);
        CardTransactionsDTO cardTransactionsDTO = cardTransactionsMapper.toDto(cardTransactions);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardTransactionsMockMvc.perform(post("/api/card-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardTransactionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CardTransactions> cardTransactionsList = cardTransactionsRepository.findAll();
        assertThat(cardTransactionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardTransactionsRepository.findAll().size();
        // set the field null
        cardTransactions.setAmount(null);

        // Create the CardTransactions, which fails.
        CardTransactionsDTO cardTransactionsDTO = cardTransactionsMapper.toDto(cardTransactions);

        restCardTransactionsMockMvc.perform(post("/api/card-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardTransactionsDTO)))
            .andExpect(status().isBadRequest());

        List<CardTransactions> cardTransactionsList = cardTransactionsRepository.findAll();
        assertThat(cardTransactionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCardTransactions() throws Exception {
        // Initialize the database
        cardTransactionsRepository.saveAndFlush(cardTransactions);

        // Get all the cardTransactionsList
        restCardTransactionsMockMvc.perform(get("/api/card-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardTransactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getCardTransactions() throws Exception {
        // Initialize the database
        cardTransactionsRepository.saveAndFlush(cardTransactions);

        // Get the cardTransactions
        restCardTransactionsMockMvc.perform(get("/api/card-transactions/{id}", cardTransactions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cardTransactions.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCardTransactions() throws Exception {
        // Get the cardTransactions
        restCardTransactionsMockMvc.perform(get("/api/card-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCardTransactions() throws Exception {
        // Initialize the database
        cardTransactionsRepository.saveAndFlush(cardTransactions);
        int databaseSizeBeforeUpdate = cardTransactionsRepository.findAll().size();

        // Update the cardTransactions
        CardTransactions updatedCardTransactions = cardTransactionsRepository.findOne(cardTransactions.getId());
        updatedCardTransactions
            .amount(UPDATED_AMOUNT);
        CardTransactionsDTO cardTransactionsDTO = cardTransactionsMapper.toDto(updatedCardTransactions);

        restCardTransactionsMockMvc.perform(put("/api/card-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardTransactionsDTO)))
            .andExpect(status().isOk());

        // Validate the CardTransactions in the database
        List<CardTransactions> cardTransactionsList = cardTransactionsRepository.findAll();
        assertThat(cardTransactionsList).hasSize(databaseSizeBeforeUpdate);
        CardTransactions testCardTransactions = cardTransactionsList.get(cardTransactionsList.size() - 1);
        assertThat(testCardTransactions.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingCardTransactions() throws Exception {
        int databaseSizeBeforeUpdate = cardTransactionsRepository.findAll().size();

        
        // Create the User Accounts
        userAccount= UserAccountResourceIntTest.createEntity(em);
        userAccount = userAccountMapper.toEntity(userAccountService.save(userAccountMapper.toDto(userAccount)));
        
        // Create the User Cards
        userCards = UserCardsResourceIntTest.createEntity(em);
        userCards.setToAccount(userAccount);
        userCardsService.save(userCardsMapper.toDto(userCards));
        
        // Create the CardTransactions
        userCards = userCardsMapper.toEntity(userCardsService.findByCreditCardNumber(DEFAULT_CREDIT_CARD_NUMBER));
		cardTransactions.creditCardNumber(userCards);
        // Create the CardTransactions
        CardTransactionsDTO cardTransactionsDTO = cardTransactionsMapper.toDto(cardTransactions);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCardTransactionsMockMvc.perform(put("/api/card-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardTransactionsDTO)))
            .andExpect(status().isCreated());

        // Validate the CardTransactions in the database
        List<CardTransactions> cardTransactionsList = cardTransactionsRepository.findAll();
        assertThat(cardTransactionsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCardTransactions() throws Exception {
        // Initialize the database
        cardTransactionsRepository.saveAndFlush(cardTransactions);
        int databaseSizeBeforeDelete = cardTransactionsRepository.findAll().size();

        // Get the cardTransactions
        restCardTransactionsMockMvc.perform(delete("/api/card-transactions/{id}", cardTransactions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CardTransactions> cardTransactionsList = cardTransactionsRepository.findAll();
        assertThat(cardTransactionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardTransactions.class);
        CardTransactions cardTransactions1 = new CardTransactions();
        cardTransactions1.setId(1L);
        CardTransactions cardTransactions2 = new CardTransactions();
        cardTransactions2.setId(cardTransactions1.getId());
        assertThat(cardTransactions1).isEqualTo(cardTransactions2);
        cardTransactions2.setId(2L);
        assertThat(cardTransactions1).isNotEqualTo(cardTransactions2);
        cardTransactions1.setId(null);
        assertThat(cardTransactions1).isNotEqualTo(cardTransactions2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardTransactionsDTO.class);
        CardTransactionsDTO cardTransactionsDTO1 = new CardTransactionsDTO();
        cardTransactionsDTO1.setId(1L);
        CardTransactionsDTO cardTransactionsDTO2 = new CardTransactionsDTO();
        assertThat(cardTransactionsDTO1).isNotEqualTo(cardTransactionsDTO2);
        cardTransactionsDTO2.setId(cardTransactionsDTO1.getId());
        assertThat(cardTransactionsDTO1).isEqualTo(cardTransactionsDTO2);
        cardTransactionsDTO2.setId(2L);
        assertThat(cardTransactionsDTO1).isNotEqualTo(cardTransactionsDTO2);
        cardTransactionsDTO1.setId(null);
        assertThat(cardTransactionsDTO1).isNotEqualTo(cardTransactionsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cardTransactionsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cardTransactionsMapper.fromId(null)).isNull();
    }
}
