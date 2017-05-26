package com.esgin.mywallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.esgin.mywallet.service.AccountTransactionsService;
import com.esgin.mywallet.service.UserAccountService;
import com.esgin.mywallet.service.UserService;
import com.esgin.mywallet.web.rest.util.HeaderUtil;
import com.esgin.mywallet.web.rest.util.PaginationUtil;
import com.esgin.mywallet.service.dto.AccountTransactionsDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AccountTransactions.
 */
@RestController
@RequestMapping("/api")
public class AccountTransactionsResource {

    private final Logger log = LoggerFactory.getLogger(AccountTransactionsResource.class);

    private static final String ENTITY_NAME = "accountTransactions";
        
    private final AccountTransactionsService accountTransactionsService;
    
    private final UserAccountService userAccountService;
    
    private final UserService userService;

    public AccountTransactionsResource(AccountTransactionsService accountTransactionsService,
    		UserAccountService userAccountService, UserService userService) {
        this.accountTransactionsService = accountTransactionsService;
        this.userAccountService = userAccountService;
        this.userService = userService;
    }

    /**
     * POST  /account-transactions : Create a new accountTransactions.
     *
     * @param accountTransactionsDTO the accountTransactionsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountTransactionsDTO, or with status 400 (Bad Request) if the accountTransactions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/account-transactions")
    @Timed
	public ResponseEntity<AccountTransactionsDTO> createAccountTransactions(
			@Valid @RequestBody AccountTransactionsDTO accountTransactionsDTO) throws URISyntaxException {
		try {

			log.debug("REST request to save AccountTransactions : {}", accountTransactionsDTO);
			if (accountTransactionsDTO.getId() != null) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
						"A new accountTransactions cannot already have an ID")).body(null);
			}

			userAccountService.removeMoney(accountTransactionsDTO.getFromAccountId(),
					accountTransactionsDTO.getAmount());
			
			userAccountService.addMoney(accountTransactionsDTO.getToAccountId(),
					accountTransactionsDTO.getAmount());

			accountTransactionsDTO.setFromUserEmailId(userService.getUserWithAuthorities().getId());
			AccountTransactionsDTO result = accountTransactionsService.save(accountTransactionsDTO);

			return ResponseEntity.created(new URI("/api/account-transactions/" + result.getId()))
					.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
		} catch (Exception e) {
			log.error("Exception transferring money: ", e);
			return ResponseEntity.badRequest()
	                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "negativeBalance", "Account Balance cannot be negative"))
	                .body(null);
		}
	}

    /**
     * PUT  /account-transactions : Updates an existing accountTransactions.
     *
     * @param accountTransactionsDTO the accountTransactionsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountTransactionsDTO,
     * or with status 400 (Bad Request) if the accountTransactionsDTO is not valid,
     * or with status 500 (Internal Server Error) if the accountTransactionsDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/account-transactions")
    @Timed
    public ResponseEntity<AccountTransactionsDTO> updateAccountTransactions(@Valid @RequestBody AccountTransactionsDTO accountTransactionsDTO) throws URISyntaxException {
        log.debug("REST request to update AccountTransactions : {}", accountTransactionsDTO);
        if (accountTransactionsDTO.getId() == null) {
            return createAccountTransactions(accountTransactionsDTO);
        }
        AccountTransactionsDTO result = accountTransactionsService.save(accountTransactionsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountTransactionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /account-transactions : get all the accountTransactions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountTransactions in body
     */
    @GetMapping("/account-transactions")
    @Timed
    public ResponseEntity<List<AccountTransactionsDTO>> getAllAccountTransactions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of AccountTransactions");
        Page<AccountTransactionsDTO> page = accountTransactionsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/account-transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /account-transactions/:id : get the "id" accountTransactions.
     *
     * @param id the id of the accountTransactionsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountTransactionsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/account-transactions/{id}")
    @Timed
    public ResponseEntity<AccountTransactionsDTO> getAccountTransactions(@PathVariable Long id) {
        log.debug("REST request to get AccountTransactions : {}", id);
        AccountTransactionsDTO accountTransactionsDTO = accountTransactionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accountTransactionsDTO));
    }

    /**
     * DELETE  /account-transactions/:id : delete the "id" accountTransactions.
     *
     * @param id the id of the accountTransactionsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/account-transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountTransactions(@PathVariable Long id) {
        log.debug("REST request to delete AccountTransactions : {}", id);
        accountTransactionsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
