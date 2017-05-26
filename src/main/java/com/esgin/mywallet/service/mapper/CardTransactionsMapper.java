package com.esgin.mywallet.service.mapper;

import com.esgin.mywallet.domain.*;
import com.esgin.mywallet.service.dto.CardTransactionsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CardTransactions and its DTO CardTransactionsDTO.
 */
@Mapper(componentModel = "spring", uses = {UserCardsMapper.class, })
public interface CardTransactionsMapper extends EntityMapper <CardTransactionsDTO, CardTransactions> {
    @Mapping(source = "creditCardNumber.id", target = "creditCardNumberId")
    @Mapping(source = "creditCardNumber.creditCardNumber", target = "creditCardNumberCreditCardNumber")
    CardTransactionsDTO toDto(CardTransactions cardTransactions); 
    @Mapping(source = "creditCardNumberId", target = "creditCardNumber")
    CardTransactions toEntity(CardTransactionsDTO cardTransactionsDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default CardTransactions fromId(Long id) {
        if (id == null) {
            return null;
        }
        CardTransactions cardTransactions = new CardTransactions();
        cardTransactions.setId(id);
        return cardTransactions;
    }
}
