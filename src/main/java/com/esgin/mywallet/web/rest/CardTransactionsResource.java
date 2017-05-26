package com.esgin.mywallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.esgin.mywallet.service.CardTransactionsService;
import com.esgin.mywallet.service.UserAccountService;
import com.esgin.mywallet.service.UserCardsService;
import com.esgin.mywallet.web.rest.util.HeaderUtil;
import com.esgin.mywallet.web.rest.util.PaginationUtil;
import com.esgin.mywallet.service.dto.CardTransactionsDTO;
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
 * REST controller for managing CardTransactions.
 */
@RestController
@RequestMapping("/api")
public class CardTransactionsResource {

    private final Logger log = LoggerFactory.getLogger(CardTransactionsResource.class);

    private static final String ENTITY_NAME = "cardTransactions";
        
    private final CardTransactionsService cardTransactionsService;
    
    private final UserCardsService userCardsService;
    
    private final UserAccountService userAccountService;

    public CardTransactionsResource(CardTransactionsService cardTransactionsService, UserCardsService userCardsService, UserAccountService userAccountService) {
        this.cardTransactionsService = cardTransactionsService;
        this.userCardsService = userCardsService;
        this.userAccountService = userAccountService;
    }

    /**
     * POST  /card-transactions : Create a new cardTransactions.
     *
     * @param cardTransactionsDTO the cardTransactionsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cardTransactionsDTO, or with status 400 (Bad Request) if the cardTransactions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/card-transactions")
    @Timed
    public ResponseEntity<CardTransactionsDTO> createCardTransactions(@Valid @RequestBody CardTransactionsDTO cardTransactionsDTO) throws URISyntaxException {
        log.debug("REST request to save CardTransactions : {}", cardTransactionsDTO);
        if (cardTransactionsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cardTransactions cannot already have an ID")).body(null);
        }
        UserCardsDTO userCards = userCardsService.findOne(cardTransactionsDTO.getCreditCardNumberId());
        userAccountService.addMoney(userCards.getToAccountId(), cardTransactionsDTO.getAmount());
        
        CardTransactionsDTO result = cardTransactionsService.save(cardTransactionsDTO);
        return ResponseEntity.created(new URI("/api/card-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /card-transactions : Updates an existing cardTransactions.
     *
     * @param cardTransactionsDTO the cardTransactionsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cardTransactionsDTO,
     * or with status 400 (Bad Request) if the cardTransactionsDTO is not valid,
     * or with status 500 (Internal Server Error) if the cardTransactionsDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/card-transactions")
    @Timed
    public ResponseEntity<CardTransactionsDTO> updateCardTransactions(@Valid @RequestBody CardTransactionsDTO cardTransactionsDTO) throws URISyntaxException {
        log.debug("REST request to update CardTransactions : {}", cardTransactionsDTO);
        if (cardTransactionsDTO.getId() == null) {
            return createCardTransactions(cardTransactionsDTO);
        }
        CardTransactionsDTO result = cardTransactionsService.save(cardTransactionsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cardTransactionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /card-transactions : get all the cardTransactions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cardTransactions in body
     */
    @GetMapping("/card-transactions")
    @Timed
    public ResponseEntity<List<CardTransactionsDTO>> getAllCardTransactions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CardTransactions");
        Page<CardTransactionsDTO> page = cardTransactionsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/card-transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /card-transactions/:id : get the "id" cardTransactions.
     *
     * @param id the id of the cardTransactionsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cardTransactionsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/card-transactions/{id}")
    @Timed
    public ResponseEntity<CardTransactionsDTO> getCardTransactions(@PathVariable Long id) {
        log.debug("REST request to get CardTransactions : {}", id);
        CardTransactionsDTO cardTransactionsDTO = cardTransactionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cardTransactionsDTO));
    }

    /**
     * DELETE  /card-transactions/:id : delete the "id" cardTransactions.
     *
     * @param id the id of the cardTransactionsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/card-transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteCardTransactions(@PathVariable Long id) {
        log.debug("REST request to delete CardTransactions : {}", id);
        cardTransactionsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
