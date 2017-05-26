package com.esgin.mywallet.service.mapper;

import com.esgin.mywallet.domain.*;
import com.esgin.mywallet.service.dto.UserCardsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserCards and its DTO UserCardsDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, UserAccountMapper.class, })
public interface UserCardsMapper extends EntityMapper <UserCardsDTO, UserCards> {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "toAccount.id", target = "toAccountId")
    @Mapping(source = "toAccount.accountName", target = "toAccountAccountName")
    UserCardsDTO toDto(UserCards userCards); 
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "toAccountId", target = "toAccount")
    UserCards toEntity(UserCardsDTO userCardsDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default UserCards fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserCards userCards = new UserCards();
        userCards.setId(id);
        return userCards;
    }
}
