package com.esgin.mywallet.service.impl;

import com.esgin.mywallet.service.AccountTransactionsService;
import com.esgin.mywallet.domain.AccountTransactions;
import com.esgin.mywallet.repository.AccountTransactionsRepository;
import com.esgin.mywallet.service.dto.AccountTransactionsDTO;
import com.esgin.mywallet.service.mapper.AccountTransactionsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing AccountTransactions.
 */
@Service
@Transactional
public class AccountTransactionsServiceImpl implements AccountTransactionsService{

    private final Logger log = LoggerFactory.getLogger(AccountTransactionsServiceImpl.class);
    
    private final AccountTransactionsRepository accountTransactionsRepository;

    private final AccountTransactionsMapper accountTransactionsMapper;

    public AccountTransactionsServiceImpl(AccountTransactionsRepository accountTransactionsRepository, AccountTransactionsMapper accountTransactionsMapper) {
        this.accountTransactionsRepository = accountTransactionsRepository;
        this.accountTransactionsMapper = accountTransactionsMapper;
    }

    /**
     * Save a accountTransactions.
     *
     * @param accountTransactionsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AccountTransactionsDTO save(AccountTransactionsDTO accountTransactionsDTO) {
        log.debug("Request to save AccountTransactions : {}", accountTransactionsDTO);
        AccountTransactions accountTransactions = accountTransactionsMapper.toEntity(accountTransactionsDTO);
        accountTransactions = accountTransactionsRepository.save(accountTransactions);
        AccountTransactionsDTO result = accountTransactionsMapper.toDto(accountTransactions);
        return result;
    }

    /**
     *  Get all the accountTransactions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AccountTransactionsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccountTransactions");
        Page<AccountTransactions> result = accountTransactionsRepository.findAll(pageable);
        return result.map(accountTransactions -> accountTransactionsMapper.toDto(accountTransactions));
    }

    /**
     *  Get one accountTransactions by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AccountTransactionsDTO findOne(Long id) {
        log.debug("Request to get AccountTransactions : {}", id);
        AccountTransactions accountTransactions = accountTransactionsRepository.findOne(id);
        AccountTransactionsDTO accountTransactionsDTO = accountTransactionsMapper.toDto(accountTransactions);
        return accountTransactionsDTO;
    }

    /**
     *  Delete the  accountTransactions by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountTransactions : {}", id);
        accountTransactionsRepository.delete(id);
    }
}
