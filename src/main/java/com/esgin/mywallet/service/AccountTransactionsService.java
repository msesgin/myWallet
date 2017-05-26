package com.esgin.mywallet.service;

import com.esgin.mywallet.service.dto.AccountTransactionsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing AccountTransactions.
 */
public interface AccountTransactionsService {

    /**
     * Save a accountTransactions.
     *
     * @param accountTransactionsDTO the entity to save
     * @return the persisted entity
     */
    AccountTransactionsDTO save(AccountTransactionsDTO accountTransactionsDTO);

    /**
     *  Get all the accountTransactions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AccountTransactionsDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" accountTransactions.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AccountTransactionsDTO findOne(Long id);

    /**
     *  Delete the "id" accountTransactions.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
