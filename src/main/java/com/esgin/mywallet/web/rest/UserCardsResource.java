package com.esgin.mywallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.esgin.mywallet.service.UserCardsService;
import com.esgin.mywallet.service.UserService;
import com.esgin.mywallet.web.rest.util.HeaderUtil;
import com.esgin.mywallet.web.rest.util.PaginationUtil;
import com.esgin.mywallet.service.dto.UserCardsDTO;
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
 * REST controller for managing UserCards.
 */
@RestController
@RequestMapping("/api")
public class UserCardsResource {

    private final Logger log = LoggerFactory.getLogger(UserCardsResource.class);

    private static final String ENTITY_NAME = "userCards";
        
    private final UserCardsService userCardsService;

    private final UserService userService;

    public UserCardsResource(UserCardsService userCardsService,  UserService userService) {
        this.userCardsService = userCardsService;
        this.userService =  userService;
    }

    /**
     * POST  /user-cards : Create a new userCards.
     *
     * @param userCardsDTO the userCardsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userCardsDTO, or with status 400 (Bad Request) if the userCards has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-cards")
    @Timed
    public ResponseEntity<UserCardsDTO> createUserCards(@Valid @RequestBody UserCardsDTO userCardsDTO) throws URISyntaxException {
        log.debug("REST request to save UserCards : {}", userCardsDTO);
        if (userCardsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new userCards cannot already have an ID")).body(null);
        }
        userCardsDTO.setUserId(userService.getUserWithAuthorities().getId());
        if(Integer.parseInt(userCardsDTO.getLastUsageMonth())>12){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "lastUsageMonthMax",
					"Last Usage Month cannot be more than 12")).body(null);
        }
        UserCardsDTO result = userCardsService.save(userCardsDTO);
        return ResponseEntity.created(new URI("/api/user-cards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-cards : Updates an existing userCards.
     *
     * @param userCardsDTO the userCardsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userCardsDTO,
     * or with status 400 (Bad Request) if the userCardsDTO is not valid,
     * or with status 500 (Internal Server Error) if the userCardsDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-cards")
    @Timed
    public ResponseEntity<UserCardsDTO> updateUserCards(@Valid @RequestBody UserCardsDTO userCardsDTO) throws URISyntaxException {
        log.debug("REST request to update UserCards : {}", userCardsDTO);
        if (userCardsDTO.getId() == null) {
            return createUserCards(userCardsDTO);
        }
        UserCardsDTO result = userCardsService.save(userCardsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userCardsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-cards : get all the userCards.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userCards in body
     */
    @GetMapping("/user-cards")
    @Timed
    public ResponseEntity<List<UserCardsDTO>> getAllUserCards(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of UserCards");
        Page<UserCardsDTO> page = userCardsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-cards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-cards/:id : get the "id" userCards.
     *
     * @param id the id of the userCardsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userCardsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-cards/{id}")
    @Timed
    public ResponseEntity<UserCardsDTO> getUserCards(@PathVariable Long id) {
        log.debug("REST request to get UserCards : {}", id);
        UserCardsDTO userCardsDTO = userCardsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userCardsDTO));
    }

    /**
     * DELETE  /user-cards/:id : delete the "id" userCards.
     *
     * @param id the id of the userCardsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-cards/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserCards(@PathVariable Long id) {
        log.debug("REST request to delete UserCards : {}", id);
        userCardsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
