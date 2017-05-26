package com.esgin.mywallet.service.mapper;

import com.esgin.mywallet.domain.*;
import com.esgin.mywallet.service.dto.UserAccountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserAccount and its DTO UserAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, CurrencyMapper.class, })
public interface UserAccountMapper extends EntityMapper <UserAccountDTO, UserAccount> {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "currency.id", target = "currencyId")
    @Mapping(source = "currency.currencyCode", target = "currencyCurrencyCode")
    UserAccountDTO toDto(UserAccount userAccount); 
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "currencyId", target = "currency")
    UserAccount toEntity(UserAccountDTO userAccountDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default UserAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserAccount userAccount = new UserAccount();
        userAccount.setId(id);
        return userAccount;
    }
}
