package com.esgin.mywallet.repository;

import com.esgin.mywallet.domain.UserAccount;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserAccount entity.
 */
@SuppressWarnings("unused")
public interface UserAccountRepository extends JpaRepository<UserAccount,Long> {

    @Query("select userAccount from UserAccount userAccount where userAccount.user.login = ?#{principal.username}")
    Page<UserAccount> findByUserIsCurrentUser(Pageable pageable);
    
    List<UserAccount> findByUserId(Long userId);

}
