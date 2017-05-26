package com.esgin.mywallet.repository;

import com.esgin.mywallet.domain.AccountTransactions;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AccountTransactions entity.
 */
@SuppressWarnings("unused")
public interface AccountTransactionsRepository extends JpaRepository<AccountTransactions,Long> {

    @Query("select accountTransactions from AccountTransactions accountTransactions where accountTransactions.fromUserEmail.login = ?#{principal.username}")
    List<AccountTransactions> findByFromUserEmailIsCurrentUser();

    @Query("select accountTransactions from AccountTransactions accountTransactions where accountTransactions.toUserEmail.login = ?#{principal.username}")
    List<AccountTransactions> findByToUserEmailIsCurrentUser();

}
