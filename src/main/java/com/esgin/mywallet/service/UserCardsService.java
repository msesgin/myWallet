package com.esgin.mywallet.service;

import com.esgin.mywallet.service.dto.UserCardsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing UserCards.
 */
public interface UserCardsService {

    /**
     * Save a userCards.
     *
     * @param userCardsDTO the entity to save
     * @return the persisted entity
     */
    UserCardsDTO save(UserCardsDTO userCardsDTO);

    /**
     *  Get all the userCards.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<UserCardsDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" userCards.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    UserCardsDTO findOne(Long id);

    /**
     *  Delete the "id" userCards.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

	UserCardsDTO findByCreditCardNumber(String creditCardNumber);
}
