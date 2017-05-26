package com.esgin.mywallet.service;

import com.esgin.mywallet.service.dto.UserAccountDTO;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing UserAccount.
 */
public interface UserAccountService {

    /**
     * Save a userAccount.
     *
     * @param userAccountDTO the entity to save
     * @return the persisted entity
     */
    UserAccountDTO save(UserAccountDTO userAccountDTO);

    /**
     *  Get all the userAccounts.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<UserAccountDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" userAccount.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    UserAccountDTO findOne(Long id);
    
    /**
     *  Get all the userAccounts.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<UserAccountDTO> findByUserIsCurrentUser(Pageable pageable);
    
    /**
     *  Get all the userAccounts.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    List<UserAccountDTO> findByUser(Long userId);

    /**
     *  Delete the "id" userAccount.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *  Add money to given account
     *
     *  @param id the id of the entity
     *  @param amount the amount of the transaction
     */
	void addMoney(Long id, BigDecimal amount);
	
	/**
     *  Remove money to given account
     *
     *  @param id the id of the entity
     *  @param amount the amount of the transaction
	 * @throws Exception 
     */
	void removeMoney(Long id, BigDecimal amount) throws Exception;
}
