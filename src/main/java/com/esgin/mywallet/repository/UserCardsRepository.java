package com.esgin.mywallet.repository;

import com.esgin.mywallet.domain.UserCards;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserCards entity.
 */
@SuppressWarnings("unused")
public interface UserCardsRepository extends JpaRepository<UserCards,Long> {

    @Query("select userCards from UserCards userCards where userCards.user.login = ?#{principal.username}")
    List<UserCards> findByUserIsCurrentUser();
    
    UserCards findByCreditCardNumber(String creditCardNumber);

}
