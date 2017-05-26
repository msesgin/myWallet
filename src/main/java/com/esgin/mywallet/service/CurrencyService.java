package com.esgin.mywallet.service;

import com.esgin.mywallet.service.dto.CurrencyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Currency.
 */
public interface CurrencyService {

    /**
     * Save a currency.
     *
     * @param currencyDTO the entity to save
     * @return the persisted entity
     */
    CurrencyDTO save(CurrencyDTO currencyDTO);

    /**
     *  Get all the currencies.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CurrencyDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" currency.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CurrencyDTO findOne(Long id);

    /**
     *  Delete the "id" currency.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
