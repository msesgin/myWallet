package com.esgin.mywallet.web.rest;

import com.esgin.mywallet.MyWalletApp;

import com.esgin.mywallet.domain.AccountTransactions;
import com.esgin.mywallet.domain.UserAccount;
import com.esgin.mywallet.repository.AccountTransactionsRepository;
import com.esgin.mywallet.service.AccountTransactionsService;
import com.esgin.mywallet.service.UserAccountService;
import com.esgin.mywallet.service.UserService;
import com.esgin.mywallet.service.dto.AccountTransactionsDTO;
import com.esgin.mywallet.service.mapper.AccountTransactionsMapper;
import com.esgin.mywallet.service.mapper.UserAccountMapper;
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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AccountTransactionsResource REST controller.
 *
 * @see AccountTransactionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyWalletApp.class)
public class AccountTransactionsResourceIntTest {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    @Autowired
    private AccountTransactionsRepository accountTransactionsRepository;

    @Autowired
    private AccountTransactionsMapper accountTransactionsMapper;
    
    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private AccountTransactionsService accountTransactionsService;
    
    @Autowired
    private UserAccountService userAccountService;
    
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

    private MockMvc restAccountTransactionsMockMvc;

    private AccountTransactions accountTransactions;
    
    private UserAccount fromAccount;
    
    private UserAccount toAccount;

    @Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		AccountTransactionsResource accountTransactionsResource = new AccountTransactionsResource(
				accountTransactionsService, userAccountService, userService); 
		this.restAccountTransactionsMockMvc = MockMvcBuilders.standaloneSetup(accountTransactionsResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountTransactions createEntity(EntityManager em) {
        AccountTransactions accountTransactions = new AccountTransactions()
            .amount(DEFAULT_AMOUNT);
        return accountTransactions;
    }

    @Before
    public void initTest() {
        accountTransactions = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountTransactions() throws Exception {
        int databaseSizeBeforeCreate = accountTransactionsRepository.findAll().size();

        // Create the from Accounts
        fromAccount= UserAccountResourceIntTest.createEntity(em);
        fromAccount.setBalance(fromAccount.getBalance().add(DEFAULT_AMOUNT));
        fromAccount = userAccountMapper.toEntity(userAccountService.save(userAccountMapper.toDto(fromAccount)));
        
        // Create the to Accounts
        toAccount= UserAccountResourceIntTest.createEntity(em);
        toAccount.setBalance(toAccount.getBalance().subtract(DEFAULT_AMOUNT));
        toAccount = userAccountMapper.toEntity(userAccountService.save(userAccountMapper.toDto(toAccount)));
         
        // Create the security context
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "token"));
        SecurityContextHolder.setContext(securityContext);
        
        // Create the AccountTransactions
        accountTransactions.setFromAccount(fromAccount);
        accountTransactions.setToAccount(toAccount);
        AccountTransactionsDTO accountTransactionsDTO = accountTransactionsMapper.toDto(accountTransactions);
        restAccountTransactionsMockMvc.perform(post("/api/account-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountTransactionsDTO)))
            .andExpect(status().isCreated());

        // Validate the AccountTransactions in the database
        List<AccountTransactions> accountTransactionsList = accountTransactionsRepository.findAll();
        assertThat(accountTransactionsList).hasSize(databaseSizeBeforeCreate + 1);
        AccountTransactions testAccountTransactions = accountTransactionsList.get(accountTransactionsList.size() - 1);
        assertThat(testAccountTransactions.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createAccountTransactionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountTransactionsRepository.findAll().size();

        // Create the AccountTransactions with an existing ID
        accountTransactions.setId(1L);
        AccountTransactionsDTO accountTransactionsDTO = accountTransactionsMapper.toDto(accountTransactions);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountTransactionsMockMvc.perform(post("/api/account-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountTransactionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AccountTransactions> accountTransactionsList = accountTransactionsRepository.findAll();
        assertThat(accountTransactionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountTransactionsRepository.findAll().size();
        // set the field null
        accountTransactions.setAmount(null);

        // Create the AccountTransactions, which fails.
        AccountTransactionsDTO accountTransactionsDTO = accountTransactionsMapper.toDto(accountTransactions);

        restAccountTransactionsMockMvc.perform(post("/api/account-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountTransactionsDTO)))
            .andExpect(status().isBadRequest());

        List<AccountTransactions> accountTransactionsList = accountTransactionsRepository.findAll();
        assertThat(accountTransactionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAccountTransactions() throws Exception {
        // Initialize the database
        accountTransactionsRepository.saveAndFlush(accountTransactions);

        // Get all the accountTransactionsList
        restAccountTransactionsMockMvc.perform(get("/api/account-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountTransactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getAccountTransactions() throws Exception {
        // Initialize the database
        accountTransactionsRepository.saveAndFlush(accountTransactions);

        // Get the accountTransactions
        restAccountTransactionsMockMvc.perform(get("/api/account-transactions/{id}", accountTransactions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountTransactions.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAccountTransactions() throws Exception {
        // Get the accountTransactions
        restAccountTransactionsMockMvc.perform(get("/api/account-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountTransactions() throws Exception {
        // Initialize the database
        accountTransactionsRepository.saveAndFlush(accountTransactions);
        int databaseSizeBeforeUpdate = accountTransactionsRepository.findAll().size();

        // Update the accountTransactions
        AccountTransactions updatedAccountTransactions = accountTransactionsRepository.findOne(accountTransactions.getId());
        updatedAccountTransactions
            .amount(UPDATED_AMOUNT);
        AccountTransactionsDTO accountTransactionsDTO = accountTransactionsMapper.toDto(updatedAccountTransactions);

        restAccountTransactionsMockMvc.perform(put("/api/account-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountTransactionsDTO)))
            .andExpect(status().isOk());

        // Validate the AccountTransactions in the database
        List<AccountTransactions> accountTransactionsList = accountTransactionsRepository.findAll();
        assertThat(accountTransactionsList).hasSize(databaseSizeBeforeUpdate);
        AccountTransactions testAccountTransactions = accountTransactionsList.get(accountTransactionsList.size() - 1);
        assertThat(testAccountTransactions.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountTransactions() throws Exception {
        int databaseSizeBeforeUpdate = accountTransactionsRepository.findAll().size();

        // Create the from Accounts
        fromAccount= UserAccountResourceIntTest.createEntity(em);
        fromAccount.setBalance(fromAccount.getBalance().add(DEFAULT_AMOUNT));
        fromAccount = userAccountMapper.toEntity(userAccountService.save(userAccountMapper.toDto(fromAccount)));
        
        // Create the to Accounts
        toAccount= UserAccountResourceIntTest.createEntity(em);
        toAccount.setBalance(toAccount.getBalance().subtract(DEFAULT_AMOUNT));
        toAccount = userAccountMapper.toEntity(userAccountService.save(userAccountMapper.toDto(toAccount)));
         
        // Create the security context
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "token"));
        SecurityContextHolder.setContext(securityContext);
        
        
        // Create the AccountTransactions
        accountTransactions.setFromAccount(fromAccount);
        accountTransactions.setToAccount(toAccount);
        AccountTransactionsDTO accountTransactionsDTO = accountTransactionsMapper.toDto(accountTransactions);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccountTransactionsMockMvc.perform(put("/api/account-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountTransactionsDTO)))
            .andExpect(status().isCreated());

        // Validate the AccountTransactions in the database
        List<AccountTransactions> accountTransactionsList = accountTransactionsRepository.findAll();
        assertThat(accountTransactionsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccountTransactions() throws Exception {
        // Initialize the database
        accountTransactionsRepository.saveAndFlush(accountTransactions);
        int databaseSizeBeforeDelete = accountTransactionsRepository.findAll().size();

        // Get the accountTransactions
        restAccountTransactionsMockMvc.perform(delete("/api/account-transactions/{id}", accountTransactions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AccountTransactions> accountTransactionsList = accountTransactionsRepository.findAll();
        assertThat(accountTransactionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountTransactions.class);
        AccountTransactions accountTransactions1 = new AccountTransactions();
        accountTransactions1.setId(1L);
        AccountTransactions accountTransactions2 = new AccountTransactions();
        accountTransactions2.setId(accountTransactions1.getId());
        assertThat(accountTransactions1).isEqualTo(accountTransactions2);
        accountTransactions2.setId(2L);
        assertThat(accountTransactions1).isNotEqualTo(accountTransactions2);
        accountTransactions1.setId(null);
        assertThat(accountTransactions1).isNotEqualTo(accountTransactions2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountTransactionsDTO.class);
        AccountTransactionsDTO accountTransactionsDTO1 = new AccountTransactionsDTO();
        accountTransactionsDTO1.setId(1L);
        AccountTransactionsDTO accountTransactionsDTO2 = new AccountTransactionsDTO();
        assertThat(accountTransactionsDTO1).isNotEqualTo(accountTransactionsDTO2);
        accountTransactionsDTO2.setId(accountTransactionsDTO1.getId());
        assertThat(accountTransactionsDTO1).isEqualTo(accountTransactionsDTO2);
        accountTransactionsDTO2.setId(2L);
        assertThat(accountTransactionsDTO1).isNotEqualTo(accountTransactionsDTO2);
        accountTransactionsDTO1.setId(null);
        assertThat(accountTransactionsDTO1).isNotEqualTo(accountTransactionsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(accountTransactionsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(accountTransactionsMapper.fromId(null)).isNull();
    }
}
