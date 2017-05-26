package com.esgin.mywallet.service.impl;

import com.esgin.mywallet.service.CardTransactionsService;
import com.esgin.mywallet.domain.CardTransactions;
import com.esgin.mywallet.repository.CardTransactionsRepository;
import com.esgin.mywallet.service.dto.CardTransactionsDTO;
import com.esgin.mywallet.service.mapper.CardTransactionsMapper;
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
 * Service Implementation for managing CardTransactions.
 */
@Service
@Transactional
public class CardTransactionsServiceImpl implements CardTransactionsService{

    private final Logger log = LoggerFactory.getLogger(CardTransactionsServiceImpl.class);
    
    private final CardTransactionsRepository cardTransactionsRepository;

    private final CardTransactionsMapper cardTransactionsMapper;

    public CardTransactionsServiceImpl(CardTransactionsRepository cardTransactionsRepository, CardTransactionsMapper cardTransactionsMapper) {
        this.cardTransactionsRepository = cardTransactionsRepository;
        this.cardTransactionsMapper = cardTransactionsMapper;
    }

    /**
     * Save a cardTransactions.
     *
     * @param cardTransactionsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CardTransactionsDTO save(CardTransactionsDTO cardTransactionsDTO) {
        log.debug("Request to save CardTransactions : {}", cardTransactionsDTO);
        CardTransactions cardTransactions = cardTransactionsMapper.toEntity(cardTransactionsDTO);
        cardTransactions = cardTransactionsRepository.save(cardTransactions);
        CardTransactionsDTO result = cardTransactionsMapper.toDto(cardTransactions);
        return result;
    }

    /**
     *  Get all the cardTransactions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CardTransactionsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CardTransactions");
        Page<CardTransactions> result = cardTransactionsRepository.findAll(pageable);
        return result.map(cardTransactions -> cardTransactionsMapper.toDto(cardTransactions));
    }

    /**
     *  Get one cardTransactions by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CardTransactionsDTO findOne(Long id) {
        log.debug("Request to get CardTransactions : {}", id);
        CardTransactions cardTransactions = cardTransactionsRepository.findOne(id);
        CardTransactionsDTO cardTransactionsDTO = cardTransactionsMapper.toDto(cardTransactions);
        return cardTransactionsDTO;
    }

    /**
     *  Delete the  cardTransactions by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CardTransactions : {}", id);
        cardTransactionsRepository.delete(id);
    }
}
