package com.esgin.mywallet.repository;

import com.esgin.mywallet.domain.Currency;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Currency entity.
 */
@SuppressWarnings("unused")
public interface CurrencyRepository extends JpaRepository<Currency,Long> {

}
