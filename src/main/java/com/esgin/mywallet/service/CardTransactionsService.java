package com.esgin.mywallet.service;

import com.esgin.mywallet.service.dto.CardTransactionsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing CardTransactions.
 */
public interface CardTransactionsService {

    /**
     * Save a cardTransactions.
     *
     * @param cardTransactionsDTO the entity to save
     * @return the persisted entity
     */
    CardTransactionsDTO save(CardTransactionsDTO cardTransactionsDTO);

    /**
     *  Get all the cardTransactions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CardTransactionsDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" cardTransactions.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CardTransactionsDTO findOne(Long id);

    /**
     *  Delete the "id" cardTransactions.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
