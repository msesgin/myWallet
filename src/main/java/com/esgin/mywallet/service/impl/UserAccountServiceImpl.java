package com.esgin.mywallet.service.impl;

import com.esgin.mywallet.service.UserAccountService;
import com.esgin.mywallet.domain.UserAccount;
import com.esgin.mywallet.repository.UserAccountRepository;
import com.esgin.mywallet.service.dto.UserAccountDTO;
import com.esgin.mywallet.service.mapper.UserAccountMapper;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;


/**
 * Service Implementation for managing UserAccount.
 */
@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService{

    private final Logger log = LoggerFactory.getLogger(UserAccountServiceImpl.class);
    
    private final UserAccountRepository userAccountRepository;

    private final UserAccountMapper userAccountMapper;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, UserAccountMapper userAccountMapper) {
        this.userAccountRepository = userAccountRepository;
        this.userAccountMapper = userAccountMapper;
    }

    /**
     * Save a userAccount.
     *
     * @param userAccountDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UserAccountDTO save(UserAccountDTO userAccountDTO) {
        log.debug("Request to save UserAccount : {}", userAccountDTO);
        UserAccount userAccount = userAccountMapper.toEntity(userAccountDTO);
        userAccount = userAccountRepository.save(userAccount);
        UserAccountDTO result = userAccountMapper.toDto(userAccount);
        return result;
    }

    /**
     *  Get all the userAccounts.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserAccounts");
        Page<UserAccount> result = userAccountRepository.findAll(pageable);
        return result.map(userAccount -> userAccountMapper.toDto(userAccount));
    }
    
    /**
     *  Get all the userAccounts ByUserIsCurrentUser.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserAccountDTO> findByUserIsCurrentUser(Pageable pageable) {
        log.debug("Request to get all UserAccounts");
        Page<UserAccount> result = userAccountRepository.findByUserIsCurrentUser(pageable);
        return result.map(userAccount -> userAccountMapper.toDto(userAccount));
    }

    /**
     *  Get all the userAccounts ByUser.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserAccountDTO> findByUser(Long userId) {
        log.debug("Request to get all UserAccounts");
        List<UserAccount> result = userAccountRepository.findByUserId(userId);
        return userAccountMapper.toDto(result);
    }
    
    /**
     *  Get one userAccount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UserAccountDTO findOne(Long id) {
        log.debug("Request to get UserAccount : {}", id);
        UserAccount userAccount = userAccountRepository.findOne(id);
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);
        return userAccountDTO;
    }

    /**
     *  Delete the  userAccount by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserAccount : {}", id);
        userAccountRepository.delete(id);
    }
    
    /**
     *  Add money to given account
     *
     *  @param id the id of the entity
     *  @param amount the amount of the transaction
     */
    @Override
    public void addMoney(Long id, BigDecimal amount) {
        log.debug("Request to add money UserAccount : {}", id);
        UserAccountDTO accountDto = findOne(id);
        accountDto.setBalance(accountDto.getBalance().add(amount));
        save(accountDto);
    }
    
    /**
     *  Remove money to given account
     *
     *  @param id the id of the entity
     *  @param amount the amount of the transaction
     * @throws java.lang.Exception 
     */
    @Override
    public void removeMoney(Long id, BigDecimal amount) throws java.lang.Exception {
    	 log.debug("Request to remove money UserAccount : {}", id);
         UserAccountDTO accountDto = findOne(id);
         if(accountDto.getBalance().subtract(amount).compareTo(BigDecimal.ZERO)>=0){
	         accountDto.setBalance(accountDto.getBalance().subtract(amount));
	         save(accountDto);
         }else{
        	 throw new Exception("Account Balance cannot be negative");
         }
    }
}
