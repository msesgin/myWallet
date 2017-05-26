package com.esgin.mywallet.service.mapper;

import com.esgin.mywallet.domain.*;
import com.esgin.mywallet.service.dto.AccountTransactionsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AccountTransactions and its DTO AccountTransactionsDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, UserAccountMapper.class, })
public interface AccountTransactionsMapper extends EntityMapper <AccountTransactionsDTO, AccountTransactions> {
    @Mapping(source = "fromUserEmail.id", target = "fromUserEmailId")
    @Mapping(source = "fromUserEmail.login", target = "fromUserEmailLogin")
    @Mapping(source = "toUserEmail.id", target = "toUserEmailId")
    @Mapping(source = "toUserEmail.login", target = "toUserEmailLogin")
    @Mapping(source = "fromAccount.id", target = "fromAccountId")
    @Mapping(source = "fromAccount.accountName", target = "fromAccountAccountName")
    @Mapping(source = "toAccount.id", target = "toAccountId")
    @Mapping(source = "toAccount.accountName", target = "toAccountAccountName")
    AccountTransactionsDTO toDto(AccountTransactions accountTransactions); 
    @Mapping(source = "fromUserEmailId", target = "fromUserEmail")
    @Mapping(source = "toUserEmailId", target = "toUserEmail")
    @Mapping(source = "fromAccountId", target = "fromAccount")
    @Mapping(source = "toAccountId", target = "toAccount")
    AccountTransactions toEntity(AccountTransactionsDTO accountTransactionsDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default AccountTransactions fromId(Long id) {
        if (id == null) {
            return null;
        }
        AccountTransactions accountTransactions = new AccountTransactions();
        accountTransactions.setId(id);
        return accountTransactions;
    }
}
