package com.esgin.mywallet.service.impl;

import com.esgin.mywallet.service.CurrencyService;
import com.esgin.mywallet.domain.Currency;
import com.esgin.mywallet.repository.CurrencyRepository;
import com.esgin.mywallet.service.dto.CurrencyDTO;
import com.esgin.mywallet.service.mapper.CurrencyMapper;
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
 * Service Implementation for managing Currency.
 */
@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService{

    private final Logger log = LoggerFactory.getLogger(CurrencyServiceImpl.class);
    
    private final CurrencyRepository currencyRepository;

    private final CurrencyMapper currencyMapper;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }

    /**
     * Save a currency.
     *
     * @param currencyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CurrencyDTO save(CurrencyDTO currencyDTO) {
        log.debug("Request to save Currency : {}", currencyDTO);
        Currency currency = currencyMapper.toEntity(currencyDTO);
        currency = currencyRepository.save(currency);
        CurrencyDTO result = currencyMapper.toDto(currency);
        return result;
    }

    /**
     *  Get all the currencies.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CurrencyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Currencies");
        Page<Currency> result = currencyRepository.findAll(pageable);
        return result.map(currency -> currencyMapper.toDto(currency));
    }

    /**
     *  Get one currency by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CurrencyDTO findOne(Long id) {
        log.debug("Request to get Currency : {}", id);
        Currency currency = currencyRepository.findOne(id);
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);
        return currencyDTO;
    }

    /**
     *  Delete the  currency by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Currency : {}", id);
        currencyRepository.delete(id);
    }
}
