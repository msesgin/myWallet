package com.esgin.mywallet.service.impl;

import com.esgin.mywallet.service.UserCardsService;
import com.esgin.mywallet.domain.UserCards;
import com.esgin.mywallet.repository.UserCardsRepository;
import com.esgin.mywallet.service.dto.UserCardsDTO;
import com.esgin.mywallet.service.mapper.UserCardsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;


/**
 * Service Implementation for managing UserCards.
 */
@Service
@Transactional
public class UserCardsServiceImpl implements UserCardsService{

    private final Logger log = LoggerFactory.getLogger(UserCardsServiceImpl.class);
    
    private final UserCardsRepository userCardsRepository;

    private final UserCardsMapper userCardsMapper;

    public UserCardsServiceImpl(UserCardsRepository userCardsRepository, UserCardsMapper userCardsMapper) {
        this.userCardsRepository = userCardsRepository;
        this.userCardsMapper = userCardsMapper;
    }

    /**
     * Save a userCards.
     *
     * @param userCardsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UserCardsDTO save(UserCardsDTO userCardsDTO) {
        log.debug("Request to save UserCards : {}", userCardsDTO);
        UserCards userCards = userCardsMapper.toEntity(userCardsDTO);
        userCards = userCardsRepository.save(userCards);
        UserCardsDTO result = userCardsMapper.toDto(userCards);
        return result;
    }

    /**
     *  Get all the userCards.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserCardsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserCards");
        Page<UserCards> result = userCardsRepository.findAll(pageable);
        return result.map(userCards -> userCardsMapper.toDto(userCards));
    }

    /**
     *  Get one userCards by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UserCardsDTO findOne(Long id) {
        log.debug("Request to get UserCards : {}", id);
        UserCards userCards = userCardsRepository.findOne(id);
        UserCardsDTO userCardsDTO = userCardsMapper.toDto(userCards);
        return userCardsDTO;
    }

    /**
     *  Get one userCards by creditCardNumber.
     *
     *  @param creditCardNumber the creditCardNumber of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UserCardsDTO findByCreditCardNumber(String creditCardNumber) {
        log.debug("Request to get UserCards : {}", creditCardNumber);
        UserCards userCards = userCardsRepository.findByCreditCardNumber(creditCardNumber);
        UserCardsDTO userCardsDTO = userCardsMapper.toDto(userCards);
        return userCardsDTO;
    }
    
    /**
     *  Delete the  userCards by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserCards : {}", id);
        userCardsRepository.delete(id);
    }
}
