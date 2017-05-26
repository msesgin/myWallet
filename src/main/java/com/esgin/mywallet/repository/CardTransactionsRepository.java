package com.esgin.mywallet.repository;

import com.esgin.mywallet.domain.CardTransactions;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CardTransactions entity.
 */
@SuppressWarnings("unused")
public interface CardTransactionsRepository extends JpaRepository<CardTransactions,Long> {

}
