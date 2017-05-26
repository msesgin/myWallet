package com.esgin.mywallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.esgin.mywallet.service.UserAccountService;
import com.esgin.mywallet.service.UserService;
import com.esgin.mywallet.web.rest.util.HeaderUtil;
import com.esgin.mywallet.web.rest.util.PaginationUtil;
import com.esgin.mywallet.service.dto.UserAccountDTO;
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
 * REST controller for managing UserAccount.
 */
@RestController
@RequestMapping("/api")
public class UserAccountResource {

    private final Logger log = LoggerFactory.getLogger(UserAccountResource.class);

    private static final String ENTITY_NAME = "userAccount";
        
    private final UserAccountService userAccountService;
    
    private final UserService userService;

    public UserAccountResource(UserAccountService userAccountService,  UserService userService) {
        this.userAccountService = userAccountService;
        this.userService =  userService; 
    }

    /**
     * POST  /user-accounts : Create a new userAccount.
     *
     * @param userAccountDTO the userAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userAccountDTO, or with status 400 (Bad Request) if the userAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-accounts")
    @Timed
    public ResponseEntity<UserAccountDTO> createUserAccount(@Valid @RequestBody UserAccountDTO userAccountDTO) throws URISyntaxException {
        log.debug("REST request to save UserAccount : {}", userAccountDTO);
        if (userAccountDTO.getId() != null) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new userAccount cannot already have an ID")).body(null);
        }
        userAccountDTO.setUserId(userService.getUserWithAuthorities().getId());
        UserAccountDTO result = userAccountService.save(userAccountDTO);
        return ResponseEntity.created(new URI("/api/user-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-accounts : Updates an existing userAccount.
     *
     * @param userAccountDTO the userAccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userAccountDTO,
     * or with status 400 (Bad Request) if the userAccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the userAccountDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-accounts")
    @Timed
    public ResponseEntity<UserAccountDTO> updateUserAccount(@Valid @RequestBody UserAccountDTO userAccountDTO) throws URISyntaxException {
        log.debug("REST request to update UserAccount : {}", userAccountDTO);
        if (userAccountDTO.getId() == null) {
            return createUserAccount(userAccountDTO);
        }
        UserAccountDTO result = userAccountService.save(userAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-accounts : get all the userAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userAccounts in body
     */
    @GetMapping("/user-accounts")
    @Timed
    public ResponseEntity<List<UserAccountDTO>> getAllUserAccounts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of UserAccounts");
        Page<UserAccountDTO> page = userAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /user-accounts : get all the userAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userAccounts in body
     */
    @GetMapping("/user-accounts-current-user")
    @Timed
    public ResponseEntity<List<UserAccountDTO>> getAllUserAccountsByUserIsCurrentUser(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of UserAccounts");
        Page<UserAccountDTO> page = userAccountService.findByUserIsCurrentUser(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /user-accounts : get all the userAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userAccounts in body
     */
    @GetMapping("/user-accounts-user/{userId}")
    @Timed 
    public ResponseEntity<List<UserAccountDTO>> getAllUserAccountsByUser(@PathVariable Long userId) {
        log.debug("REST request to get a page of UserAccounts");
        List<UserAccountDTO> userList = userAccountService.findByUser(userId);
       return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userList));
    }

    /**
     * GET  /user-accounts/:id : get the "id" userAccount.
     *
     * @param id the id of the userAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userAccountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-accounts/{id}")
    @Timed
    public ResponseEntity<UserAccountDTO> getUserAccount(@PathVariable Long id) {
        log.debug("REST request to get UserAccount : {}", id);
        UserAccountDTO userAccountDTO = userAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userAccountDTO));
    }

    /**
     * DELETE  /user-accounts/:id : delete the "id" userAccount.
     *
     * @param id the id of the userAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserAccount(@PathVariable Long id) {
        log.debug("REST request to delete UserAccount : {}", id);
        userAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
